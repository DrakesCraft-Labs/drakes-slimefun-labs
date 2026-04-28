package dev.j3fftw.litexpansion.machine;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.machine.extensions.ChargingStorageUnit;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the {@link MultiFunctionalStorageUnit} class.
 *
 * @author j3fftw
 */
public class MultiFunctionalStorageUnit extends ChargingStorageUnit {

    public MultiFunctionalStorageUnit() {
        super(Items.LITEXPANSION, 40_000_000, 52, Items.MULTI_FUNCTIONAL_STORAGE_UNIT,
            RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.LAPOTRON_CRYSTAL.item(), Items.ADVANCED_CIRCUIT.item(), Items.LAPOTRON_CRYSTAL.item(),
                Items.LAPOTRON_CRYSTAL.item(), Items.MULTI_FUNCTIONAL_ELECTRIC_STORAGE_UNIT.item(), Items.LAPOTRON_CRYSTAL.item(),
                Items.LAPOTRON_CRYSTAL.item(), Items.ADVANCED_MACHINE_BLOCK.item(), Items.LAPOTRON_CRYSTAL.item()
            });
    }

    @Override
    public String getTitle() {
        return "\u00a76MFSU";
    }
}
