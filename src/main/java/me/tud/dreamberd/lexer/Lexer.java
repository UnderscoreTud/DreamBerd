package me.tud.dreamberd.lexer;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.utils.StringReader;

import java.util.LinkedList;
import java.util.List;

public class Lexer {

    private final StringReader reader;

    public Lexer(String string) {
        this(new StringReader(string));
    }

    public Lexer(StringReader reader) {
        this.reader = reader;
    }

    public List<Token> tokenize() throws ParseException {
        List<Token> tokens = new LinkedList<>();
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
