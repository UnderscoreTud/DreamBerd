package me.tud.dreamberd.lexer;

import java.util.Set;

public enum TokenGroup {

    INSIGNIFICANT(TokenType.WHITESPACE, TokenType.NEW_LINE, TokenType.COMMENT),
    IDENTIFIER(TokenType.LITERAL_STRING, TokenType.LITERAL_NUMBER, TokenType.LITERAL_BOOLEAN, TokenType.OTHER)
    ;

    private final Set<TokenType> types;

    TokenGroup(TokenType... types) {
        this.types = Set.of(types);
    }

    public boolean contains(TokenType type) {
        return types.contains(type);
    }

}
