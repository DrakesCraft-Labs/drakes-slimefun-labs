package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.item.exts;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.RainbowTickHandler;
import com.github.drakescraft-labs.slimefun4.utils.ColoredMaterial;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.parent.CustomItem;

public class CustomRainbowBlock extends CustomItem {
    private final Object[] constructorArgs;

    public CustomRainbowBlock(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            RainbowTickHandler ticker) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(ticker);

        constructorArgs = new Object[] {itemGroup, item, recipeType, recipe, ticker};
    }

    public CustomRainbowBlock(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            ColoredMaterial materialType) {
        this(itemGroup, item, recipeType, recipe, new RainbowTickHandler(materialType));
    }

    public CustomRainbowBlock(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            List<Material> materials) {
        this(itemGroup, item, recipeType, recipe, new RainbowTickHandler(materials));
    }

    @Override
    public Object[] constructorArgs() {
        return constructorArgs;
    }
}
