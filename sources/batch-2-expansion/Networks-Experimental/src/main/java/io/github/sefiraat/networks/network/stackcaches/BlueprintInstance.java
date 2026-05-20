package io.github.sefiraat.networks.network.stackcaches;

import com.balugaq.netex.utils.Converter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlueprintInstance extends ItemStackCache {

    @Getter
    private final ItemStack[] recipeItems;
    @Nullable
    private Recipe recipe = null;

    public BlueprintInstance(@Nonnull ItemStack[] recipeItems, @Nonnull ItemStack expectedOutput) {
        super(expectedOutput);
        this.recipeItems = recipeItems;
    }

    @Nullable
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(@Nullable Recipe recipe) {
        this.recipe = recipe;
    }

    public void generateVanillaRecipe(World world) {
        if (this.recipe == null) {
            this.recipe = Bukkit.getCraftingRecipe(this.recipeItems, world);
        }
    }
}
