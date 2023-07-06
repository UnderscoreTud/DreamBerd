package me.tud.dreamberd.lang;

import me.tud.dreamberd.Script;
import me.tud.dreamberd.exceptions.DreamBerdException;
import me.tud.dreamberd.lang.variable.VariableMap;
import org.jetbrains.annotations.Nullable;

public class Context {

    private final @Nullable Script script;
    private final VariableMap variableMap;
    private final @Nullable Context parentContext;

    public Context() {
        this(null);
    }

    public Context(@Nullable Script script) {
        this(script, new VariableMap());
    }

    public Context(@Nullable Script script, VariableMap variableMap) {
        this(script, variableMap, null);
    }

    private Context(@Nullable Script script, VariableMap variableMap, Context parentContext) {
        this.script = script;
        this.variableMap = variableMap;
        this.parentContext = parentContext;
    }

    public @Nullable Script getScript() {
        return script;
    }

    public VariableMap getVariableMap() {
        return variableMap;
    }

    public Context enterScope() {
        return new Context(script, new VariableMap(variableMap), this);
    }

    public Context exitScope() {
        if (parentContext == null)
            throw new DreamBerdException("Cannot exit the root scope");
        return parentContext;
    }

}
