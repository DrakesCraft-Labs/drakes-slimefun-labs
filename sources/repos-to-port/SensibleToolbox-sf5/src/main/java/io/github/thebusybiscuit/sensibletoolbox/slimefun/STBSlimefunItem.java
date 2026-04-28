package io.github.thebusybiscuit.sensibletoolbox.slimefun;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotConfigurable;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;

public class STBSlimefunItem extends SlimefunItem implements NotPlaceable, NotConfigurable {

    @ParametersAreNonnullByDefault
    public STBSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        setUseableInWorkbench(true);
    }

}

