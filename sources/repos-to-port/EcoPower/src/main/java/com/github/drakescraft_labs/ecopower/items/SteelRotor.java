package com.github.drakescraft_labs.ecopower.items;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

public class SteelRotor extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public SteelRotor(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, recipeOutput);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return PlayerRightClickEvent::cancel;
    }

}
