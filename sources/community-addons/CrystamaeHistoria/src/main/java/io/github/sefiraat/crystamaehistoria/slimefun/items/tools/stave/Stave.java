package io.github.sefiraat.crystamaehistoria.slimefun.items.tools.stave;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class Stave extends SlimefunItem {

    @Getter
    private final int level;

    @ParametersAreNonnullByDefault
    public Stave(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int level) {
        super(itemGroup, item, recipeType, recipe);
        this.level = level;
    }

}
