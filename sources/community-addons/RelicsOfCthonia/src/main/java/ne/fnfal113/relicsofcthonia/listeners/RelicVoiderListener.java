package ne.fnfal113.relicsofcthonia.listeners;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import ne.fnfal113.relicsofcthonia.slimefun.RelicVoider;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RelicVoiderListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onScrollHotbar(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }

        Inventory inv = player.getInventory();
        ItemStack previous = inv.getItem(event.getPreviousSlot());
        if (!(SlimefunItem.getByItem(previous) instanceof RelicVoider)) {
            return;
        }

        event.setCancelled(true);
        int direction = event.getNewSlot() - event.getPreviousSlot();
        if (Math.abs(direction) >= 8) {
            direction = direction > 0 ? -1 : 1;
        } else {
            direction = direction > 0 ? 1 : -1;
        }

        int oldQuota = RelicVoider.getConditionQuota(previous);
        int quota = Math.clamp(oldQuota + direction, 1, 100);
        if (oldQuota == quota) {
            return;
        }

        RelicVoider.setConditionQuota(previous, quota);
        Utils.replaceLoreValue(previous, "Condition Quota", "&d", "Condition Quota: ", "%", quota, true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Item pickedUpItem = event.getItem();
        ItemStack pickedUpItemStack = pickedUpItem.getItemStack();
        SlimefunItem pickedUpSfItem = SlimefunItem.getByItem(pickedUpItemStack);
        if (!(pickedUpSfItem instanceof AbstractRelic relic)) {
            return;
        }

        Inventory inv = player.getInventory();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack itemStack = inv.getItem(i);
            if (SlimefunItem.getByItem(itemStack) instanceof RelicVoider voider
                    && voider.onRelicPickup(event, itemStack, relic, pickedUpItem)) {
                break;
            }
        }
    }
}
