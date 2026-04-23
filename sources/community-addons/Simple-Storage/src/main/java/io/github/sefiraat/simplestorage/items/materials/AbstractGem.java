package io.github.sefiraat.simplestorage.items.materials;

import com.github.drakescraft_labs.slimefun4.api.geo.GEOResource;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public abstract class AbstractGem extends UnplaceableBlock implements NotPlaceable, GEOResource {

    private final int maxDeviation;
    private final String name;

    protected AbstractGem(String name, ItemGroup category, SlimefunItemStack slimefunItemStack, RecipeType recipeType, ItemStack[] recipe, int deviation) {
        super(category, slimefunItemStack, recipeType, recipe);
        this.maxDeviation = deviation;
        this.name = name;
    }

    @Override
    public int getMaxDeviation() {
        return maxDeviation;
    }

    @Override
    public @Nonnull String getName() {
        return name;
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }

}
