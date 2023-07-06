package me.tud.dreamberd.lang;

import me.tud.dreamberd.lexer.Token;

public class Identifier extends Literal<String> {

    public Identifier(String literal, Token[] tokens) {
        super(literal, tokens);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

}
