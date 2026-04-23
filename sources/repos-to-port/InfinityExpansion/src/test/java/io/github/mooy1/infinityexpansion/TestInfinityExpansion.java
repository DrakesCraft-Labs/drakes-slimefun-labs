package com.github.drakescraft_labs.infinityexpansion;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

class TestInfinityExpansion {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testLoad() {
        MockBukkit.load(InfinityExpansion.class);
    }

}
