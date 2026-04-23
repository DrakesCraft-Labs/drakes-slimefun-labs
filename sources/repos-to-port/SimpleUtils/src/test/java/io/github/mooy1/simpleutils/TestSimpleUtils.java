package com.github.drakescraft-labs.simpleutils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.setup.SlimefunItemSetup;

class TestSimpleUtils {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        SlimefunItemSetup.setup(MockBukkit.load(Slimefun.class));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testLoad() {
        MockBukkit.load(SimpleUtils.class);
    }

}
