package dev.drake.infinitylib;

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
        assertEquals("dev.drake.infinitylib", InfinityLib.PACKAGE);
    }

    @Test
    void testAddonPackage() {
        assertEquals("com.github.drakescraft_labs", InfinityLib.ADDON_PACKAGE);
    }

}
