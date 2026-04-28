package dev.j3fftw.litexpansion.armor;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.machine.multiblock.MetalForge;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the {@link ElectricChestplate} class.
 *
 * @author j3fftw
 */
public class ElectricChestplate extends SlimefunItem implements Rechargeable {

    public ElectricChestplate() {
        super(Items.LITEXPANSION, Items.ELECTRIC_CHESTPLATE, MetalForge.RECIPE_TYPE, new ItemStack[] {
            Items.MAG_THOR.item(), null, Items.MAG_THOR.item(),
            Items.MAG_THOR.item(), SlimefunItems.BIG_CAPACITOR.item(), Items.MAG_THOR.item(),
            Items.MAG_THOR.item(), Items.MAG_THOR.item(), Items.MAG_THOR.item()
        });
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return 8192;
    }
}
