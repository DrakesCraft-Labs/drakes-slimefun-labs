package me.profelements.dynatech.listeners;

import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;
import me.profelements.dynatech.DynaTech;
import me.profelements.dynatech.items.tools.InventoryFilter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryFilterListener implements Listener {

    private final InventoryFilter inventoryFilter;

    public InventoryFilterListener(@Nonnull DynaTech plugin, @Nonnull InventoryFilter inventoryFilter) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.inventoryFilter = inventoryFilter;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (inventoryFilter == null || inventoryFilter.isDisabled()) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        final Player p = (Player) e.getEntity();
        final Item pickedItem = e.getItem();
        final ItemStack pickedItemStack = pickedItem.getItemStack();

        for (ItemStack filter : p.getInventory().getContents()) {
            if (inventoryFilter.isItem(filter) && SlimefunUtils.canPlayerUseItem(p, filter, true)) {
                PlayerProfile.getBackpack(filter,
                    backpack -> {
                    for (ItemStack item : backpack.getInventory().getContents()) {
                        if (SlimefunUtils.isItemSimilar(item, pickedItemStack, true, false)) {
                            e.setCancelled(true);
                            pickedItem.remove();
                            break;
                        }
                    }
                }
                );
            }
        }
    }
}
