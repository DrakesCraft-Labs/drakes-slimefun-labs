package io.github.sefiraat.networks.network;

import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class GridItemRequest extends ItemRequest {
    private final Player player;

    public GridItemRequest(ItemStack itemStack, int amount, Player player) {
        super(itemStack, amount);
        this.player = player;
    }
}
