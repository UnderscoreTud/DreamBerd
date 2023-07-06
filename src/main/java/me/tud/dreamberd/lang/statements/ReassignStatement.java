package me.tud.dreamberd.lang.statements;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.exceptions.VariableException;
import me.tud.dreamberd.lang.Context;
import me.tud.dreamberd.lang.Expression;
import me.tud.dreamberd.lang.Identifier;
import me.tud.dreamberd.lang.Statement;
import me.tud.dreamberd.lang.variable.VariableMap.VariableInfo;
import me.tud.dreamberd.lexer.Token;
import me.tud.dreamberd.lang.VariableModifiers;

public class ReassignStatement extends Statement {

    private final Identifier identifier;
    private final Expression<?> expression;
    private VariableInfo info;

    public ReassignStatement(Identifier identifier, Expression<?> expression, Token[] tokens) {
        super(tokens);
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public void init(Context context) throws ParseException {
        info = context.getVariableMap().getVariableInfo(identifier.get());
        if (!VariableModifiers.isAssignable(info.getModifiers()))
            throw VariableException.cannotBe(identifier.get(), "re-assigned");
        expression.init(context);
    }

    @Override
    public void execute(Context context) {
        info.setValue(expression.get(context));
    }

}
