package com.github.drakescraft-labs.infinitylib.recipes.normal;

import dev.drake.dough.collections.RandomizedSet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Compatibility class for InfinityLib RandomRecipeMap.
 */
public final class RandomRecipeMap {

    private final Map<ItemStack, RandomizedSet<ItemStack>> recipes = new HashMap<>();

    public void put(@Nonnull ItemStack input, @Nonnull RandomizedSet<ItemStack> outputs) {
        recipes.put(input, outputs);
    }

    @Nullable
    public ItemStack get(@Nonnull ItemStack input) {
        for (Map.Entry<ItemStack, RandomizedSet<ItemStack>> entry : recipes.entrySet()) {
            if (entry.getKey().isSimilar(input)) {
                return entry.getValue().getRandom();
            }
        }
        return null;
    }
}
