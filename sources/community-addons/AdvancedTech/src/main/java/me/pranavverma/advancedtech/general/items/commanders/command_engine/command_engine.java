package me.pranavverma.advancedtech.general.items.commanders.command_engine;


import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import org.bukkit.inventory.ItemStack;


public class command_engine extends SlimefunItem implements Radioactive {

    public command_engine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }



    @Override
    public Radioactivity getRadioactivity() {
        return Radioactivity.MODERATE;
    }


}
