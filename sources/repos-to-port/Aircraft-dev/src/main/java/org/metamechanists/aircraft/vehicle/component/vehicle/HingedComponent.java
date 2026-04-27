package org.metamechanists.aircraft.vehicle.component.vehicle;

import lombok.Getter;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.aircraft.utils.Utils;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleSurface;
import org.metamechanists.aircraft.vehicle.component.base.ItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.VehicleComponent;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.List;
import java.util.Set;

import static java.lang.Math.sin;


public class HingedComponent extends VehicleComponent<HingedComponent.HingedComponentSchema> {
    @Getter
    public static class HingedComponentSchema extends VehicleComponentSchema {
        private final Vector3d rotationAxis;
        private final double rotationRate;
        private final double rotationMax;
        private final List<String> signalsUp;
        private final List<String> signalsDown;

        @SuppressWarnings("DataFlowIssue")
        public HingedComponentSchema(
                @NotNull String id,
                @NotNull YamlTraverser traverser,
                @NotNull YamlTraverser hingedTraverser,
                @NotNull Vector3f translation,
                boolean mirror
        ) {
            super(id, HingedComponent.class, traverser, translation, mirror);

            rotationAxis = hingedTraverser.getVector3d("rotationAxis");
            rotationRate = hingedTraverser.get("rotationRate");
            rotationMax = hingedTraverser.get("rotationMax");
            signalsUp = hingedTraverser.get("signalsUp");
            signalsDown = hingedTraverser.get("signalsDown");
        }

        @Override
        public ItemComponent<?> build(@NotNull VehicleEntity vehicleEntity) {
            return new HingedComponent(this, vehicleEntity);
        }
    }

    private double angle;
    private int ticksUntilReturn;

    @SuppressWarnings("WeakerAccess")
    public HingedComponent(@NotNull HingedComponent.HingedComponentSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, () -> {
            Pig pig = vehicleEntity.entity();
            ItemDisplay item = schema.modelItem(vehicleEntity, new Vector3d(), new Vector3d()).build(pig.getLocation());
            pig.addPassenger(item);
            return item;
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public HingedComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
        angle = reader.get("angle", Double.class);
        ticksUntilReturn = reader.get("ticksUntilReturn", Integer.class);
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("angle", angle);
        writer.set("ticksUntilReturn", ticksUntilReturn);
    }

    @Override
    public @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity) {
        Vector3d extraRotation = new Quaterniond()
                .fromAxisAngleRad(new Vector3d(schema().getRotationAxis()), angle)
                .getEulerAnglesXYZ(new Vector3d());
        Vector3d extraTranslation = new Vector3d(0.0, -schema().getSize().x / 2.0, 0.0)
                .mul(sin(angle));
        return schema().modelItem(vehicleEntity, extraRotation, extraTranslation);
    }

    @Override
    public void update(@NotNull VehicleEntity vehicleEntity) {
        super.update(vehicleEntity);

        if (ticksUntilReturn > 0) {
            ticksUntilReturn--;
            return;
        }

        if (Math.abs(angle) < schema().getRotationRate()) {
            angle = 0;
        } else if (angle > 0) {
            angle -= schema().getRotationRate();
        } else if (angle < 0) {
            angle += schema().getRotationRate();
        }
    }

    @Override
    public void onSignal(String signal) {
        double directionUnit ;
        if (schema().getSignalsDown().contains(signal)) {
            directionUnit = -1.0;
        } else if (schema().getSignalsUp().contains(signal)) {
            directionUnit = 1.0;
        } else {
            return;
        }

        angle = Utils.clampToRange(angle + directionUnit * schema().getRotationRate(), -schema().getRotationMax(), schema().getRotationMax());
        ticksUntilReturn = 1;
    }

    @Override
    public @NotNull Set<VehicleSurface> getSurfaces() {
        Vector3d extraRotation = new Quaterniond()
                .fromAxisAngleRad(new Vector3d(schema().getRotationAxis()), angle)
                .getEulerAnglesXYZ(new Vector3d());
        return schema().getSurfaces(extraRotation);
    }
}
