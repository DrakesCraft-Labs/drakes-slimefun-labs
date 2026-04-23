package dev.j3fftw.extrautils.objects;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * A class that can be used to register {@link SlimefunItem}
 * that can interact with hoppers. Items registered using
 * this class can not interact with the material hopper.
 *
 * @author NCBPFluffyBear
 */
public class NonHopperableBlock extends SlimefunItem {

    public NonHopperableBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @EventHandler
    public void onHopper(InventoryMoveItemEvent e) {
        if (e.getSource().getType() == InventoryType.HOPPER
            && e.getDestination().getLocation() != null
            && BlockStorage.hasBlockInfo(e.getDestination().getLocation())
            && BlockStorage.check(e.getDestination().getLocation()) instanceof NonHopperableBlock) {
            e.setCancelled(true);
        }
    }
}
