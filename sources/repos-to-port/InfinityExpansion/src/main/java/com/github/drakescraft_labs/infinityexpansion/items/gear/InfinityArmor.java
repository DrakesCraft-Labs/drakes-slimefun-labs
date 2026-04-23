package com.github.drakescraft_labs.infinityexpansion.items.gear;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.drakescraft_labs.infinityexpansion.InfinityExpansion;
import com.github.drakescraft_labs.infinityexpansion.categories.Groups;
import com.github.drakescraft_labs.infinityexpansion.items.blocks.InfinityWorkbench;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.attributes.ProtectionType;
import com.github.drakescraft_labs.slimefun4.core.attributes.ProtectiveArmor;
import com.github.drakescraft_labs.slimefun4.core.attributes.Soulbound;
import com.github.drakescraft_labs.slimefun4.implementation.items.armor.SlimefunArmorPiece;

/**
 * armor
 *
 * @author Mooy1
 */
public final class InfinityArmor extends SlimefunArmorPiece implements ProtectiveArmor, Soulbound, NotPlaceable {

    private static final NamespacedKey KEY = InfinityExpansion.createKey("infinity_armor");

    public InfinityArmor(SlimefunItemStack item, PotionEffect[] effects, ItemStack[] recipe) {
        super(Groups.INFINITY_CHEAT, item, InfinityWorkbench.TYPE, recipe, effects);
    }

    @Nonnull
    @Override
    public ProtectionType[] getProtectionTypes() {
        return new ProtectionType[] {
                ProtectionType.BEES, ProtectionType.RADIATION, ProtectionType.FLYING_INTO_WALL
        };
    }

    @Override
    public boolean isFullSetRequired() {
        return false;
    }

    @Nonnull
    @Override
    public NamespacedKey getArmorSetId() {
        return KEY;
    }

}
