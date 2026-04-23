package com.github.drakescraft-labs.slimefun4.implementation.items.food;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * This {@link SlimefunItem} can be obtained by crafting, it's
 * used for various foods and recipes.
 *
 * @author TheSilentPro
 * 
 */
public class HeavyCream extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public HeavyCream(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (!block.isPresent() || !block.get().getType().isInteractable()) {
                e.cancel();
            }
        };
    }

}
