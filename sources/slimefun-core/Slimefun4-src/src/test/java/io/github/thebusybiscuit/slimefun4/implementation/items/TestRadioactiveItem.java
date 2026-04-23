package com.github.drakescraft-labs.slimefun4.implementation.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestRadioactiveItem {

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @EnumSource(value = Radioactivity.class)
    @DisplayName("Test radioactive items being radioactive")
    void testWikiPages(Radioactivity radioactivity) {
        ItemGroup itemGroup = TestUtilities.getItemGroup(plugin, "radioactivity_test");
        SlimefunItemStack stack = new SlimefunItemStack("RADIOACTIVE_" + radioactivity.name(), Material.EMERALD, "&4Radioactive!!!", "Imagine dragons");
        RadioactiveItem item = new RadioactiveItem(itemGroup, radioactivity, stack, RecipeType.NULL, new ItemStack[9]);

        Assertions.assertEquals(radioactivity, item.getRadioactivity());
    }

}
