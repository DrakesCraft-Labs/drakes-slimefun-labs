package io.github.thebusybiscuit.exoticgarden;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Smoke tests for ExoticGarden.
 *
 * @author ExoticGarden contributors
 */
class ExoticGardenTest {

    @Test
    @DisplayName("ExoticGarden class should be loadable")
    void testClassLoads() {
        assertNotNull(ExoticGarden.class);
    }

    @Test
    @DisplayName("PlantType enum should have expected values")
    void testPlantTypeEnum() {
        assertNotNull(PlantType.BUSH);
        assertNotNull(PlantType.FRUIT);
        assertNotNull(PlantType.DOUBLE_PLANT);
        assertNotNull(PlantType.ORE_PLANT);
    }

}
