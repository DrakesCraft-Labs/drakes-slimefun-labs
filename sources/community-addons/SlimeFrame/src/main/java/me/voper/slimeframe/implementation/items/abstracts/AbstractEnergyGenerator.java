package me.voper.slimeframe.implementation.items.abstracts;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.EnergyNetProvider;

import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

public abstract class AbstractEnergyGenerator extends AbstractContainer implements EnergyNetProvider {

    @Getter
    protected int energyGenerated = -1;
    protected int energyCapacity = -1;

    public AbstractEnergyGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public int getGeneratedOutput(@Nonnull Location location, @Nonnull Config config) {
        BlockMenu inventory = BlockStorage.getInventory(location);
        return getGeneratedOutput(inventory, location.getBlock());
    }

    public int getGeneratedOutput(BlockMenu menu, Block block) {
        return 0;
    }

    public AbstractEnergyGenerator setEnergyCapacity(int energyCapacity) {
        Preconditions.checkArgument(energyCapacity > 0, "Energy capacity must be greater than zero");
        Preconditions.checkArgument(energyCapacity > energyGenerated, "Energy capacity must be greater than the energy generated");
        this.energyCapacity = energyCapacity;
        return this;
    }

    public AbstractEnergyGenerator setEnergyGenerated(int energyGenerated) {
        Preconditions.checkArgument(energyGenerated > 0, "Energy generated must be greater than zero");
        this.energyGenerated = energyGenerated;
        return this;
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        if (energyGenerated == -1) {
            warn("The energy generated has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyGenerated(...)' before registering!");
            return;
        }

        if (energyCapacity == -1) energyCapacity = energyGenerated * 10;

        if (energyCapacity < energyGenerated) {
            warn("Energy capacity must be greater than the energy generated. The Item was disabled.");
            return;
        }
        super.register(addon);
    }

    @Override
    public int getCapacity() {
        return energyCapacity;
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[0];
    }
}
