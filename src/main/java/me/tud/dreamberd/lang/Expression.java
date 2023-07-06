package me.tud.dreamberd.lang;

import me.tud.dreamberd.lexer.Token;

public abstract class Expression<T> implements Initiable {

    private final Token[] tokens;

    public Expression(Token[] tokens) {
        this.tokens = tokens;
    }

    public abstract T get(Context context);

    public abstract Class<? extends T> getReturnType();

    public Token[] getTokens() {
        return tokens;
    }

}
