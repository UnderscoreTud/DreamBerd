package me.tud.dreamberd.lang;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lexer.Token;

public class Literal<T> extends Expression<T> {

    private final T literal;

    public Literal(T literal, Token[] tokens) {
        super(tokens);
        this.literal = literal;
    }

    @Override
    public void init(Context context) throws ParseException {}

    @Override
    public T get(Context context) {
        return literal;
    }

    public T get() {
        return literal;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends T> getReturnType() {
        return (Class<? extends T>) literal.getClass();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' + literal + '}';
    }

}
