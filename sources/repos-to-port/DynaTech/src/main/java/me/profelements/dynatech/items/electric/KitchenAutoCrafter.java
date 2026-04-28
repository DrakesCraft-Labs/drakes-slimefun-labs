package me.profelements.dynatech.items.electric;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.exoticgarden.ExoticGardenRecipeTypes;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.items.autocrafters.SlimefunAutoCrafter;

public class KitchenAutoCrafter extends SlimefunAutoCrafter {
    
    @ParametersAreNonnullByDefault
	public KitchenAutoCrafter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe, ExoticGardenRecipeTypes.KITCHEN);
	}
}
