package com.github.drakescraft-labs.gcereborn.setup;

import com.github.drakescraft-labs.slimefun4.api.researches.Research;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft-labs.gcereborn.items.GCEItems;
import com.github.drakescraft-labs.gcereborn.utils.Keys;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Researches {

    public static final Research MAIN = new Research(
        Keys.get("genetic_chickengineering"),
        29841,
        "Defying Nature",
        13
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
    }
}
