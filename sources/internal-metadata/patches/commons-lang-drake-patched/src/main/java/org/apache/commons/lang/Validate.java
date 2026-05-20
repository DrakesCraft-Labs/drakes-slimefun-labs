package org.apache.commons.lang;

import java.util.Collection;
import java.util.Map;

/**
 * Compatibility shim for legacy Commons Lang 2 imports backed by Commons Lang 3.
 */
public final class Validate {

    private Validate() {
    }

    public static void isTrue(boolean expression) {
        org.apache.commons.lang3.Validate.isTrue(expression);
    }

    public static void isTrue(boolean expression, String message, Object... values) {
        org.apache.commons.lang3.Validate.isTrue(expression, message, values);
    }

    public static <T> T notNull(T object) {
        return org.apache.commons.lang3.Validate.notNull(object);
    }

    public static <T> T notNull(T object, String message, Object... values) {
        return org.apache.commons.lang3.Validate.notNull(object, message, values);
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... values) {
        return org.apache.commons.lang3.Validate.notEmpty(array, message, values);
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... values) {
        return org.apache.commons.lang3.Validate.notEmpty(collection, message, values);
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... values) {
        return org.apache.commons.lang3.Validate.notEmpty(map, message, values);
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
        return org.apache.commons.lang3.Validate.notEmpty(chars, message, values);
    }

    public static <T> T[] noNullElements(T[] array, String message, Object... values) {
        return org.apache.commons.lang3.Validate.noNullElements(array, message, values);
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... values) {
        return org.apache.commons.lang3.Validate.noNullElements(iterable, message, values);
    }
}
