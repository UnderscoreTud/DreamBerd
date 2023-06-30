package me.tud.dreamberd;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class UnsureBoolean {

    public static final UnsureBoolean FALSE = new UnsureBoolean(false);
    public static final UnsureBoolean MAYBE = new UnsureBoolean(null);
    public static final UnsureBoolean TRUE = new UnsureBoolean(true);

    private @Nullable Boolean bool;

    public UnsureBoolean(@Nullable Boolean bool) {
        this.bool = bool;
    }

    public void set(Boolean value) {
        bool = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UnsureBoolean that = (UnsureBoolean) o;
        return Objects.equals(bool, that.bool);
    }

    @Override
    public int hashCode() {
        return bool != null ? bool.hashCode() : 0;
    }

    @Override
    public String toString() {
        return bool == null ? "maybe" : bool ? "true" : "false";
    }

}
