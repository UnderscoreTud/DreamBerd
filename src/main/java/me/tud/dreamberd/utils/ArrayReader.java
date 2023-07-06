package me.tud.dreamberd.utils;

public class ArrayReader<T> implements Cloneable {

    protected final T[] array;
    protected int cursor = 0;

    public ArrayReader(T[] array) {
        this.array = array;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getRemaining() {
        return array.length - cursor;
    }

    public boolean canRead() {
        return canRead(1);
    }

    public boolean canRead(int offset) {
        return cursor + offset <= array.length;
    }

    public T read() {
        return array[cursor++];
    }

    public T peek() {
        return peek(0);
    }

    public T peek(int offset) {
        return array[cursor + offset];
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayReader<T> clone() {
        try {
            return (ArrayReader<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
