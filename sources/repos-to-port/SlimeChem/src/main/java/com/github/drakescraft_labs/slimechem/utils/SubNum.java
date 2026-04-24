package com.github.drakescraft_labs.slimechem.utils;

import javax.annotation.Nonnull;

/**
 * Subscript numbers and getter
 * 
 * @author TheBusyBiscuit
 * @author Mooy1
 * TODO: make better 
 */
public final class SubNum {

    public static final char ZERO_STARTING_POINT = '\u2080';

    private SubNum() {}

    @Nonnull
    public static String fromInt(int i) {
        if (i <= 1) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        int num = i;

        while (num > 0) {
            // Use zero char as a base and then add x onto it.
            builder.append((char) (ZERO_STARTING_POINT + (num % 10)));
            num /= 10;
        }

        return builder.reverse().toString();
    }
}
