package me.voper.slimeframe.implementation.items.resources;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotHopperable;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft-labs.slimefun4.implementation.items.RadioactiveItem;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.utils.Utils;

public class Resource extends SlimefunItem implements NotPlaceable, NotHopperable {

    public Resource(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(Groups.RESOURCES, item, recipeType, recipe);
        setUseableInWorkbench(false);
    }

    public Resource(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack output) {
        super(Groups.RESOURCES, item, recipeType, recipe, output);
        setUseableInWorkbench(false);
    }

    public Resource(SlimefunItemStack item, RecipeType recipeType, ItemStack itemStack) {
        this(item, recipeType, new ItemStack[]{itemStack, itemStack, itemStack, itemStack, itemStack, itemStack, itemStack, itemStack, itemStack});
    }

    public Resource(SlimefunItemStack item, ItemStack itemStack) {
        this(item, new ItemStack[]{itemStack, itemStack, itemStack, itemStack, itemStack, itemStack, itemStack, itemStack, itemStack});
    }

    public Resource(SlimefunItemStack item, RecipeType recipeType) {
        this(item, recipeType, Utils.NULL_ITEMS_ARRAY);
    }

    public Resource(SlimefunItemStack item, ItemStack[] recipe, ItemStack output) {
        this(item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, output);
    }

    public Resource(SlimefunItemStack item, ItemStack[] recipe) {
        this(item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

    @Nonnull
    public static SlimefunItem createRadioactive(SlimefunItemStack itemStack, Radioactivity radioactivity, ItemStack[] recipe) {
        return new RadioactiveItem(Groups.RESOURCES, radioactivity, itemStack, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }
}
