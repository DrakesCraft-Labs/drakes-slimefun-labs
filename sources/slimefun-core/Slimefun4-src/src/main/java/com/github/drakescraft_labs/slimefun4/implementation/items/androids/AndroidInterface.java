package com.github.drakescraft_labs.slimefun4.implementation.items.androids;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockDispenseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * The {@link AndroidInterface} are inventories used to interact with a {@link ProgrammableAndroid}.
 * There are two variants of interfaces, fuel and items.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ProgrammableAndroid
 *
 */
public class AndroidInterface extends SimpleSlimefunItem<BlockDispenseHandler> {

    @ParametersAreNonnullByDefault
    public AndroidInterface(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(new VanillaInventoryDropHandler<>(Dispenser.class));
    }

    @Override
    public BlockDispenseHandler getItemHandler() {
        return (e, d, block, machine) -> e.setCancelled(true);
    }

}
