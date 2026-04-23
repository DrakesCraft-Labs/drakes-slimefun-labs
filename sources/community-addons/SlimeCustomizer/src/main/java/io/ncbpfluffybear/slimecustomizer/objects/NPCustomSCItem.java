package io.ncbpfluffybear.slimecustomizer.objects;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import org.bukkit.inventory.ItemStack;

/**
 * The {@link NPCustomSCItem} class is a wrapper
 * for the {@link SlimefunItem}. Modified to not be placeable
 *
 * @author NCBPFluffyBear
 */
public class NPCustomSCItem extends SlimefunItem implements SCNotPlaceable {

    public NPCustomSCItem(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack output) {
        super(category, item, recipeType, recipe, output);
    }

    public NPCustomSCItem(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }
}
