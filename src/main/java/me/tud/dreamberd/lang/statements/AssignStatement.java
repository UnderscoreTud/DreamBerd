package me.tud.dreamberd.lang.statements;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lang.Context;
import me.tud.dreamberd.lang.Expression;
import me.tud.dreamberd.lang.Identifier;
import me.tud.dreamberd.lang.Statement;
import me.tud.dreamberd.lang.variable.Lifetime;
import me.tud.dreamberd.lang.variable.VariableMap.VariableInfo;
import me.tud.dreamberd.lexer.Token;

import java.util.Optional;

public class AssignStatement extends Statement {

    private final byte modifiers;
    private final Identifier identifier;
    private final Lifetime lifetime;
    private final Expression<?> expression;
    private final int priority;
    private VariableInfo info;

    public AssignStatement(byte modifiers, Identifier identifier, Lifetime lifetime, Expression<?> expression, int priority, Token[] tokens) {
        super(tokens);
        this.modifiers = modifiers;
        this.identifier = identifier;
        this.lifetime = lifetime;
        this.expression = expression;
        this.priority = priority;
    }

    @Override
    public void init(Context context) throws ParseException {
        info = context.getVariableMap().declareVariable(modifiers, identifier, lifetime, priority);
        expression.init(context);
    }

    @Override
    public void execute(Context context) {
        if (info != null && context.getVariableMap().variableExists(info))
            info.setValue(expression.get(context));
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Optional<Lifetime> getLifetime() {
        return Optional.ofNullable(lifetime);
    }

}
