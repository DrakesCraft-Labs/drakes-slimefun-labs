package me.voper.slimeframe.implementation.items.abstracts;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.ItemState;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft-labs.slimefun4.core.networks.energy.EnergyNetComponentType;

import me.voper.slimeframe.utils.MachineUtils;

import lombok.Getter;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

public abstract class AbstractMachine extends AbstractTickingContainer implements EnergyNetComponent {

    @Getter
    protected int energyPerTick = -1;
    protected int energyCapacity = -1;

    public AbstractMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected final void tick(BlockMenu menu, Block b) {
        if (getCharge(menu.getLocation()) < energyPerTick) {
            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_ENERGY);
        } else if (!checkCraftConditions(menu)) {
            onCraftConditionsNotMet(menu);
        } else if (process(menu, b)) {
            onCraft(menu);
            removeCharge(b.getLocation(), energyPerTick);
        }
    }

    protected abstract boolean process(BlockMenu menu, Block b);

    protected boolean checkCraftConditions(BlockMenu menu) {
        return true;
    }

    protected void onCraftConditionsNotMet(BlockMenu menu) {
    }

    protected void onCraft(BlockMenu menu) {
    }

    @Override
    protected void onBreak(BlockBreakEvent e, BlockMenu menu, Location l) {
        menu.dropItems(l, getOutputSlots());
        menu.dropItems(l, getInputSlots());
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return energyCapacity;
    }

    public final AbstractMachine setEnergyPerTick(int energyPerTick) {
        Preconditions.checkArgument(energyPerTick > 0, "The energy consumption must be greater than zero!");
        this.energyPerTick = energyPerTick;
        return this;
    }

    public final AbstractMachine setEnergyCapacity(int capacity) {
        Preconditions.checkArgument(capacity > 0, "The capacity must be greater than zero");

        if (getState() == ItemState.UNREGISTERED) {
            this.energyCapacity = capacity;
            return this;
        } else {
            throw new IllegalStateException("You cannot modify the capacity after the Item was registered.");
        }
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        if (energyPerTick == -1) {
            warn("The energy consumption has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyConsumption(...)' before registering!");
            return;
        }
        if (energyCapacity == -1) energyCapacity = 3 * energyPerTick;
        super.register(addon);
    }

}
