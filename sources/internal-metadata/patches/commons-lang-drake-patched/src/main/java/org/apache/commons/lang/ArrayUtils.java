package org.apache.commons.lang;

/**
 * Legacy ArrayUtils facade backed by Commons Lang 3.
 */
public final class ArrayUtils {

    private ArrayUtils() {
    }

    public static <T> T[] addAll(T[] array1, T... array2) {
        return org.apache.commons.lang3.ArrayUtils.addAll(array1, array2);
    }
}
