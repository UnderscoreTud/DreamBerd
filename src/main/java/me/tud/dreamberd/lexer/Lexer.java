package me.tud.dreamberd.lexer;

import me.tud.dreamberd.ParseException;
import me.tud.dreamberd.utils.StringReader;

import java.util.LinkedList;

public class Lexer {

    private final StringReader reader;

    public Lexer(String string) {
        this(new StringReader(string));
    }

    public Lexer(StringReader reader) {
        this.reader = reader;
    }

    public LinkedList<Token> tokenize() throws ParseException {
        LinkedList<Token> tokens = new LinkedList<>();
        while (reader.canRead())
            tokens.add(nextToken());
        return tokens;
    }

    public Token nextToken() throws ParseException {
        Token token = TokenType.parse(reader);
        if (token == null)
            throw new ParseException(reader);
        return token;
    }

    public StringReader getReader() {
        return reader;
    }

}
