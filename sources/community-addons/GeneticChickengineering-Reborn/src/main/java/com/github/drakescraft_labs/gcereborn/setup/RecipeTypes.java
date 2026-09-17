package com.github.drakescraft_labs.gcereborn.setup;

import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class RecipeTypes {

    public static final RecipeType FROM_NET = GeneticChickengineering.getLocalization().getRecipeType(
        "from_net",
        GCEItems.CHICKEN_NET
    );
    public static final RecipeType FROM_CHICKEN = GeneticChickengineering.getLocalization().getRecipeType(
        "from_chicken",
        GCEItems.EXCITATION_CHAMBER
    );
    public static final RecipeType BIO_CLONING = GeneticChickengineering.getLocalization().getRecipeType(
        "bio_cloning",
        GCEItems.BIO_CLONING_VAT
    );
    public static final RecipeType MUTAGENIC_SPLICING = GeneticChickengineering.getLocalization().getRecipeType(
        "mutagenic_splicing",
        GCEItems.MUTAGENIC_SPLICER
    );
    public static final RecipeType INDUSTRIAL_ROOST = GeneticChickengineering.getLocalization().getRecipeType(
        "industrial_roost",
        GCEItems.INDUSTRIAL_ROOST
    );
}
