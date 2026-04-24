package org.apache.commons.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Patched Edition for DrakesLab Reactor</p>
 * <p>Fixes CVE-2025-48924 by replacing recursive getClass with iterative logic.</p>
 */
public class ClassUtils {

    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

    private static final Map<String, String> abbreviationMap = new HashMap<String, String>();
    private static final Map<String, String> reverseAbbreviationMap = new HashMap<String, String>();

    static {
        addAbbreviation("int", "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation("float", "F");
        addAbbreviation("long", "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation("double", "D");
        addAbbreviation("char", "C");
    }

    private static void addAbbreviation(String primitive, String abbreviation) {
        abbreviationMap.put(primitive, abbreviation);
        reverseAbbreviationMap.put(abbreviation, primitive);
    }

    public static Class getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        try {
            Class clazz;
            if (abbreviationMap.containsKey(className)) {
                String clsName = "[" + abbreviationMap.get(className);
                clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
            } else {
                clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return clazz;
        } catch (ClassNotFoundException ex) {
            // ITERATIVE FIX for CVE-2025-48924
            int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
            if (lastDotIndex != -1) {
                String nextClassName = className;
                while (lastDotIndex != -1) {
                    nextClassName = nextClassName.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR_CHAR + nextClassName.substring(lastDotIndex + 1);
                    try {
                        return Class.forName(nextClassName, initialize, classLoader);
                    } catch (ClassNotFoundException cnfe) {
                        lastDotIndex = nextClassName.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
                    }
                }
            }
            throw ex;
        }
    }

    public static Class getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    public static Class getClass(String className) throws ClassNotFoundException {
        return getClass(Thread.currentThread().getContextClassLoader(), className, true);
    }

    public static Class getClass(String className, boolean initialize) throws ClassNotFoundException {
        return getClass(Thread.currentThread().getContextClassLoader(), className, initialize);
    }

    private static String toCanonicalName(String className) {
        className = StringUtils.deleteWhitespace(className);
        if (className == null) {
            throw new NullPointerException("className must not be null.");
        } else if (className.endsWith("[]")) {
            StringBuilder classNameBuffer = new StringBuilder();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            String abbreviation = abbreviationMap.get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        }
        return className;
    }
    
    // Add missing methods if needed by Slimefun, but getClass is the target.
}
