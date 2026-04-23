package com.github.drakescraft-labs.gcereborn.setup;

import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft-labs.gcereborn.items.GCEItems;

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
}
