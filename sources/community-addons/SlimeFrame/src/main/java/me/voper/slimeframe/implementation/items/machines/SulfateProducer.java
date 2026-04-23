package me.voper.slimeframe.implementation.items.machines;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractProcessorMachine;
import me.voper.slimeframe.implementation.items.multiblocks.Foundry;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class SulfateProducer extends AbstractProcessorMachine implements RecipeDisplayItem {

    @Setter
    private int production = 1;

    public SulfateProducer(SlimefunItemStack item, ItemStack[] recipe) {
        super(Groups.MACHINES, item, Foundry.RECIPE_TYPE, recipe);
    }

    @Override
    public void postRegister() {
        super.postRegister();
        registerRecipe(5, new ItemStack(Material.BASALT, 4), new SlimefunItemStack(SlimefunItems.SULFATE, 4 * production));
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return List.of(new ItemStack(Material.BASALT, 4), new SlimefunItemStack(SlimefunItems.SULFATE, 4 * production));
    }
}
