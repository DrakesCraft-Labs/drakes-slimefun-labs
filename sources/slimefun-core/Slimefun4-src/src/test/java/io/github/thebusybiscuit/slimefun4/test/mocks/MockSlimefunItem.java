package com.github.drakescraft_labs.slimefun4.test.mocks;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

public class MockSlimefunItem extends SlimefunItem {

    public MockSlimefunItem(ItemGroup itemGroup, ItemStack item, String id) {
        super(itemGroup, new SlimefunItemStack(id, item), RecipeType.NULL, new ItemStack[9]);
    }

}
