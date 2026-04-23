package io.github.sefiraat.simplestorage.items;

import io.github.sefiraat.simplestorage.categories.Categories;
import io.github.sefiraat.simplestorage.items.machines.DiePress;
import io.github.sefiraat.simplestorage.items.machines.Enderator;
import io.github.sefiraat.simplestorage.items.machines.MetalPressBasic;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

public final class Machines {

    private Machines() {
        throw new IllegalStateException("Utility class");
    }

    public static void set() {

        new DiePress(Categories.MCN, SimpleStorageItemStacks.DIE_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.DIE_PRESS);
        new MetalPressBasic(Categories.MCN, SimpleStorageItemStacks.METAL_PRESS_BASIC, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.METAL_PRESS_BASIC);
        new Enderator(Categories.MCN, SimpleStorageItemStacks.ENDERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.ENDERATOR);

    }

}
