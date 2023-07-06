package me.tud.dreamberd.lang.variable;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.exceptions.VariableException;
import me.tud.dreamberd.lang.Context;
import me.tud.dreamberd.lang.Expression;
import me.tud.dreamberd.lang.Identifier;
import me.tud.dreamberd.utils.ArrayUtils;

public class Variable<T> extends Expression<T> {

    private final Identifier identifier;
    private final Class<T> superType;
    private final Class<? extends T>[] returnTypes;

    @SuppressWarnings("unchecked")
    public Variable(Identifier identifier, Class<? extends T>[] returnTypes) {
        super(identifier.getTokens());
        this.identifier = identifier;
        this.superType = (Class<T>) ArrayUtils.getSuperclass(returnTypes);
        this.returnTypes = returnTypes;
    }

    @Override
    public void init(Context context) throws ParseException {
        if (!context.getVariableMap().variableExists(identifier.get()))
            throw VariableException.doesntExist(identifier.get());
    }

    @Override
    public T get(Context context) {
        return context.getVariableMap().getVariable(identifier.get(), returnTypes);
    }

    @Override
    public Class<? extends T> getReturnType() {
        return superType;
    }

}
