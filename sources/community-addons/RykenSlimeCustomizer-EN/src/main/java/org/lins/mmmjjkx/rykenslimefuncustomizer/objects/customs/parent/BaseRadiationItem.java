package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.parent;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft_labs.slimefun4.implementation.items.RadioactiveItem;
import org.bukkit.inventory.ItemStack;

// FUCK YOU https://github.com/Slimefun/Slimefun4/pull/3131
public abstract class BaseRadiationItem extends RadioactiveItem {
    public BaseRadiationItem(
            ItemGroup itemGroup,
            Radioactivity radioactivity,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe) {
        super(itemGroup, radioactivity, item, recipeType, recipe);
    }

    public abstract Object[] constructArgs();
}
