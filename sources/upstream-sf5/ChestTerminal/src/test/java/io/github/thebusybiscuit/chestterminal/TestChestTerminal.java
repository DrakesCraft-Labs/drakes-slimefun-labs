package io.github.thebusybiscuit.chestterminal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the ChestTerminal plugin.
 *
 * @author TheBusyBiscuit
 */
class TestChestTerminal {

    @DisplayName("ChestTerminal main class should be loadable")
    @Test
    void testMainClassExists() {
        assertNotNull(ChestTerminal.class.getName());
    }
}
