package me.jasperchasetoq.dyebench.setup;

import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import me.jasperchasetoq.dyebench.DyeBench;
import me.jasperchasetoq.dyebench.DyeBenchItems;
import me.jasperchasetoq.dyebench.implemention.items.machines.TheDyeBench;
import me.jasperchasetoq.dyebench.implemention.items.pigments.DyePigments;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

    public class DyeBenchItemSetup {
        private DyeBenchItemSetup() {
        }

        public static void setup(@Nonnull DyeBench plugin) {


            //machines
            new TheDyeBench(DyeBenchItems.DyeBenchMachines, DyeBenchItems.JC_DYEBENCH, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            SlimefunItems.RAINBOW_WOOL, SlimefunItems.RAINBOW_WOOL, SlimefunItems.RAINBOW_WOOL,
                            SlimefunItems.RAINBOW_WOOL, new ItemStack(Material.LOOM),SlimefunItems.RAINBOW_WOOL,
                            SlimefunItems.RAINBOW_WOOL, SlimefunItems.RAINBOW_WOOL, SlimefunItems.RAINBOW_WOOL,
                    }).register(plugin);
            //pigments
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_WHITE_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.WHITE_DYE), new ItemStack(Material.WHITE_DYE), new ItemStack(Material.WHITE_DYE),
                            new ItemStack(Material.WHITE_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.WHITE_DYE),
                            new ItemStack(Material.WHITE_DYE), new ItemStack(Material.WHITE_DYE), new ItemStack(Material.WHITE_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_ORANGE_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.ORANGE_DYE),
                            new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.ORANGE_DYE),
                            new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.ORANGE_DYE), new ItemStack(Material.ORANGE_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_MAGENTA_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.MAGENTA_DYE), new ItemStack(Material.MAGENTA_DYE), new ItemStack(Material.MAGENTA_DYE),
                            new ItemStack(Material.MAGENTA_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.MAGENTA_DYE),
                            new ItemStack(Material.MAGENTA_DYE), new ItemStack(Material.MAGENTA_DYE), new ItemStack(Material.MAGENTA_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_LIGHT_BLUE_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.LIGHT_BLUE_DYE), new ItemStack(Material.LIGHT_BLUE_DYE), new ItemStack(Material.LIGHT_BLUE_DYE),
                            new ItemStack(Material.LIGHT_BLUE_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.LIGHT_BLUE_DYE),
                            new ItemStack(Material.LIGHT_BLUE_DYE), new ItemStack(Material.LIGHT_BLUE_DYE), new ItemStack(Material.LIGHT_BLUE_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_YELLOW_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.YELLOW_DYE),
                            new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.YELLOW_DYE),
                            new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.YELLOW_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_LIME_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.LIME_DYE), new ItemStack(Material.LIME_DYE), new ItemStack(Material.LIME_DYE),
                            new ItemStack(Material.LIME_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.LIME_DYE),
                            new ItemStack(Material.LIME_DYE), new ItemStack(Material.LIME_DYE), new ItemStack(Material.LIME_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_PINK_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.PINK_DYE), new ItemStack(Material.PINK_DYE), new ItemStack(Material.PINK_DYE),
                            new ItemStack(Material.PINK_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.PINK_DYE),
                            new ItemStack(Material.PINK_DYE), new ItemStack(Material.PINK_DYE), new ItemStack(Material.PINK_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_GRAY_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.GRAY_DYE), new ItemStack(Material.GRAY_DYE), new ItemStack(Material.GRAY_DYE),
                            new ItemStack(Material.GRAY_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.GRAY_DYE),
                            new ItemStack(Material.GRAY_DYE), new ItemStack(Material.GRAY_DYE), new ItemStack(Material.GRAY_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_LIGHT_GRAY_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.LIGHT_GRAY_DYE), new ItemStack(Material.LIGHT_GRAY_DYE), new ItemStack(Material.LIGHT_GRAY_DYE),
                            new ItemStack(Material.LIGHT_GRAY_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.LIGHT_GRAY_DYE),
                            new ItemStack(Material.LIGHT_GRAY_DYE), new ItemStack(Material.LIGHT_GRAY_DYE), new ItemStack(Material.LIGHT_GRAY_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_CYAN_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.CYAN_DYE), new ItemStack(Material.CYAN_DYE), new ItemStack(Material.CYAN_DYE),
                            new ItemStack(Material.CYAN_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.CYAN_DYE),
                            new ItemStack(Material.CYAN_DYE), new ItemStack(Material.CYAN_DYE), new ItemStack(Material.CYAN_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_PURPLE_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.PURPLE_DYE),
                            new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.PURPLE_DYE),
                            new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.PURPLE_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_BLUE_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.BLUE_DYE), new ItemStack(Material.BLUE_DYE), new ItemStack(Material.BLUE_DYE),
                            new ItemStack(Material.BLUE_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.BLUE_DYE),
                            new ItemStack(Material.BLUE_DYE), new ItemStack(Material.BLUE_DYE), new ItemStack(Material.BLUE_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_BROWN_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.BROWN_DYE), new ItemStack(Material.BROWN_DYE), new ItemStack(Material.BROWN_DYE),
                            new ItemStack(Material.BROWN_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.BROWN_DYE),
                            new ItemStack(Material.BROWN_DYE), new ItemStack(Material.BROWN_DYE), new ItemStack(Material.BROWN_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_GREEN_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.GREEN_DYE), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.GREEN_DYE),
                            new ItemStack(Material.GREEN_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.GREEN_DYE),
                            new ItemStack(Material.GREEN_DYE), new ItemStack(Material.GREEN_DYE), new ItemStack(Material.GREEN_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_RED_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.RED_DYE), new ItemStack(Material.RED_DYE), new ItemStack(Material.RED_DYE),
                            new ItemStack(Material.RED_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.RED_DYE),
                            new ItemStack(Material.RED_DYE), new ItemStack(Material.RED_DYE), new ItemStack(Material.RED_DYE),
                    }).register(plugin);
            new DyePigments(DyeBenchItems.DyeBenchPigments, DyeBenchItems.JC_BLACK_PIGMENT, RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[]{
                            new ItemStack(Material.BLACK_DYE), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.BLACK_DYE),
                            new ItemStack(Material.BLACK_DYE), new ItemStack(Material.LOOM), new ItemStack(Material.BLACK_DYE),
                            new ItemStack(Material.BLACK_DYE), new ItemStack(Material.BLACK_DYE), new ItemStack(Material.BLACK_DYE),
                    }).register(plugin);
        }
    }

