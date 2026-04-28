package io.github.mooy1.infinityexpansion.items.blocks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public final class Blocks {

    private Blocks() {}

    public static final SlimefunItemStack STRAINER_BASE = new SlimefunItemStack(
            "STRAINER_BASE",
            Material.SANDSTONE_WALL,
            "&7Strainer Base"
    );
    public static final SlimefunItemStack ADVANCED_ANVIL = new SlimefunItemStack(
            "ADVANCED_ANVIL",
            Material.SMITHING_TABLE,
            "&cAdvanced Anvil",
            "&7Combines tools and gear enchants and sometimes upgrades them",
            "&bWorks with Slimefun items",
            "",
            MachineLore.energy(100000) + "per use"
    );
    public static final SlimefunItemStack INFINITY_FORGE = new SlimefunItemStack(
            "INFINITY_FORGE",
            Material.RESPAWN_ANCHOR,
            "&6Infinity Workbench",
            "&7Used to craft infinity items",
            "",
            MachineLore.energy(10000000) + "per item"
    );

    public static void setup(InfinityExpansion plugin) {
        new StrainerBase(Groups.BASIC_MACHINES, STRAINER_BASE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.STICK), new ItemStack(Material.STRING), new ItemStack(Material.STICK),
                new ItemStack(Material.STICK), new ItemStack(Material.STRING), new ItemStack(Material.STICK),
                Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(),
        }, 48).register(plugin);
        new AdvancedAnvil(Groups.MAIN_MATERIALS, ADVANCED_ANVIL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), new ItemStack(Material.ANVIL), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 100000).register(plugin);
        new InfinityWorkbench(Groups.MAIN_MATERIALS, INFINITY_FORGE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item(),
                SlimefunItems.ENERGIZED_CAPACITOR.item(), new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.ENERGIZED_CAPACITOR.item(),
                Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item()
        }, 10000000).register(plugin);
    }

}
