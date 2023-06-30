package me.tud.dreamberd.utils;

import java.util.Locale;
import java.util.function.Predicate;

public class StringReader implements Cloneable {

    private final String string;
    private int cursor = 0;

    public StringReader(String string) {
        this.string = string;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public String getString() {
        return string;
    }

    public int getRemaining() {
        return string.length() - cursor;
    }

    public String finish() {
        String remaining = string.substring(cursor);
        cursor = string.length();
        return remaining;
    }

    public String readUntil(Predicate<Character> predicate) {
        if (!canRead())
            return "";
        StringBuilder builder = new StringBuilder();
        while (canRead() && !predicate.test(peek())) {
            builder.append(read());
        }
        return builder.toString();
    }

    public boolean canRead() {
        return canRead(1);
    }

    public boolean canRead(int offset) {
        return cursor + offset <= string.length();
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public String read(int amount) {
        String string = this.string.substring(cursor, cursor + amount);
        cursor += amount;
        return string;
    }

    public boolean isNext(String match) {
        return isNext(match, false);
    }

    public boolean isNext(String match, boolean ignoreCase) {
        if (!canRead(match.length()))
            return false;
        String read = read(match.length());
        return ignoreCase ? match.equalsIgnoreCase(read) : match.equals(read);
    }

    public char peek() {
        return peek(0);
    }

    public char peek(int offset) {
        return string.charAt(cursor + offset);
    }

    @Override
    public StringReader clone() {
        try {
            return (StringReader) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
