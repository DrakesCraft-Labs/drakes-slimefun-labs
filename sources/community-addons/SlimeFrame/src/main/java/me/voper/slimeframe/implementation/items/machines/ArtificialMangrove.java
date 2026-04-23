package me.voper.slimeframe.implementation.items.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;

import me.voper.slimeframe.implementation.items.abstracts.AbstractProcessorMachine;

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;

public class ArtificialMangrove extends AbstractProcessorMachine implements RecipeDisplayItem {

    public ArtificialMangrove(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void postRegister() {
        super.postRegister();
        registerRecipe(5, new ItemStack(Material.DIRT, 2), new ItemStack(Material.MUD));
        registerRecipe(5, new ItemStack(Material.COARSE_DIRT, 2), new ItemStack(Material.MUD));
        registerRecipe(5, new ItemStack(Material.ROOTED_DIRT, 2), new ItemStack(Material.MUD));
        registerRecipe(5, new ItemStack[]{new ItemStack(Material.VINE), new ItemStack(Material.MUD)}, new ItemStack[]{new ItemStack(Material.MANGROVE_ROOTS, 2)});
        registerRecipe(5, new ItemStack[]{new ItemStack(Material.MUD), new ItemStack(Material.MANGROVE_ROOTS)}, new ItemStack[]{new ItemStack(Material.MUDDY_MANGROVE_ROOTS, 3)});
        registerRecipe(5, new ItemStack[]{new ItemStack(Material.MUD), new ItemStack(Material.WHEAT)}, new ItemStack[]{new ItemStack(Material.PACKED_MUD, 4)});
        registerRecipe(5, new ItemStack[]{new ItemStack(Material.MUD), new ItemStack(Material.WHEAT_SEEDS)}, new ItemStack[]{new ItemStack(Material.PACKED_MUD, 1)});
        registerRecipe(5, new ItemStack(Material.PACKED_MUD), new ItemStack(Material.MUD_BRICKS, 2));
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.MANGROVE_ROOTS);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> displayRecipes = new ArrayList<>();
        for (MachineRecipe recipe : recipes) {
            ItemStack[] input = recipe.getInput();
            ItemStack[] output = recipe.getOutput();
            displayRecipes.add(input[0]);
            displayRecipes.add(output[0]);
            if (input.length == 1 && output.length == 1) continue;

            if (input.length == 2) {
                displayRecipes.add(input[1]);
            } else {
                displayRecipes.add(null);
            }

            if (output.length == 2) {
                displayRecipes.add(output[1]);
            } else {
                displayRecipes.add(null);
            }
        }
        return displayRecipes;
    }
}