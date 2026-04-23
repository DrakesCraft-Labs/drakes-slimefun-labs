package io.github.sefiraat.simplestorage.items;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.categories.Categories;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;

public class Items {

    private Items() {
        throw new IllegalStateException("Utility class");
    }

    public static void set(SimpleStorage p) {

        new UnplaceableBlock(Categories.ITM, SimpleStorageItemStacks.UPGRADE_BARREL, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.AUGMENT_BARREL).register(p);

    }

}
