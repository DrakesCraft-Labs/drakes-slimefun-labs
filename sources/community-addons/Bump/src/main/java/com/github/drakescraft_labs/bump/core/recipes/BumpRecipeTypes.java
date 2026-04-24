package com.github.drakescraft_labs.bump.core.recipes;

import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

import lombok.experimental.UtilityClass;

/**
 * This class holds all {@link RecipeType} of Bump.
 *
 * @author ybw0014
 */
@UtilityClass
public final class BumpRecipeTypes {
    public static final RecipeType GETGOLD_SPADE = new RecipeType(
        Bump.createKey("getgold_spade"),
        BumpItems.GETGOLD_SPADE
    );

    public static final RecipeType COMPRESSOR_MOCK = new RecipeType(
        Bump.createKey("compressor_mock"),
        Bump.getLocalization().getRecipeTypeItem(
            "compressor_mock",
            SlimefunItems.COMPRESSOR
        )
    );
}
