package me.pranavverma.advancedtech.general.resources.framed_uranium;


import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import org.bukkit.inventory.ItemStack;


public class framed_uranium extends SlimefunItem implements Radioactive {

    public framed_uranium(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public Radioactivity getRadioactivity() {
        return Radioactivity.VERY_DEADLY;
    }
}
