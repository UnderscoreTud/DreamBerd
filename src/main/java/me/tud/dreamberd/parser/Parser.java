package me.tud.dreamberd.parser;

import me.tud.dreamberd.Script;
import me.tud.dreamberd.UnsureBoolean;
import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lang.*;
import me.tud.dreamberd.lang.expressions.FunctionCallExpr;
import me.tud.dreamberd.lang.statements.AssignStatement;
import me.tud.dreamberd.lang.statements.ReassignStatement;
import me.tud.dreamberd.lang.variable.Lifetime;
import me.tud.dreamberd.lang.variable.Variable;
import me.tud.dreamberd.lexer.Lexer;
import me.tud.dreamberd.lexer.Token;
import me.tud.dreamberd.lexer.TokenType;
import me.tud.dreamberd.utils.ArrayReader;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class Parser extends ArrayReader<Token> {

    private final Context context;
    private final int indentationLevel;
    private final boolean reverseIndents;
    private final Set<Identifier> variables;
    private int line;

    public Parser(String input) {
        this(new Context(), input);
    }

    public Parser(Context context, String input) {
        this(context, new Lexer(input).tokenize().toArray(new Token[0]), new HashSet<>());
    }

    public Parser(Context context, Token[] tokens, Set<Identifier> variables) {
        this(tokens, context, getIndentationLevel(tokens), getIndentationLevel(tokens) > 0, variables);
    }

    private Parser(Token[] tokens, Context context, int indentationLevel, boolean reverseIndents, Set<Identifier> variables) {
        super(tokens);
        this.context = context;
        this.indentationLevel = indentationLevel;
        this.reverseIndents = reverseIndents;
        this.variables = variables;
    }

    public RootNode parse() {
        int start = cursor;
        return new RootNode(parse0(), tokensFrom(start));
    }

    private Node parse0() {
        readUntil(token -> !token.is(TokenType.NEW_LINE));
        Node node = parseNode();
        readUntil(token -> !token.is(TokenType.WHITESPACE, TokenType.COMMENT));
        skipWhitespace();
        if (canRead())
            eat(TokenType.NEW_LINE);
        if (canRead()) {
            Node next = parse0();
            next.setPrevious(node);
            node.setNext(next);
        }
        return node;
    }

    Node parseNode() {
        if (!canRead())
            throw  new ParseException("File ended unexpectedly");
        int indent = peek().is(TokenType.INDENT) ? eat(TokenType.INDENT, Integer.class) : 0;
        int start = cursor;
        if (indent != indentationLevel)
            throw new ParseException("The indentation level does not match: " + indentationLevel);
        return new StatementNode(this, line, statement(), tokensFrom(start));
    }

    public Statement statement() {
        Statement statement;
        if (peek().is(TokenType.VARIABLE_MODIFIER)) {
            statement = assignment();
        } else if (peek().type().isIdentifier() && peekSig(1).is(TokenType.EQUALS)) {
            statement = reassign();
        } else {
            Expression<?> expression = expression();
            if (!(expression instanceof StatementExpression<?> statementExpression))
                throw new ParseException("fdshakjlfhsdjk");
            statement = statementExpression.asStatement();
        }

        skipWhitespace();

        if (!canRead())
            return statement;

        if (peek().is(TokenType.END_STATEMENT_DEBUG)) {
            eat(TokenType.END_STATEMENT_DEBUG);
            System.out.println(statement);
        } else if (peek().is(TokenType.END_STATEMENT)) {
            eat(TokenType.END_STATEMENT);
        }

        return statement;
    }

    private AssignStatement assignment() {
        int start = cursor;
        byte modifiers = 0;
        modifiers |= eat(TokenType.VARIABLE_MODIFIER, Integer.class);
        eat(TokenType.WHITESPACE);
        modifiers |= eat(TokenType.VARIABLE_MODIFIER, Integer.class) << 1;
        eat(TokenType.WHITESPACE);
        Identifier identifier = identifier();
        Lifetime lifetime = peek().is(TokenType.LIFETIME) ? eat(TokenType.LIFETIME, Lifetime.class) : null;
        if (peek().is(TokenType.COLON)) {
            eat(TokenType.COLON);
            skipWhitespace();
            identifier();
        }
        if (eat(TokenType.EQUALS, true, Integer.class) != 1)
            throw new ParseException("fdsfsa"); // TODO actual error message
        skipWhitespace();
        if (lifetime != null && lifetime.getLines() != null && lifetime.getLines() > 0)
            variables.add(identifier);
        return new AssignStatement(
                modifiers,
                identifier,
                lifetime,
                expression(),
                canRead() && peek().is(TokenType.END_STATEMENT) ? peek().value(Integer.class) : 1,
                tokensFrom(start)
        );
    }

    private ReassignStatement reassign() {
        int start = cursor;
        Identifier identifier = identifier();
        if (eat(TokenType.EQUALS, true, Integer.class) != 1)
            throw new ParseException("fdsfsa"); // TODO actual error message
        skipWhitespace();
        return new ReassignStatement(
                identifier,
                expression(),
                tokensFrom(start)
        );
    }

    private FunctionCallExpr functionCall() {
        int start = cursor;
        Identifier identifier = identifier();
        eat(TokenType.LEFT_PAREN);
        skipWhitespace();
        Expression<?>[] arguments = peek().is(TokenType.RIGHT_PAREN) ? new Expression[0] : arguments();
        skipWhitespace();
        if (peek().is(TokenType.RIGHT_PAREN))
            eat(TokenType.RIGHT_PAREN);
        return new FunctionCallExpr(identifier, arguments, tokensFrom(start));
    }

    private Expression<?>[] arguments() {
        List<Expression<?>> arguments = new LinkedList<>();
        arguments.add(expression());
        while (canRead() && peekSig().is(TokenType.COMMA)) {
            eat(TokenType.COMMA, true);
            skipWhitespace();
            arguments.add(expression());
        }
        return arguments.toArray(new Expression[0]);
    }

    public Expression<?> expression() {
        int start = cursor;
        if (peek().type().isIdentifier()) {
            Identifier identifier = identifier();
            if (peek().is(TokenType.LEFT_PAREN)) {
                cursor = start;
                return functionCall();
            }
            if (variables.contains(identifier))
                return new Variable<Object>(identifier, new Class[]{Object.class});
        }
        cursor = start;
        return literal();
    }

    public Literal<?> literal() {
        int start = cursor;
        return switch (peek().type()) {
            case LITERAL_NUMBER -> new Literal<>((Number) read().value(), tokensFrom(start));
            case LITERAL_BOOLEAN -> new Literal<>((UnsureBoolean) read().value(), tokensFrom(start));
            case LITERAL_STRING -> new Literal<>((String) read().value(), tokensFrom(start));
            default -> throw new ParseException("Expected a literal but got '" + read() + "' instead");
        };
    }

    private Identifier identifier() {
        if (!peek().type().isIdentifier())
            throw new ParseException("Expected an identifier but got '" + peek() + "' instead");
        int start = cursor;
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(read().raw());
        } while (canRead() && peek().type().isIdentifier());
        return new Identifier(builder.toString(), tokensFrom(start));
    }

    private Token current() {
        return peek(-1);
    }

    @SuppressWarnings("unchecked")
    private <T> T eat(TokenType expected) {
        return (T) eat(expected, Object.class);
    }

    private <T> T eat(TokenType expected, Class<T> returnType) {
        return eat(expected, false, returnType);
    }

    @SuppressWarnings("unchecked")
    private <T> T eat(TokenType expected, boolean skipWhitespace) {
        return (T) eat(expected, skipWhitespace, Object.class);
    }

    private <T> T eat(TokenType expected, boolean skipWhitespace, Class<T> returnType) {
        if (skipWhitespace)
            skipWhitespace();
        Token got = read();
        if (!got.is(expected))
            throw new ParseException("Expected '" + expected + "' but got '" + got + "' instead");
        return returnType.cast(got.value());
    }

    private void skipWhitespace() {
        readUntil(token -> !token.is(TokenType.WHITESPACE));
    }

    @Override
    public Token read() {
        Token token = super.read();
        if (token.is(TokenType.NEW_LINE))
            line++;
        return token;
    }

    public Token readSig() {
        readUntil(token -> token.type().isSignificant());
        return read();
    }

    public Token peekSig() {
        return peekSig(0);
    }

    public Token peekSig(int offset) {
        int current = cursor;
        int counter = 0;
        boolean reverse = offset < 0;
        if (reverse)
            offset = Math.abs(offset);
        while (counter <= offset) {
            if (array[reverse ? current-- : current++].type().isSignificant())
                counter++;
        }
        return array[current - 1];
    }

    public Token[] readUntil(Predicate<Token> predicate) {
        if (!canRead())
            return new Token[0];
        int start = cursor;
        int offset = 0;
        while (canRead() && !predicate.test(peek())) {
            read();
            offset++;
        }
        return Arrays.copyOfRange(array, start, start + offset);
    }

    public @Nullable Script getScript() {
        return context.getScript();
    }

    public Context getContext() {
        return context;
    }

    public int getIndentationLevel() {
        return indentationLevel;
    }

    public boolean isReverseIndents() {
        return reverseIndents;
    }

    public Set<Identifier> getVariables() {
        return variables;
    }

    private Token[] tokensFrom(int start) {
        return Arrays.copyOfRange(array, start, cursor);
    }

    private static int getIndentationLevel(Token[] tokens) {
        for (Token token : tokens) {
            if (token.is(TokenType.INDENT))
                return token.value(Integer.class);
            if (token.type().isSignificant())
                break;
        }
        return 0;
    }

}
