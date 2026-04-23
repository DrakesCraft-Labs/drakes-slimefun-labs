package me.pranavverma.advancedtech.general.items.diggers;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import me.pranavverma.advancedtech.general.items.diggers.lib.carbonado.ExplosiveTool7x7;

import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class handheld_digger_3 extends ExplosiveTool7x7 {

    @ParametersAreNonnullByDefault
    public handheld_digger_3(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }



}
