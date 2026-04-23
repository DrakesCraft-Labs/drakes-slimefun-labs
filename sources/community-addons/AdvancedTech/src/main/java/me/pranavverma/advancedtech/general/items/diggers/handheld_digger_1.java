package me.pranavverma.advancedtech.general.items.diggers;


import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import me.pranavverma.advancedtech.general.items.diggers.lib.basic.ExplosiveTool4x4;


public class handheld_digger_1 extends ExplosiveTool4x4 {

    @ParametersAreNonnullByDefault
    public handheld_digger_1(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}
