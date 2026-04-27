package org.metamechanists.aircraft.vehicle.component.base;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;


public abstract class HudItemComponent<T extends HudItemComponent.HudItemComponentSchema> extends ItemComponent<T> {
    @Getter
    public abstract static class HudItemComponentSchema extends KinematicEntitySchema {
        private final HudSection.HudSectionSchema sectionSchema;

        protected HudItemComponentSchema (
                @NotNull String id,
                @NotNull EntityType entityType,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser,
                @NotNull Class<? extends KinematicEntity<?, ?>> kinematicClass) {
            super(sectionSchema.idWithoutNamespace() + "_" + id, entityType, kinematicClass);
            this.sectionSchema = sectionSchema;
            register(Aircraft.getInstance());
        }
    }

    protected HudItemComponent(@NotNull T schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, () -> {
            Pig pig = vehicleEntity.entity();
            ItemDisplay item = (ItemDisplay) pig.getLocation().getWorld().spawnEntity(pig.getLocation(), EntityType.ITEM_DISPLAY);
            pig.addPassenger(item);
            return item;
        });
    }

    protected HudItemComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
    }
}
