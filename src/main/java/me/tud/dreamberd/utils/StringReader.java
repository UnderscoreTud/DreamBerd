package me.tud.dreamberd.utils;

import java.util.function.Predicate;

public class StringReader extends ArrayReader<Character> implements Cloneable {

    private final String string;

    public StringReader(String string) {
        super(ArrayUtils.box(string.toCharArray()));
        this.string = string;
    }

    public String getString() {
        return string;
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
        while (canRead() && !predicate.test(peek()))
            builder.append(read());
        return builder.toString();
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

    @Override
    public StringReader clone() {
        return (StringReader) super.clone();
    }

}
