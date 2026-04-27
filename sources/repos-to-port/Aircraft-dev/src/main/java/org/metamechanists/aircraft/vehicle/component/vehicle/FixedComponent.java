package org.metamechanists.aircraft.vehicle.component.vehicle;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleSurface;
import org.metamechanists.aircraft.vehicle.component.base.ItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.VehicleComponent;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.Set;


public class FixedComponent extends VehicleComponent<VehicleComponent.VehicleComponentSchema> {
    public static class FixedComponentSchema extends VehicleComponentSchema {
        public FixedComponentSchema(
                @NotNull String id,
                @NotNull YamlTraverser traverser,
                @NotNull Vector3f translation,
                boolean mirror) {
            super(id, FixedComponent.class, traverser, translation, mirror);
        }

        @Override
        public ItemComponent<?> build(@NotNull VehicleEntity vehicleEntity) {
            return new FixedComponent(this, vehicleEntity);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public FixedComponent(@NotNull VehicleComponent.VehicleComponentSchema vehicleComponentSchema, @NotNull VehicleEntity vehicleEntity) {
        super(vehicleComponentSchema, () -> {
            Pig pig = vehicleEntity.entity();
            ItemDisplay item = vehicleComponentSchema.modelItem(vehicleEntity, new Vector3d(), new Vector3d()).build(pig.getLocation());
            pig.addPassenger(item);
            return item;
        });
    }

    public FixedComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
    }

    @Override
    protected @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity) {
        return schema().modelItem(vehicleEntity, new Vector3d(), new Vector3d());
    }

    @Override
    public @NotNull Set<VehicleSurface> getSurfaces() {
        return schema().getSurfaces(new Vector3d());
    }
}
