package io.github.thebusybiscuit.slimefunluckyblocks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the SlimefunLuckyBlocks plugin.
 *
 * @author TheBusyBiscuit
 */
class TestLuckyBlocks {

    @DisplayName("SlimefunLuckyBlocks main class should be loadable")
    @Test
    void testMainClassExists() {
        assertNotNull(SlimefunLuckyBlocks.class.getName(), "Main class should exist");
    }
}
