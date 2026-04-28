package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the {@link MagThor} class.
 *
 * @author j3fftw
 */
public class MagThor extends UnplaceableBlock {

    public MagThor() {
        super(Items.LITEXPANSION, Items.MAG_THOR, RecipeType.SMELTERY, new ItemStack[] {
                SlimefunItems.REINFORCED_ALLOY_INGOT.item(), Items.THORIUM.item(), SlimefunItems.MAGNESIUM_INGOT.item(),
                SlimefunItems.ZINC_INGOT.item(), null, null,
                null, null, null
            }
        );
    }
}
