package com.github.drakescraft_labs.slimefun4.test.mocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.ProtectionType;
import com.github.drakescraft_labs.slimefun4.core.attributes.ProtectiveArmor;
import com.github.drakescraft_labs.slimefun4.implementation.items.armor.SlimefunArmorPiece;

public class MockHazmatSuit extends SlimefunArmorPiece implements ProtectiveArmor {

    @ParametersAreNonnullByDefault
    public MockHazmatSuit(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[9], new PotionEffect[0]);
    }

    @Override
    public ProtectionType[] getProtectionTypes() {
        return new ProtectionType[] { ProtectionType.RADIATION };
    }

    @Override
    public boolean isFullSetRequired() {
        return false;
    }

    @Override
    public NamespacedKey getArmorSetId() {
        return new NamespacedKey(getAddon().getJavaPlugin(), "mock_hazmat");
    }

}
