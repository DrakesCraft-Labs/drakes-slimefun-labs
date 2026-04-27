package org.metamechanists.aircraft.vehicle.component.vehicle;

import lombok.Getter;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleSurface;
import org.metamechanists.aircraft.vehicle.component.base.ItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.VehicleComponent;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.PI;


public class PropellerComponent extends VehicleComponent<PropellerComponent.PropellerComponentSchema> {
    @Getter
    public static class PropellerComponentSchema extends VehicleComponentSchema {
        private final Vector3d rotationAxis;
        private final double maxRotationRate;
        private final List<String> fuels;

        @SuppressWarnings("DataFlowIssue")
        public PropellerComponentSchema(
                @NotNull String id,
                @NotNull YamlTraverser traverser,
                @NotNull YamlTraverser propellerTraverser,
                @NotNull Vector3f translation,
                boolean mirror
        ) {
            super(id, PropellerComponent.class, traverser, translation, mirror);
            rotationAxis = propellerTraverser.getVector3d("rotationAxis");
            maxRotationRate = propellerTraverser.get("maxRotationRate");
            fuels = propellerTraverser.get("fuels");
        }

        @Override
        public ItemComponent<?> build(@NotNull VehicleEntity vehicleEntity) {
            return new PropellerComponent(this, vehicleEntity);
        }
    }

    private double angle;

    @SuppressWarnings("WeakerAccess")
    public PropellerComponent(@NotNull PropellerComponent.PropellerComponentSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, () -> {
            Pig pig = vehicleEntity.entity();
            ItemDisplay item = schema.modelItem(vehicleEntity, new Vector3d(), new Vector3d()).build(pig.getLocation());
            pig.addPassenger(item);
            return item;
        });
    }


    @SuppressWarnings("DataFlowIssue")
    public PropellerComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
        angle = reader.get("angle", Double.class);
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("angle", angle);
    }

    @Override
    public void update(@NotNull VehicleEntity vehicleEntity) {
        super.update(vehicleEntity);

        for (String fuel : schema().fuels) {
            if (vehicleEntity.remainingResource(fuel) <= 0) {
                return;
            }
        }

        double throttleFraction = vehicleEntity.getThrottle() / 100.0;

        angle += throttleFraction * schema().getMaxRotationRate();
        angle %= 2.0 * PI;
    }

    @Override
    public @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity) {
        Vector3d extraRotation = new Quaterniond()
                .fromAxisAngleRad(new Vector3d(schema().getRotationAxis()), angle)
                .getEulerAnglesXYZ(new Vector3d());
        return schema().modelItem(vehicleEntity, extraRotation, new Vector3d());
    }

    @Override
    public @NotNull Set<VehicleSurface> getSurfaces() {
        return new HashSet<>();
    }
}
