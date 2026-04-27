package org.metamechanists.aircraft.vehicle.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;


public final class MainGui {
    private MainGui() {}

    public static void show(@NotNull VehicleEntity vehicleEntity, @NotNull Player player) {
        Gui.Builder.Normal gui = Gui.normal()
                .setStructure(vehicleEntity.schema().getGuiStructure().toArray(new String[]{}))
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE))
                .addIngredient('i', vehicleEntity.schema().getItemStack())
                .addIngredient('f', () -> new FlyItem(vehicleEntity, player))
                .addIngredient('p', () -> new PickUpItem(vehicleEntity, player));

        for (String resourceName : vehicleEntity.getResources().keySet()) {
            char key = vehicleEntity.schema().getResources().get(resourceName).guiKey();
            gui.addIngredient(key, () -> new ResourceItem(vehicleEntity, resourceName));
        }

        Window.single()
                .setViewer(player)
                .setTitle(vehicleEntity.schema().getItemStack().getItemMeta().getDisplayName())
                .setGui(gui.build())
                .build()
                .open();
        // customise, resources, passengers, cargo, fly, permissions, pick up,
    }
}
