package org.metamechanists.aircraft.vehicle.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleEntitySchema;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;


public final class MainGui {
    private MainGui() {}

    public static void show(@NotNull VehicleEntity vehicleEntity, @NotNull Player player) {
        VehicleEntitySchema schema = vehicleEntity.schema();
        if (schema == null) {
            return;
        }
        ItemStack vehicleStack = schema.getItemStack();
        ItemMeta vehicleMeta = vehicleStack.getItemMeta();
        String title = vehicleMeta != null && vehicleMeta.getDisplayName() != null
                ? vehicleMeta.getDisplayName()
                : "Vehicle";

        Gui.Builder.Normal gui = Gui.normal()
                .setStructure(schema.getGuiStructure().toArray(new String[]{}))
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE))
                .addIngredient('i', vehicleStack)
                .addIngredient('f', () -> new FlyItem(vehicleEntity, player))
                .addIngredient('p', () -> new PickUpItem(vehicleEntity, player));

        for (String resourceName : vehicleEntity.getResources().keySet()) {
            if (!schema.getResources().containsKey(resourceName)) {
                continue;
            }
            char key = schema.getResources().get(resourceName).guiKey();
            gui.addIngredient(key, () -> new ResourceItem(vehicleEntity, resourceName));
        }

        Window.single()
                .setViewer(player)
                .setTitle(title)
                .setGui(gui.build())
                .build()
                .open();
        // customise, resources, passengers, cargo, fly, permissions, pick up,
    }
}
