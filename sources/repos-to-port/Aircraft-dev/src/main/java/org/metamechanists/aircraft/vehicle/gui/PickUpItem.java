package org.metamechanists.aircraft.vehicle.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.utils.Utils;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;


public class PickUpItem extends AbstractItem {
    private final VehicleEntity vehicleEntity;
    private final Player player;

    public PickUpItem(@NotNull VehicleEntity vehicleEntity, @NotNull Player player) {
        super();
        this.vehicleEntity = vehicleEntity;
        this.player = player;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (vehicleEntity.canPickUp(player)) {
            return new ItemBuilder(Material.LIME_CONCRETE)
                    .setDisplayName(Utils.formatMiniMessage("<color:#00ff00>Pick Up"));
        }
        return new ItemBuilder(Material.RED_CONCRETE)
                    .setDisplayName(Utils.formatMiniMessage("<color:#ff0000>Pick Up"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (vehicleEntity.canPickUp(player)) {
            player.closeInventory();
            vehicleEntity.pickUp();
        }
    }
}
