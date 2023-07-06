package me.tud.dreamberd.lang;

import me.tud.dreamberd.lexer.Token;

public abstract class Statement implements Initiable {

    private final Token[] tokens;

    public Statement(Token[] tokens) {
        this.tokens = tokens;
    }

    public abstract void execute(Context context);

    public Token[] getTokens() {
        return tokens;
    }

}
