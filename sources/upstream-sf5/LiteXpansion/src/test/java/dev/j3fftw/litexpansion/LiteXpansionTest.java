package dev.j3fftw.litexpansion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Smoke tests for LiteXpansion.
 *
 * @author LiteXpansion contributors
 */
class LiteXpansionTest {

    @Test
    @DisplayName("LiteXpansion class should be loadable")
    void testClassLoads() {
        assertNotNull(LiteXpansion.class);
    }

}
