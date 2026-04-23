package com.github.drakescraft_labs.infinityexpansion.items.quarries;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.infinityexpansion.categories.Groups;
import com.github.drakescraft_labs.infinityexpansion.items.materials.Materials;
import com.github.drakescraft_labs.infinitylib.common.StackUtils;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import dev.drake.dough.items.ItemUtils;

public final class Oscillator extends SlimefunItem {

    private static final Map<String, Oscillator> OSCILLATORS = new HashMap<>();

    public final double chance;

    @Nullable
    public static Oscillator getOscillator(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        return OSCILLATORS.get(StackUtils.getId(item));
    }

    @Nonnull
    public static SlimefunItemStack create(Material material, double chance) {
        return new SlimefunItemStack(
                "QUARRY_OSCILLATOR_" + material.name(),
                material,
                "&b" + ItemUtils.getItemName(new ItemStack(material)) + " Oscillator",
                "&7Place in a quarry to give it",
                "&7a " + (chance * 100) + "% chance of mining this material"
        );
    }

    public Oscillator(SlimefunItemStack item, double chance) {
        super(Groups.MAIN_MATERIALS, item, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE, SlimefunItems.BLISTERING_INGOT_3, Materials.MACHINE_PLATE,
                SlimefunItems.BLISTERING_INGOT_3, new ItemStack(item.getType()), SlimefunItems.BLISTERING_INGOT_3,
                Materials.MACHINE_PLATE, SlimefunItems.BLISTERING_INGOT_3, Materials.MACHINE_PLATE
        });
        OSCILLATORS.put(getId(), this);
        this.chance = chance;
    }

}
