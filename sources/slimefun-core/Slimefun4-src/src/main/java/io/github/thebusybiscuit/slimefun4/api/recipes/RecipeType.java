package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.function.BiConsumer;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import dev.drake.dough.recipes.MinecraftRecipe;

@Deprecated(forRemoval = false)
public class RecipeType extends com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType {

    public RecipeType(ItemStack item, String machine) {
        super(item, machine);
    }

    public RecipeType(NamespacedKey key, io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack slimefunItem, String... lore) {
        super(key, slimefunItem, lore);
    }

    public RecipeType(NamespacedKey key, ItemStack item, BiConsumer<ItemStack[], ItemStack> callback, String... lore) {
        super(key, item, callback, lore);
    }

    public RecipeType(NamespacedKey key, ItemStack item) {
        super(key, item);
    }

    public RecipeType(MinecraftRecipe<?> recipe) {
        super(recipe);
    }
}
