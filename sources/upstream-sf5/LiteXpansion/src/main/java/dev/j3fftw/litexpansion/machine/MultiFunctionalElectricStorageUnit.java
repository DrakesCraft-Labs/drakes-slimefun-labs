package dev.j3fftw.litexpansion.machine;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.machine.extensions.ChargingStorageUnit;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the {@link MultiFunctionalElectricStorageUnit} class.
 *
 * @author j3fftw
 */
public class MultiFunctionalElectricStorageUnit extends ChargingStorageUnit {

    public MultiFunctionalElectricStorageUnit() {
        super(Items.LITEXPANSION, 4_000_000, 12, Items.MULTI_FUNCTIONAL_ELECTRIC_STORAGE_UNIT,
            RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GOLD_CABLE.item(), SlimefunItems.POWER_CRYSTAL.item(), Items.GOLD_CABLE.item(),
                SlimefunItems.POWER_CRYSTAL.item(), Items.MACHINE_BLOCK.item(), SlimefunItems.POWER_CRYSTAL.item(),
                Items.GOLD_CABLE.item(), SlimefunItems.POWER_CRYSTAL.item(), Items.GOLD_CABLE.item()
            });
    }

    @Override
    public String getTitle() {
        return "\u00a78MFE";
    }
}
