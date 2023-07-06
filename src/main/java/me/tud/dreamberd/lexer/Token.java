package me.tud.dreamberd.lexer;

public record Token(TokenType type, Object value, String raw, int start, int end) {

    public <T> T value(Class<T> expectedType) {
        return expectedType.cast(value);
    }

    public String substring(String string) {
        return string.substring(start, end);
    }

    public boolean is(TokenType... types) {
        for (TokenType type : types) {
            if (this.type == type)
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Token[type=" + type + (value == null ? "" : ", value=" + value) +
                ", start=" + start + ", end=" + end + ']';
    }

}
