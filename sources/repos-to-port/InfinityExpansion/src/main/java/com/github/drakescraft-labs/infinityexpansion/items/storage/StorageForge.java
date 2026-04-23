package com.github.drakescraft-labs.infinityexpansion.items.storage;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.infinitylib.machines.CraftingBlock;
import com.github.drakescraft-labs.infinitylib.machines.MachineRecipeType;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

/**
 * A crafting machine for upgrading storage units and retaining the stored items
 *
 * @author Mooy1
 */
@ParametersAreNonnullByDefault
public final class StorageForge extends CraftingBlock {

    public static final MachineRecipeType TYPE = new MachineRecipeType("storage_forge", Storage.STORAGE_FORGE);

    public StorageForge(ItemGroup category, SlimefunItemStack stack, RecipeType type, ItemStack[] recipe) {
        super(category, stack, type, recipe);
        addRecipesFrom(TYPE);
    }

    @Override
    protected void onSuccessfulCraft(BlockMenu menu, ItemStack toOutput) {
        StorageUnit.transferToStack(menu.getItemInSlot(layout.inputSlots()[4]), toOutput);
    }

}
