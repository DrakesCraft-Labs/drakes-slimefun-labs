package dev.drake.infinitylib.abstracts;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Compatibility class for InfinityLib AbstractMachine.
 */
public abstract class AbstractMachine extends AbstractTicker implements EnergyNetComponent {

    private final int energyPerTick;
    private final int capacity;

    protected AbstractMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int energyPerTick) {
        super(itemGroup, item, recipeType, recipe);
        this.capacity = capacity;
        this.energyPerTick = energyPerTick;
    }

    @Override
    protected void tick(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config config) {
        if (getCharge(b.getLocation()) >= energyPerTick) {
            if (process(menu, b, config)) {
                removeCharge(b.getLocation(), energyPerTick);
            }
        }
    }

    protected abstract boolean process(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config config);

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }
    
    @Override
    public abstract void setupMenu(@Nonnull BlockMenuPreset preset);
    
    @Nonnull
    @Override
    public abstract int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, ItemStack item);
}
