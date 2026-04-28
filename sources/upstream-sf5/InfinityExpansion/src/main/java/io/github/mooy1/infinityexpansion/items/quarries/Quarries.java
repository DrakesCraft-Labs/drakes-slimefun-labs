package io.github.mooy1.infinityexpansion.items.quarries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.SlimefunExtension;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.gear.Gear;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public final class Quarries {

    private Quarries() {}

    public static final SlimefunItemStack BASIC_QUARRY = new SlimefunItemStack(
            "BASIC_QUARRY",
            Material.CHISELED_SANDSTONE,
            "&9Basic Quarry",
            "&7Automatically mines overworld ores",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(300)
    );
    public static final SlimefunItemStack ADVANCED_QUARRY = new SlimefunItemStack(
            "ADVANCED_QUARRY",
            Material.CHISELED_RED_SANDSTONE,
            "&cAdvanced Quarry",
            "&7Automatically mines overworld and nether ores",
            "",
            MachineLore.speed(2),
            MachineLore.energyPerSecond(900)
    );
    public static final SlimefunItemStack VOID_QUARRY = new SlimefunItemStack(
            "VOID_QUARRY",
            Material.CHISELED_NETHER_BRICKS,
            "&8Void Quarry",
            "&7Automatically mines overworld and nether ores",
            "",
            MachineLore.speed(6),
            MachineLore.energyPerSecond(3600)
    );
    public static final SlimefunItemStack INFINITY_QUARRY = new SlimefunItemStack(
            "INFINITY_QUARRY",
            Material.CHISELED_POLISHED_BLACKSTONE,
            "&bInfinity Quarry",
            "&7Automatically mines overworld and nether ores",
            "",
            MachineLore.speed(64),
            MachineLore.energyPerSecond(36000)
    );
    public static final double DIAMOND_CHANCE = getOscillatorChance("diamond");
    public static final double REDSTONE_CHANCE = getOscillatorChance("redstone");
    public static final double LAPIS_CHANCE = getOscillatorChance("lapis");
    public static final double EMERALD_CHANCE = getOscillatorChance("emerald");
    public static final double QUARTZ_CHANCE = getOscillatorChance("quartz");
    public static final SlimefunItemStack DIAMOND_OSCILLATOR = Oscillator.create(Material.DIAMOND, DIAMOND_CHANCE);
    public static final SlimefunItemStack REDSTONE_OSCILLATOR = Oscillator.create(Material.REDSTONE, REDSTONE_CHANCE);
    public static final SlimefunItemStack LAPIS_OSCILLATOR = Oscillator.create(Material.LAPIS_LAZULI, LAPIS_CHANCE);
    public static final SlimefunItemStack QUARTZ_OSCILLATOR = Oscillator.create(Material.QUARTZ, QUARTZ_CHANCE);
    public static final SlimefunItemStack EMERALD_OSCILLATOR = Oscillator.create(Material.EMERALD, EMERALD_CHANCE);

    private static double getOscillatorChance(String type) {
        return InfinityExpansion.config().getDouble("quarry-options.oscillators." + type, 0, 1);
    }

    public static void setup(InfinityExpansion plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("quarry-options.resources");
        Objects.requireNonNull(section);
        List<Material> outputs = new ArrayList<>();

        boolean coal = section.getBoolean("coal");

        if (coal) {
            outputs.add(Material.COAL);
            outputs.add(Material.COAL);
        }

        if (section.getBoolean("iron")) {
            outputs.add(Material.IRON_INGOT);
        }

        if (section.getBoolean("gold")) {
            outputs.add(Material.GOLD_INGOT);
        }

        if (section.getBoolean("copper")) {
            outputs.add(Material.COPPER_INGOT);
            outputs.add(Material.COPPER_INGOT);
        }

        if (section.getBoolean("redstone")) {
            new Oscillator(REDSTONE_OSCILLATOR, REDSTONE_CHANCE).register(plugin);
            outputs.add(Material.REDSTONE);
        }

        if (section.getBoolean("lapis")) {
            new Oscillator(LAPIS_OSCILLATOR, LAPIS_CHANCE).register(plugin);
            outputs.add(Material.LAPIS_LAZULI);
        }

        if (section.getBoolean("emerald")) {
            new Oscillator(EMERALD_OSCILLATOR, EMERALD_CHANCE).register(plugin);
            outputs.add(Material.EMERALD);
        }

        if (section.getBoolean("diamond")) {
            new Oscillator(DIAMOND_OSCILLATOR, DIAMOND_CHANCE).register(plugin);
            outputs.add(Material.DIAMOND);
        }

        new Quarry(Groups.ADVANCED_MACHINES, BASIC_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item(), Materials.MAGSTEEL_PLATE.item(),
                new ItemStack(Material.IRON_PICKAXE), SlimefunItems.GEO_MINER.item(), new ItemStack(Material.IRON_PICKAXE),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 1, 6, outputs.toArray(new Material[0])).energyPerTick(300).register(plugin);

        if (section.getBoolean("quartz")) {
            new Oscillator(QUARTZ_OSCILLATOR, QUARTZ_CHANCE).register(plugin);

            outputs.add(Material.QUARTZ);
        }

        if (section.getBoolean("netherite")) {
            outputs.add(Material.NETHERITE_INGOT);
        }

        if (section.getBoolean("netherrack")) {
            outputs.add(Material.NETHERRACK);
            outputs.add(Material.NETHERRACK);
        }

        new Quarry(Groups.ADVANCED_MACHINES, ADVANCED_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), Materials.MACHINE_PLATE.item(),
                new ItemStack(Material.DIAMOND_PICKAXE), BASIC_QUARRY.item(), new ItemStack(Material.DIAMOND_PICKAXE),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 2, 4, outputs.toArray(new Material[0])).energyPerTick(900).register(plugin);

        if (coal) {
            outputs.add(Material.COAL);
        }

        new Quarry(Groups.ADVANCED_MACHINES, VOID_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.VOID_INGOT.item(), SlimefunExtension.VOID_CAPACITOR.item(), Materials.VOID_INGOT.item(),
                new ItemStack(Material.NETHERITE_PICKAXE), ADVANCED_QUARRY.item(), new ItemStack(Material.NETHERITE_PICKAXE),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 6, 2, outputs.toArray(new Material[0])).energyPerTick(3600).register(plugin);

        if (coal) {
            outputs.add(Material.COAL);
        }

        new Quarry(Groups.INFINITY_CHEAT, INFINITY_QUARRY, InfinityWorkbench.TYPE, new ItemStack[] {
                null, Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), null,
                Materials.MACHINE_PLATE.item(), Gear.PICKAXE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Gear.PICKAXE.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), VOID_QUARRY.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CORE.item(), VOID_QUARRY.item(), Materials.MACHINE_PLATE.item(),
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.VOID_INGOT.item()
        }, 64, 1, outputs.toArray(new Material[0])).energyPerTick(36000).register(plugin);
    }

}
