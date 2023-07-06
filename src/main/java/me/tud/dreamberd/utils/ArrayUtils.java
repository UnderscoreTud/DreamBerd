package me.tud.dreamberd.utils;

public final class ArrayUtils {

    private ArrayUtils() {
        throw new UnsupportedOperationException();
    }

    public static Character[] box(char[] array) {
        Character[] boxedArray = new Character[array.length];
        for (int i = 0; i < boxedArray.length; i++)
            boxedArray[i] = array[i];
        return boxedArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? super T> getSuperclass(Class<? extends T>[] classes) {
        if (classes.length == 0)
            return Object.class;
        Class<? super T> currentSuperclass = null;
        for (Class<? extends T> c : classes) {
            if (currentSuperclass == null) {
                currentSuperclass = (Class<? super T>) c.getSuperclass();
                continue;
            }
            while (!currentSuperclass.isAssignableFrom(c))
                currentSuperclass = currentSuperclass.getSuperclass();
        }
        return currentSuperclass;
    }

}
