package com.github.drakescraft_labs.gcereborn.setup;

import com.github.drakescraft_labs.slimefun4.api.researches.Research;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.utils.Keys;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Researches {

    public static final Research MAIN = new Research(
        Keys.get("genetic_chickengineering"),
        29841,
        "Defying Nature",
        13
    );

    public static final Research BIOTECHNOLOGY = new Research(
        Keys.get("genetic_chickengineering_biotech"),
        29842,
        "Avian Biotechnology",
        24
    );

    public static void setup() {
        MAIN.addItems(
            GCEItems.POCKET_CHICKEN,
            GCEItems.CHICKEN_NET,
            GCEItems.WATER_EGG,
            GCEItems.LAVA_EGG,
            GCEItems.GENETIC_SEQUENCER,
            GCEItems.EXCITATION_CHAMBER,
            GCEItems.EXCITATION_CHAMBER_2,
            GCEItems.EXCITATION_CHAMBER_3,
            GCEItems.PRIVATE_COOP
        );
        if (GeneticChickengineering.getConfigService().isPainEnabled()) {
            MAIN.addItems(GCEItems.RESTORATION_CHAMBER);
        }
        MAIN.register();

        BIOTECHNOLOGY.addItems(
            GCEItems.BIO_CLONING_VAT,
            GCEItems.MUTAGENIC_SPLICER,
            GCEItems.INDUSTRIAL_ROOST,
            GCEItems.EXCITATION_CHAMBER_4,
            GCEItems.MUTAGENIC_SERUM,
            GCEItems.NUTRIENT_GEL,
            GCEItems.RAPID_GROWTH_FEED,
            GCEItems.FORTIFIED_VITA_FEED,
            GCEItems.EXCITATION_CATALYST
        );
        BIOTECHNOLOGY.register();
    }
}
