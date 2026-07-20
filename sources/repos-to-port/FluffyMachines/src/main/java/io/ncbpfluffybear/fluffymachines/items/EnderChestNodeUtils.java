package io.ncbpfluffybear.fluffymachines.items;

import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/** Shared ownership and atomic-transfer helpers for Ender Chest nodes. */
final class EnderChestNodeUtils {

    private EnderChestNodeUtils() {
    }

    @Nullable
    static Player getOnlineOwner(Block node) {
        String owner = BlockStorage.getLocationInfo(node.getLocation(), "owner");
        if (owner == null || owner.isBlank()) {
            return null;
        }

        try {
            return Bukkit.getPlayer(UUID.fromString(owner));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    static boolean moveFirstMatching(Inventory source, Inventory destination, Predicate<ItemStack> allowed) {
        for (int slot = 0; slot < source.getSize(); slot++) {
            ItemStack item = source.getItem(slot);
            if (item == null || item.getType().isAir() || !allowed.test(item) || !fits(destination, item)) {
                continue;
            }

            if (destination.addItem(item.clone()).isEmpty()) {
                source.setItem(slot, null);
                return true;
            }
        }

        return false;
    }

    private static boolean fits(Inventory inventory, ItemStack incoming) {
        int remaining = incoming.getAmount();

        for (ItemStack current : inventory.getContents()) {
            if (current == null || current.getType().isAir()) {
                remaining -= Math.min(incoming.getMaxStackSize(), inventory.getMaxStackSize());
            } else if (current.isSimilar(incoming)) {
                remaining -= Math.max(0, Math.min(current.getMaxStackSize(), inventory.getMaxStackSize()) - current.getAmount());
            }

            if (remaining <= 0) {
                return true;
            }
        }

        return false;
    }
}
