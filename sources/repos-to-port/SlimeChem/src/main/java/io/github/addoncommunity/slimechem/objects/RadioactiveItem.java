package io.github.addoncommunity.slimechem.objects;

import io.github.addoncommunity.slimechem.implementation.attributes.Ingredient;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.Category;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import org.bukkit.inventory.ItemStack;

@Getter
public class RadioactiveItem extends SlimefunItem implements Radioactive {

    private final Radioactivity radioactivity;
    private final Ingredient ingredient;

    public RadioactiveItem(Category category, Ingredient ingredient, RecipeType recipeType, ItemStack[] recipe, Radioactivity radioactivity) {
        super(category, ingredient.getItem(), recipeType, recipe);

        this.radioactivity = radioactivity;
        this.ingredient = ingredient;
    }
}
