package io.github.thebusybiscuit.chestterminal.items;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;

import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

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
