package com.github.drakescraft_labs.slimechem.utils;

import com.github.drakescraft_labs.slimechem.SlimeChem;
import lombok.Data;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class StringUtil {

    private StringUtil() {}

    @Data
    public static class NumberAndString {
        private final int number;
        private final String string;
    }

    @Nonnull
    public static String enumNameToTitleCaseString(@Nonnull String enumName) {
        String[] words = enumName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    sb.append(word.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    @Nonnull
    public static String getResourceAsString(@Nonnull String resource) throws IOException {
        InputStream stream = SlimeChem.class.getResourceAsStream("/" + resource);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    public static NumberAndString splitString(String s) {
        StringBuilder number = new StringBuilder();
        StringBuilder rest = new StringBuilder();

        for (char character : s.toCharArray()) {
            if (Character.isDigit(character)) {
                number.append(character);
            } else {
                rest.append(character);
            }
        }

        return new NumberAndString(Integer.parseInt(number.toString()), rest.toString());
    }
}
