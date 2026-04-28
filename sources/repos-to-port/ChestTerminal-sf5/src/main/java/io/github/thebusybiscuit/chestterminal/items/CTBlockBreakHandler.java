package io.github.thebusybiscuit.chestterminal.items;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;

import com.github.drakescraft_labs.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

/**
 * Handles block break events for ChestTerminal blocks,
 * dropping items from specified inventory slots.
 *
 * @author TheBusyBiscuit
 */
class CTBlockBreakHandler extends SimpleBlockBreakHandler {

    private final int[] slots;

    CTBlockBreakHandler(@Nonnull int[] slots) {
        this.slots = slots;
    }

    @Override
    public void onBlockBreak(@Nonnull Block b) {
        BlockMenu menu = BlockStorage.getInventory(b);

        if (menu != null) {
            menu.dropItems(b.getLocation(), slots);
        }
    }
}
