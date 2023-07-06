package me.tud.dreamberd.exceptions;

public class VariableException extends DreamBerdException {

    public VariableException(String message) {
        super(message);
    }

    public static VariableException doesntExist(String identifier) {
        return new VariableException(String.format("The variable '%s' is does not exist", identifier));
    }

    public static VariableException cannotBe(String identifier, String what) {
        return new VariableException(String.format("The variable '%s' cannot be " + what, identifier));
    }

    public static VariableException alreadyDeclared(String identifier) {
        return new VariableException(String.format("The variable '%s' is already declared", identifier));
    }

}
