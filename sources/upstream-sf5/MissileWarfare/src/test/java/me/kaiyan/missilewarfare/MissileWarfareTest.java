package me.kaiyan.missilewarfare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Smoke tests for MissileWarfare.
 *
 * @author MissileWarfare contributors
 */
class MissileWarfareTest {

    @Test
    @DisplayName("MissileWarfare class should be loadable")
    void testClassLoads() {
        assertNotNull(MissileWarfare.class);
    }

}
