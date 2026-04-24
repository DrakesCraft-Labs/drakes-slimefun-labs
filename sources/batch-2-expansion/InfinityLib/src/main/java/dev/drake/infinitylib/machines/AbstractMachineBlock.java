package dev.drake.infinitylib.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

@Setter
@ParametersAreNonnullByDefault
public abstract class AbstractMachineBlock extends TickingMenuBlock implements EnergyNetComponent {

    protected int energyPerTick = -1;
    protected int energyCapacity = -1;

    public AbstractMachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void tick(Block b, BlockMenu menu) {
        if (getCharge(menu.getLocation()) < energyPerTick) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(getStatusSlot(), NO_ENERGY_ITEM);
            }
        }
        else if (process(b, menu)) {
            removeCharge(menu.getLocation(), energyPerTick);
        }
    }

    protected abstract boolean process(Block b, BlockMenu menu);

    protected abstract int getStatusSlot();

    @Override
    public final int getCapacity() {
        return energyCapacity;
    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public final void register(@Nonnull SlimefunAddon addon) {
        if (energyPerTick == -1) {
            throw new IllegalStateException("You must call .energyPerTick() before registering!");
        }
        if (energyCapacity == -1) {
            energyCapacity = energyPerTick * 2;
        }
        super.register(addon);
    }

}
