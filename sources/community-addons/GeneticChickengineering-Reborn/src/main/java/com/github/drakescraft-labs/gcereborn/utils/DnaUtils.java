package com.github.drakescraft-labs.gcereborn.utils;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.github.drakescraft-labs.gcereborn.core.genetics.DNA;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class DnaUtils {

    @Nonnull
    public static List<String> getPossibleDNA() {
        List<String> dnaList = new LinkedList<>();

        for (int b = 2; b >= 0; b--) {
            for (int c = 2; c >= 0; c--) {
                for (int d = 2; d >= 0; d--) {
                    for (int f = 2; f >= 0; f--) {
                        for (int s = 2; s >= 0; s--) {
                            for (int w = 2; w >= 0; w--) {
                                int[] state = new int[] {b + b / 2, c + c / 2, d + d / 2, f + f / 2, s + s / 2, w + w / 2, 1};
                                dnaList.add(new DNA(state).toString());
                            }
                        }
                    }
                }
            }
        }

        return dnaList;
    }
}
