package me.tud.dreamberd.lang;

public final class VariableModifiers {

    public static final int CONST = 0;
    public static final int VAR = 1;

    private VariableModifiers() {
        throw new UnsupportedOperationException();
    }

    public static boolean isAssignable(byte modifiers) {
        return (modifiers & VAR) == VAR;
    }

    public static boolean isEditable(byte modifiers) {
        return (modifiers >> 1 & VAR) == VAR;
    }

}
