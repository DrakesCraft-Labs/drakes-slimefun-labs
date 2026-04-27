package org.metamechanists.aircraft.slimefun;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;


public class AircraftControl extends SlimefunItem {
    private final String signal;

    public AircraftControl(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String signal) {
        super(itemGroup, item, recipeType, recipe);
        this.signal = signal;
        addItemHandler(onItemUse());
    }

    private @NotNull ItemUseHandler onItemUse() {
        return event -> {
            Player player = event.getPlayer();
            Entity riding = player.getVehicle();
            if (riding == null) {
                return;
            }

            if (!(KinematicEntity.get(riding.getUniqueId()) instanceof VehicleEntity vehicleEntity)) {
                return;
            }

            vehicleEntity.onSignal(signal);
        };
    }
}
