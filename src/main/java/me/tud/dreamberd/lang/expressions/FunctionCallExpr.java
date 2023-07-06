package me.tud.dreamberd.lang.expressions;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lang.Context;
import me.tud.dreamberd.lang.Expression;
import me.tud.dreamberd.lang.Identifier;
import me.tud.dreamberd.lang.StatementExpression;
import me.tud.dreamberd.lexer.Token;

public class FunctionCallExpr extends StatementExpression<Object> {

    private final Identifier identifier;
    private final Expression<?>[] arguments;

    public FunctionCallExpr(Identifier identifier, Expression<?>[] arguments, Token[] tokens) {
        super(tokens);
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public void init(Context context) throws ParseException {
        for (Expression<?> argument : arguments)
            argument.init(context);
    }

    @Override
    public Object get(Context context) {
        for (Expression<?> argument : arguments) {
            System.out.println(argument.get(context));
        }
        return null;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

}
