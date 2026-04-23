package ne.fnfal113.relicsofcthonia.api;

import com.github.drakescraft-labs.slimefun4.api.items.ItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public interface OffHandRightClickHandler extends ItemHandler {
    void onItemRightClick(PlayerInteractEvent event, Player player, ItemStack offHand);

    @Override
    default @Nonnull Class<? extends ItemHandler> getIdentifier() {
        return OffHandRightClickHandler.class;
    }
}
