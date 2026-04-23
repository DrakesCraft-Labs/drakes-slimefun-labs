package com.github.drakescraft_labs.slimefun4.implementation.items.backpacks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.test.TestUtilities;
import com.github.drakescraft_labs.slimefun4.test.presets.SlimefunItemTest;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestEnderBackpack implements SlimefunItemTest<EnderBackpack> {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Override
    public EnderBackpack registerSlimefunItem(Slimefun plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.ENDER_CHEST, "&5Test Ender Backpack");
        EnderBackpack backpack = new EnderBackpack(TestUtilities.getItemGroup(plugin, "ender_backpack"), item, RecipeType.NULL, new ItemStack[9]);
        backpack.register(plugin);
        return backpack;
    }

    @Test
    @DisplayName("Test Ender Backpack opening Enderchest")
    void testRightClickBehaviour() {
        Player player = server.addPlayer();
        EnderBackpack backpack = registerSlimefunItem(plugin, "TEST_ENDER_BACKPACK");

        simulateRightClick(player, backpack);

        // We expect the Enderchest to be open now
        Assertions.assertEquals(player.getEnderChest(), player.getOpenInventory().getTopInventory());
    }

}
