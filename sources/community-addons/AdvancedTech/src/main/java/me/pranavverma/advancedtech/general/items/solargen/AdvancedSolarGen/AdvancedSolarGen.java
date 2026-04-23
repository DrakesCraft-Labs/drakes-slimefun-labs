package me.pranavverma.advancedtech.general.items.solargen.AdvancedSolarGen;


import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import me.pranavverma.advancedtech.general.items.solargen.AdvancedSolarGen.lib.AdvancedSolarGenLib;


public class AdvancedSolarGen extends AdvancedSolarGenLib {

    public AdvancedSolarGen(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int dayenergy, int nightenergy) {
        
        super(itemGroup, dayenergy, nightenergy, item, recipeType, recipe);
        

    }

    

}
