package me.tud.dreamberd.lexer;

import me.tud.dreamberd.MagicValues;
import me.tud.dreamberd.UnsureBoolean;
import me.tud.dreamberd.lang.VariableModifiers;
import me.tud.dreamberd.lang.variable.Lifetime;
import me.tud.dreamberd.utils.StringReader;
import me.tud.dreamberd.utils.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public enum TokenType {

    END_STATEMENT(reader -> {
        int[] amount = {0};
        String string = reader.readUntil(c -> {
            if (c == '!') {
                amount[0]++;
                return false;
            } else if (c == '¡') {
                amount[0]--;
                return false;
            }
            return true;
        });
        if (string.isEmpty())
            return null;
        return amount[0];
    }),
    END_STATEMENT_DEBUG('?'),
    LEFT_PAREN('('),
    RIGHT_PAREN(')'),
    LEFT_BRACKET('['),
    RIGHT_BRACKET(']'),
    LEFT_CURLY_BRACKET('{'),
    RIGHT_CURLY_BRACKET('}'),
    PERIOD('.'),
    COMMA(','),
    COLON(':'),
    INDENT(reader -> {
        int level = 0;
        int start = 0;
        loop: while (reader.canRead(3)) {
            for (int i = 0; i < 3; i++) {
                if (reader.read() != ' ')
                    break loop;
            }
            start = reader.getCursor();
            level++;
        }
        if (level == 0)
            return null;
        reader.setCursor(start);
        return level;
    }),
    WHITESPACE(' '),
    COMMENT(reader -> {
        if (!reader.isNext("//"))
            return null;
        return reader.readUntil(c -> c == '\n' || c == '\r');
    }),
    VARIABLE_MODIFIER(reader -> {
        int start = reader.getCursor();
        if (reader.isNext("var"))
            return VariableModifiers.VAR;
        reader.setCursor(start);
        return reader.isNext("const") ? VariableModifiers.CONST : null;
    }),
    NEW_LINE(reader -> {
        String string = reader.readUntil(c -> c != '\n' && c != '\r');
        return string.isEmpty() ? null : MagicValues.EMPTY_VALUE;
    }),
    ARITHMETIC_OPERATOR(reader -> {
        switch (reader.peek()) {
            case '+', '-', '/' -> {
                return reader.read() + "";
            }
            case '*' -> {
                reader.read();
                if (reader.canRead() && reader.peek() == '*') {
                    reader.read();
                    return "**";
                }
                return "*";
            }
        }
        return null;
    }),
    FILE_SEPARATOR(reader -> {
        if (!reader.canRead(5))
            return null;
        return reader.readUntil(c -> c != '=').length() < 5 ? null : MagicValues.EMPTY_VALUE;
    }),
    ARROW_OPERATOR(reader -> {
        if (!reader.canRead(2))
            return null;
        return reader.read() == '=' && reader.read() == '>' ? MagicValues.EMPTY_VALUE : null;
    }),
    EQUALS(reader -> {
        int i = 0;
        for (; reader.canRead() && i < 4; i++) {
            if (reader.peek() != '=')
                return i == 0 ? null : i;
            reader.read();
        }
        return i;
    }),
    IF("if"),
    ELSE("else"),
    WHEN("when"),
    PREVIOUS("previous"),
    NEXT("next"),
    AFTER("after"),
    AWAIT("await"),
    DELETE("delete"),
    RETURN("return"),
    FUNCTION_DEFINITION(reader -> {
        String function = MagicValues.FUNCTION_KEYWORD;
        String string = reader.readUntil(c -> c == ' ');
        if (string.isBlank() || string.length() > function.length())
            return null;
        if (function.equals(string))
            return MagicValues.EMPTY_VALUE;
        int lastIndex = -1;
        for (int i = 0; i < string.length(); i++) {
            lastIndex = function.indexOf(string.charAt(i), lastIndex + 1);
            if (lastIndex == -1)
                return null;
        }
        return MagicValues.EMPTY_VALUE;
    }),
    LITERAL_NUMBER(reader -> {
        String number = reader.readUntil(c -> Character.digit(c, 10) == -1 && c != '-' && c != '.');
        try {
            if (number.contains("."))
                return Double.parseDouble(number);
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }),
    LIFETIME(reader -> {
        if (reader.read() != '<')
            return null;
        Number number = (Number) LITERAL_NUMBER.parser.apply(reader);
        // TODO finish parser for this
        if (number == null || number instanceof Double || reader.read() != '>')
            return null;
        return new Lifetime(number.longValue());
    }),
    COMPARISON_OPERATOR(reader -> switch (reader.peek()) {
        case '>', '<' -> reader.read();
        default -> null;
    }),
    LITERAL_BOOLEAN(reader -> {
        int start = reader.getCursor();
        if (reader.isNext("true", true))
            return UnsureBoolean.TRUE;
        reader.setCursor(start);
        if (reader.isNext("maybe", true))
            return UnsureBoolean.MAYBE;
        reader.setCursor(start);
        return reader.isNext("false", true) ? UnsureBoolean.FALSE : null;
    }),
    LITERAL_STRING(reader -> {
        Predicate<Character> quotePredicate = c -> c != '\'' && c != '"';
        if (reader.peek() == '\'' || reader.peek() == '"') {
            String startQuotes = reader.readUntil(quotePredicate);
            String endQuotes = StringUtils.reverse(startQuotes);
            boolean escape = false;
            StringBuilder builder = new StringBuilder();
            while (reader.canRead()) {
                if (escape) {
                    builder.append(reader.read());
                    escape = false;
                    continue;
                }
                if (reader.peek() == '\\') {
                    reader.read();
                    escape = true;
                    continue;
                }
                builder.append(reader.readUntil(quotePredicate.negate().or(c -> c == '\\')));
                String quotes = reader.readUntil(quotePredicate);
                if (quotes.equals(endQuotes))
                    break;
                builder.append(quotes);
            }
            return builder.toString();
        }
        String string = reader.readUntil(c -> !Character.isLetter(c) || Character.isWhitespace(c) || c == '!');
        if (string.isBlank())
            return null;
        return string;
    }),
    OTHER(StringReader::read)
    ;

    private final Function<StringReader, Object> parser;

    TokenType(char c) {
        this(reader -> reader.read() == c ? MagicValues.EMPTY_VALUE : null);
    }

    TokenType(String string) {
        this(reader -> reader.isNext(string) ? MagicValues.EMPTY_VALUE : null);
    }

    TokenType(Function<StringReader, Object> parser) {
        this.parser = parser;
    }

    public Function<StringReader, Object> getParser() {
        return parser;
    }

    public boolean isSignificant() {
        return !TokenGroup.INSIGNIFICANT.contains(this);
    }

    public boolean isIdentifier() {
        return TokenGroup.IDENTIFIER.contains(this);
    }

    public static @Nullable Token parse(StringReader reader) {
        return parse(values(), reader);
    }

    public static @Nullable Token parse(TokenType[] types, StringReader reader) {
        int start = reader.getCursor();
        for (TokenType type : types) {
            StringReader clone = reader.clone();
            Object value = type.parser.apply(clone);
            if (value == null)
                continue;
            reader.setCursor(clone.getCursor());
            return new Token(
                    type,
                    value == MagicValues.EMPTY_VALUE ? null : value,
                    reader.getString().substring(start, reader.getCursor()),
                    start,
                    reader.getCursor()
            );
        }
        return null;
    }

}
