package com.github.drakescraft_labs.gcereborn.setup;

import org.bukkit.Material;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.utils.Heads;
import com.github.drakescraft_labs.gcereborn.utils.Keys;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Groups {

    public static final ItemGroup MAIN = new ItemGroup(
        Keys.get("genetic_chickengineering"),
        GeneticChickengineering.getLocalization().getItemGroupItem(
            "ICON",
            Heads.CHICKEN.getTexture()
        )
    );

    public static final ItemGroup DICTIONARY = new ItemGroup(
        Keys.get("genetic_chickengineering_chickens"),
        GeneticChickengineering.getLocalization().getItemGroupItem(
            "DIRECTORY_ICON",
            Material.BLAST_FURNACE
        )
    );
}
