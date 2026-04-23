package io.github.sefiraat.crystamaehistoria.slimefun.items.tools;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.magical.InfusedMagnet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class DisplacedVoid extends InfusedMagnet {

    @ParametersAreNonnullByDefault
    public DisplacedVoid(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public double getRadius() {
        return super.getRadius() + 3;
    }
}
