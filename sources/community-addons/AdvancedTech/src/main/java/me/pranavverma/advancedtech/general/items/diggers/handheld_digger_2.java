package me.pranavverma.advancedtech.general.items.diggers;


import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import me.pranavverma.advancedtech.general.items.diggers.lib.advanced.ExplosiveTool5x5;


public class handheld_digger_2 extends ExplosiveTool5x5{

    @ParametersAreNonnullByDefault
    public handheld_digger_2(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}
