package me.vaan.mapjammer.implementation;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;

public class Setup {

    public Setup(@Nonnull SlimefunAddon plugin, @NonNull ItemGroup group) {

        new Jammer(group, Items.JAMMER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        null, SlimefunItems.GPS_TRANSMITTER, null,
                        SlimefunItems.STEEL_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.STEEL_INGOT,
                        SlimefunItems.COPPER_WIRE, SlimefunItems.STEEL_INGOT, SlimefunItems.COPPER_WIRE
                }
                ,120)
                .register(plugin);

        new Jammer(group, Items.JAMMER_2, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        null, SlimefunItems.GPS_TRANSMITTER_2, null,
                        SlimefunItems.DAMASCUS_STEEL_INGOT, Items.JAMMER, SlimefunItems.DAMASCUS_STEEL_INGOT,
                        SlimefunItems.COPPER_WIRE, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.COPPER_WIRE
                }
                ,360)
                .register(plugin);

        new Jammer(group, Items.JAMMER_3, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        null, SlimefunItems.GPS_TRANSMITTER_3, null,
                        SlimefunItems.HARDENED_METAL_INGOT, Items.JAMMER_2, SlimefunItems.HARDENED_METAL_INGOT,
                        SlimefunItems.COPPER_WIRE, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.COPPER_WIRE
                }
                ,1440)
                .register(plugin);
    }
}
