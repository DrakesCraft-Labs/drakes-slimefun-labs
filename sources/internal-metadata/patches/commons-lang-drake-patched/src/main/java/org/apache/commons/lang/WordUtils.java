package org.apache.commons.lang;

/**
 * Small legacy facade for the WordUtils methods still used by old addons.
 */
public final class WordUtils {

    private WordUtils() {
    }

    public static String capitalize(String str) {
        return org.apache.commons.lang3.text.WordUtils.capitalize(str);
    }

    public static String capitalizeFully(String str) {
        return org.apache.commons.lang3.text.WordUtils.capitalizeFully(str);
    }

    public static String wrap(String str, int wrapLength) {
        return org.apache.commons.lang3.text.WordUtils.wrap(str, wrapLength);
    }
}
