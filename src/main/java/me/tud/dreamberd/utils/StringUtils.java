package me.tud.dreamberd.utils;

import org.jetbrains.annotations.Contract;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    public static String reverse(String string) {
        char[] chars = string.toCharArray();
        char[] reverse = new char[chars.length];
        for (int i = 0; i < reverse.length; i++)
            reverse[i] = chars[chars.length - i - 1];
        return String.valueOf(reverse);
    }

}
