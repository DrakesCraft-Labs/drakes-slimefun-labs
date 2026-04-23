package io.github.sefiraat.simplestorage.items;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.categories.Categories;
import io.github.sefiraat.simplestorage.items.chests.NetworkChest;
import io.github.sefiraat.simplestorage.items.chests.SimpleChest;
import io.github.sefiraat.simplestorage.items.machines.Enderator;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

public final class Blocks {

    private Blocks() {
        throw new IllegalStateException("Utility class");
    }

    public static void set(SimpleStorage p) {

        new SimpleChest(Categories.CHT, SimpleStorageItemStacks.CHEST_IRON, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_IRON, 2).register(p);
        new SimpleChest(Categories.CHT, SimpleStorageItemStacks.CHEST_GOLD, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_GOLD, 4).register(p);
        new SimpleChest(Categories.CHT, SimpleStorageItemStacks.CHEST_DIAMOND, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_DIAMOND, 6).register(p);
        new SimpleChest(Categories.CHT, SimpleStorageItemStacks.INVENTORY_CELL, Enderator.TYPE, SimpleStorageRecipes.INVENTORY_CELL, 6).register(p);
        new NetworkChest(Categories.CHT, SimpleStorageItemStacks.CHEST_NETWORK, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_MASTER).register(p);

    }

}
