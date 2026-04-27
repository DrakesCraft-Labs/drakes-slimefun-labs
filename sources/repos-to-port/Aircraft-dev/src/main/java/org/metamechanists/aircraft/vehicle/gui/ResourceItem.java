package org.metamechanists.aircraft.vehicle.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleResource;
import org.metamechanists.kinematiccore.api.item.ItemStackBuilder;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Map;

import static org.metamechanists.aircraft.utils.Utils.formatMiniMessage;

public class ResourceItem extends AbstractItem {
    private final VehicleEntity vehicleEntity;
    private final String resourceName;

    private static final int RESOURCE_BARS = 20;
    private static final String GRAY = "<color:#777777>";
    private static final String EMPTY_BAR = "<color:#444444>";
    private static final String INFO = "<color:#aaaaaa>";

    private @NotNull String resourceAmountColor(@NotNull String name, @NotNull VehicleResource resource) {
        int green = (int) (255 * vehicleEntity.remainingResource(name) / resource.capacity());
        int red = 255 - green;
        return "<color:#" + Integer.toHexString(red) + Integer.toHexString(green) + "00>";
    }

    private @NotNull String resourceBar(@NotNull String name, @NotNull VehicleResource resource) {
        int filledBars = (int) (RESOURCE_BARS * vehicleEntity.remainingResource(name) / resource.capacity());
        int emptyBars = RESOURCE_BARS - filledBars;
        return GRAY + "["
                + resourceAmountColor(name, resource) + "|".repeat(filledBars)
                + INFO + "|".repeat(emptyBars)
                + GRAY + "]";
    }

    private @NotNull String resourceRemaining(@NotNull String name, @NotNull VehicleResource resource) {
        return GRAY + "("
                + resourceAmountColor(name, resource)
                + Math.round(vehicleEntity.remainingResource(name))
                + GRAY + "/"
                + "<color:#ffffff>" + Math.round(resource.capacity())
                + GRAY + ")";
    }

    private @NotNull String resourceItemName(@NotNull String name, @NotNull VehicleResource resource) {
        String title = switch (resource.type()) {
            case VehicleResource.ResourceType.COMBUSTIBLE -> "Fuel";
            case VehicleResource.ResourceType.WATER -> "Water";
        };

        return formatMiniMessage(resource.type().color()
                + title
                + " "
                + resourceBar(name, resource)
                + " "
                + resourceRemaining(name, resource));
    }

    public ResourceItem(@NotNull VehicleEntity vehicleEntity, String resourceName) {
        super();
        this.vehicleEntity = vehicleEntity;
        this.resourceName = resourceName;
    }

    @Override
    public ItemProvider getItemProvider() {
        VehicleResource resource = vehicleEntity.schema().getResources().get(resourceName);
        return new ItemBuilder(resource.type().icon())
                .setDisplayName(resourceItemName(resourceName, resource))
                .addLoreLines(formatMiniMessage("<color:#999999>" + ItemStackBuilder.RIGHT_ARROW + " Click to refuel"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        VehicleResource resource = vehicleEntity.schema().getResources().get(resourceName);
        for (Map.Entry<Material, Double> entry : resource.type().acceptedFuels().entrySet()) {

            if (inventoryClickEvent.getCursor().getType() == entry.getKey()) {
                double newAmount = vehicleEntity.getResources().get(resourceName) + entry.getValue();

                if (newAmount <= resource.capacity()) {
                    inventoryClickEvent.getCursor().subtract(1);
                    vehicleEntity.getResources().put(resourceName, newAmount);
                    if (inventoryClickEvent.getCursor().getType() == Material.WATER_BUCKET) {
                        player.getInventory().addItem(new ItemStack(Material.BUCKET));
                    }
                    notifyWindows();
                }

                break;
            }
        }
    }
}
