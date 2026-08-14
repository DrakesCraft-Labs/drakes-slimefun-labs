package me.sfiguz7.transcendence.implementation.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class DaxiWorldPolicyTest {

    private static final List<String> BLOCKED_WORLDS = List.of(
        "clasico", "clasico_nether", "clasico_the_end", "bskyblock_world", "oneblock_world"
    );
    private static final List<String> BLOCKED_PREFIXES = List.of("bskyblock_", "oneblock_");

    @Test
    void blocksEveryClassicDimension() {
        assertFalse(DaxiWorldPolicy.isAllowed("clasico", BLOCKED_WORLDS, BLOCKED_PREFIXES));
        assertFalse(DaxiWorldPolicy.isAllowed("clasico_nether", BLOCKED_WORLDS, BLOCKED_PREFIXES));
        assertFalse(DaxiWorldPolicy.isAllowed("CLASICO_THE_END", BLOCKED_WORLDS, BLOCKED_PREFIXES));
    }

    @Test
    void blocksGeneratedIslandDimensionsByPrefix() {
        assertFalse(DaxiWorldPolicy.isAllowed("bskyblock_world_nether", BLOCKED_WORLDS, BLOCKED_PREFIXES));
        assertFalse(DaxiWorldPolicy.isAllowed("oneblock_event", BLOCKED_WORLDS, BLOCKED_PREFIXES));
    }

    @Test
    void allowsSlimefunSurvivalWorlds() {
        assertTrue(DaxiWorldPolicy.isAllowed("world", BLOCKED_WORLDS, BLOCKED_PREFIXES));
        assertTrue(DaxiWorldPolicy.isAllowed("world_nether", BLOCKED_WORLDS, BLOCKED_PREFIXES));
    }
}
