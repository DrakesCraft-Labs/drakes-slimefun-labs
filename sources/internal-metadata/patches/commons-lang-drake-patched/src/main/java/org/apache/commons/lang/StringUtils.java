package org.apache.commons.lang;

/**
 * Minimal StringUtils for Patched ClassUtils
 */
public class StringUtils {
    public static String deleteWhitespace(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }
}
