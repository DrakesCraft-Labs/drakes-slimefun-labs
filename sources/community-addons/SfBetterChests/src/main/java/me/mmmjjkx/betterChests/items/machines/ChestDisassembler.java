package me.mmmjjkx.betterChests.items.machines;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.RecipeDisplayItem;
import me.mmmjjkx.betterChests.BCGroups;
import me.mmmjjkx.betterChests.items.BCItems;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChestDisassembler extends AContainer implements RecipeDisplayItem {
    public ChestDisassembler(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(BCGroups.TOOLS, item, recipeType, recipe);

        setProcessingSpeed(1);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_27.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.OAK_PLANKS, 2)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_36.getItem().clone()},
                new ItemStack[]{
                        BCItems.CHEST_27.getItem().clone(),
                        new ItemStack(Material.OAK_PLANKS, 2)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_45.getItem().clone()},
                new ItemStack[]{
                        BCItems.CHEST_36.getItem().clone(),
                        new ItemStack(Material.OAK_PLANKS, 2)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_54.getItem().clone()},
                new ItemStack[]{
                        BCItems.CHEST_45.getItem().clone(),
                        new ItemStack(Material.OAK_PLANKS, 2)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_INPUT_27.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_INPUT_36.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_INPUT_45.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_INPUT_54.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_OUTPUT_27.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_OUTPUT_36.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_OUTPUT_45.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });

        registerRecipe(5,
                new ItemStack[]{BCItems.CHEST_OUTPUT_54.getItem().clone()},
                new ItemStack[]{
                        new ItemStack(Material.CHEST, 1),
                        new ItemStack(Material.HOPPER, 1)
                });
    }

    @Override
    public @NotNull List<ItemStack> getDisplayRecipes() {
        List<ItemStack> items = new ArrayList<>();

        List<MachineRecipe> recipes = getMachineRecipes();
        for (MachineRecipe recipe : recipes) {
            items.add(recipe.getInput()[0]);
            items.add(new ItemStack(Material.AIR));
            items.add(recipe.getOutput()[0]);
            items.add(recipe.getOutput()[1]);
        }

        return items;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_AXE);
    }

    @Override
    public @NotNull String getMachineIdentifier() {
        return getId();
    }

    @Override
    public int getCapacity() {
        return 128;
    }

    @Override
    public int getEnergyConsumption() {
        return 16;
    }
}
