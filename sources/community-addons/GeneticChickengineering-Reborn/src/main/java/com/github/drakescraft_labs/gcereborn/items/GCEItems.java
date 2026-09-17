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
    public static final SlimefunItemStack EXCITATION_CHAMBER_4;
    public static final SlimefunItemStack PRIVATE_COOP;
    public static final SlimefunItemStack RESTORATION_CHAMBER;
    public static final SlimefunItemStack GROWTH_CHAMBER;

    // === NUEVA MAQUINARIA BIOTECNOLÓGICA ===
    public static final SlimefunItemStack BIO_CLONING_VAT;
    public static final SlimefunItemStack MUTAGENIC_SPLICER;
    public static final SlimefunItemStack INDUSTRIAL_ROOST;

    // === NUEVOS CONSUMIBLES, PIENSOS Y CATALIZADORES ===
    public static final SlimefunItemStack MUTAGENIC_SERUM;
    public static final SlimefunItemStack NUTRIENT_GEL;
    public static final SlimefunItemStack RAPID_GROWTH_FEED;
    public static final SlimefunItemStack FORTIFIED_VITA_FEED;
    public static final SlimefunItemStack EXCITATION_CATALYST;

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
        EXCITATION_CHAMBER_4 = GeneticChickengineering.getLocalization().getItem(
            "EXCITATION_CHAMBER_4",
            Material.RESPAWN_ANCHOR,
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(250)
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

        BIO_CLONING_VAT = GeneticChickengineering.getLocalization().getItem(
            "BIO_CLONING_VAT",
            Material.BREWING_STAND,
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(120)
        );
        MUTAGENIC_SPLICER = GeneticChickengineering.getLocalization().getItem(
            "MUTAGENIC_SPLICER",
            Material.ENCHANTING_TABLE,
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(150)
        );
        INDUSTRIAL_ROOST = GeneticChickengineering.getLocalization().getItem(
            "INDUSTRIAL_ROOST",
            Material.BARREL,
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.powerPerSecond(30)
        );

        MUTAGENIC_SERUM = GeneticChickengineering.getLocalization().getItem(
            "MUTAGENIC_SERUM",
            Material.DRAGON_BREATH,
            "",
            "&7Reactivo biológico altamente reactivo.",
            "&7Utilizado en el &eEmpalmador Mutagénico&7."
        );
        NUTRIENT_GEL = GeneticChickengineering.getLocalization().getItem(
            "NUTRIENT_GEL",
            Material.SLIME_BALL,
            "",
            "&7Sustrato rico en nutrientes para mitosis acelerada.",
            "&7Utilizado en la &eCuba de Bioclonación&7."
        );
        RAPID_GROWTH_FEED = GeneticChickengineering.getLocalization().getItem(
            "RAPID_GROWTH_FEED",
            Material.WHEAT_SEEDS,
            "",
            "&7Pienso enriquecido con hormonas de crecimiento.",
            "&eReduce el tiempo de cría a solo 10 segundos&7."
        );
        FORTIFIED_VITA_FEED = GeneticChickengineering.getLocalization().getItem(
            "FORTIFIED_VITA_FEED",
            Material.PUMPKIN_SEEDS,
            "",
            "&7Pienso fortificante vitaminado.",
            "&aOtorga inmunidad al daño por sobreesfuerzo&7."
        );
        EXCITATION_CATALYST = GeneticChickengineering.getLocalization().getItem(
            "EXCITATION_CATALYST",
            Material.GLOWSTONE_DUST,
            "",
            "&7Catalizador iónico de resonancia.",
            "&6+35% probabilidad de obtener cosecha doble&7."
        );
        // @formatter:on
    }
}
