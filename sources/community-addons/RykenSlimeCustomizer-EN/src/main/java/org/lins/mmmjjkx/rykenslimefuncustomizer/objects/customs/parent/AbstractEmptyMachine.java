package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.parent;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineProcessHolder;
import com.github.drakescraft_labs.slimefun4.core.machines.MachineOperation;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public abstract class AbstractEmptyMachine<O extends MachineOperation> extends SlimefunItem
        implements InventoryBlock, MachineProcessHolder<O> {
    public AbstractEmptyMachine(
            ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public abstract BlockTicker getBlockTicker();
}
