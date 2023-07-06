package me.tud.dreamberd.lang;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lexer.Token;

public abstract class StatementExpression<T> extends Expression<T> {

    public StatementExpression(Token[] tokens) {
        super(tokens);
    }

    public Statement asStatement() {
        return new AsStatement();
    }

    private class AsStatement extends Statement {

        public AsStatement() {
            super(StatementExpression.this.getTokens());
        }

        @Override
        public void init(Context context) throws ParseException {
            StatementExpression.this.init(context);
        }

        @Override
        public void execute(Context context) {
            StatementExpression.this.get(context);
        }

    }

}
