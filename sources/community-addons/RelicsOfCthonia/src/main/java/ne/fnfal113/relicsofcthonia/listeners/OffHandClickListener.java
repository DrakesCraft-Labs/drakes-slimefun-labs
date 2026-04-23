package ne.fnfal113.relicsofcthonia.listeners;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class OffHandClickListener implements Listener {

    @EventHandler
    public void onOffHandRightClick(PlayerInteractEvent event){
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK
                || event.getHand() == EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInOffHand();
        SlimefunItem item = SlimefunItem.getByItem(itemStack);
        if (item != null) {
            item.callItemHandler(OffHandRightClickHandler.class, handler -> handler.onItemRightClick(event, player, itemStack));
        }
    }

}
