package com.github.drakescraft_labs.gcereborn.items;

import org.bukkit.Material;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineTier;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineType;
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class GCEItems {

    public static final SlimefunItemStack POCKET_CHICKEN;
    public static final SlimefunItemStack CHICKEN_NET;
    public static final SlimefunItemStack WATER_EGG;
    public static final SlimefunItemStack LAVA_EGG;
    public static final SlimefunItemStack GENETIC_SEQUENCER;
    public static final SlimefunItemStack EXCITATION_CHAMBER;
    public static final SlimefunItemStack EXCITATION_CHAMBER_2;
    public static final SlimefunItemStack EXCITATION_CHAMBER_3;
    public static final SlimefunItemStack PRIVATE_COOP;
    public static final SlimefunItemStack RESTORATION_CHAMBER;
    public static final SlimefunItemStack GROWTH_CHAMBER;

    private static final String LORE_RIGHT_CLICK_TO_USE;

    static {
        LORE_RIGHT_CLICK_TO_USE = GeneticChickengineering.getLocalization().getString("lores.right-click-to-use");

        // @formatter:off
        POCKET_CHICKEN = GeneticChickengineering.getLocalization().getItem(
            "POCKET_CHICKEN",
            "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893"
        );
        CHICKEN_NET = GeneticChickengineering.getLocalization().getItem(
            "CHICKEN_NET",
            Material.COBWEB,
            "",
            LORE_RIGHT_CLICK_TO_USE
        );
        WATER_EGG = GeneticChickengineering.getLocalization().getItem(
            "WATER_EGG",
            Material.TURTLE_SPAWN_EGG,
            "",
            LORE_RIGHT_CLICK_TO_USE
        );
        LAVA_EGG = GeneticChickengineering.getLocalization().getItem(
            "LAVA_EGG",
            Material.STRIDER_SPAWN_EGG,
            "",
            LORE_RIGHT_CLICK_TO_USE
        );
        GENETIC_SEQUENCER = GeneticChickengineering.getLocalization().getItem(
            "GENETIC_SEQUENCER",
            Material.SMOKER,
            "",
            LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(6)
        );
        EXCITATION_CHAMBER = GeneticChickengineering.getLocalization().getItem(
            "EXCITATION_CHAMBER",
            Material.BLAST_FURNACE,
            "",
            LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(10)
        );
        EXCITATION_CHAMBER_2 = GeneticChickengineering.getLocalization().getItem(
            "EXCITATION_CHAMBER_2",
            Material.BLAST_FURNACE,
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(15)
        );
        EXCITATION_CHAMBER_3 = GeneticChickengineering.getLocalization().getItem(
            "EXCITATION_CHAMBER_3",
            Material.BLAST_FURNACE,
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(100)
        );
        PRIVATE_COOP = GeneticChickengineering.getLocalization().getItem(
            "PRIVATE_COOP",
            Material.BEEHIVE,
            "",
            LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(2)
        );
        RESTORATION_CHAMBER = GeneticChickengineering.getLocalization().getItem(
            "RESTORATION_CHAMBER",
            Material.PINK_SHULKER_BOX,
            "",
            LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(4)
        );
        GROWTH_CHAMBER = GeneticChickengineering.getLocalization().getItem(
            "GROWTH_CHAMBER",
            Material.GREEN_SHULKER_BOX,
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(20)
        );
        // @formatter:on
    }
}
