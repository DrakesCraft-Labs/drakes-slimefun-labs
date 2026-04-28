package io.github.thebusybiscuit.extragear;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the ExtraGear plugin.
 *
 * @author TheBusyBiscuit
 */
class TestExtraGear {

    @DisplayName("ExtraGear ArmorSet should have valid materials")
    @Test
    void testArmorSetMaterials() {
        for (ArmorSet set : ArmorSet.values()) {
            assertNotNull(set.getHelmet(), "Helmet material should not be null");
            assertNotNull(set.getChestplate(), "Chestplate material should not be null");
            assertNotNull(set.getLeggings(), "Leggings material should not be null");
            assertNotNull(set.getBoots(), "Boots material should not be null");
        }
    }
}
