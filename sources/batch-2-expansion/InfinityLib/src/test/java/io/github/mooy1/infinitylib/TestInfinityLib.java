package com.github.drakescraft-labs.infinitylib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestInfinityLib {

    @Test
    void testVersion() {
        assertNotEquals("${project.version}", InfinityLib.VERSION);
    }

    @Test
    void testPackage() {
        assertEquals("com.github.drakescraft-labs.infinitylib", InfinityLib.PACKAGE);
    }

    @Test
    void testAddonPackage() {
        assertEquals("com.github.drakescraft-labs", InfinityLib.ADDON_PACKAGE);
    }

}
