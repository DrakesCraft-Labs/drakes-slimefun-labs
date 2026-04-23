package com.github.drakescraft-labs.slimechem.test;

import org.junit.jupiter.api.Assertions;

public class Asserts extends Assertions {

    public static void assertThrows(Runnable r, Class<? extends Throwable> throwable) {
        try {
            r.run();
            fail(() -> "Exception " + throwable.getName() + " was not thrown!");
        } catch (Throwable e) {
            if (e.getClass() != throwable) {
                fail(() -> "Expected exception " + throwable.getName() + ", got " + e.getClass().getName());
            }
        }
    }
}
