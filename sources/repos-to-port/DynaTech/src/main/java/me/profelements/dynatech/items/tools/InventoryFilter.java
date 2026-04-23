package me.profelements.dynatech.items.tools;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import org.bukkit.inventory.ItemStack;

public class InventoryFilter extends SlimefunBackpack {

    public InventoryFilter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(9, itemGroup, item, recipeType, recipe);
    }
    
}
