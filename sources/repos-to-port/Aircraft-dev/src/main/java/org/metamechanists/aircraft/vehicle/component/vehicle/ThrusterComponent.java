package org.metamechanists.aircraft.vehicle.component.vehicle;

import com.destroystokyo.paper.ParticleBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pig;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.aircraft.utils.Utils;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleSurface;
import org.metamechanists.aircraft.vehicle.component.base.ItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.VehicleComponent;
import org.metamechanists.aircraft.vehicle.forces.SpatialForce;
import org.metamechanists.aircraft.vehicle.forces.SpatialForceType;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ThrusterComponent extends VehicleComponent<ThrusterComponent.ThrusterComponentSchema> {
    @Getter
    public static class ThrusterComponentSchema extends VehicleComponentSchema {
        private final List<String> signals;
        private final double thrust;
        private final Vector3d direction;
        private final Particle particle;
        private final double particleSpeed;
        private final Vector3d particleOffset;
        private final List<String> fuels;
        private final Map<String, Double> fuelDrain;

        @SuppressWarnings("DataFlowIssue")
        public ThrusterComponentSchema(
                @NotNull String id,
                @NotNull YamlTraverser traverser,
                @NotNull YamlTraverser thrusterTraverser,
                @NotNull Vector3f translation,
                boolean mirror
        ) {
            super(id, ThrusterComponent.class, traverser, translation, mirror);
            signals = thrusterTraverser.get("signals");
            thrust = thrusterTraverser.get("thrust");
            direction = thrusterTraverser.getVector3d("direction");
            particle = Particle.valueOf(thrusterTraverser.get("particle"));
            particleSpeed = thrusterTraverser.get("particleSpeed");
            particleOffset = thrusterTraverser.getVector3d("particleOffset");
            fuels = thrusterTraverser.get("fuels");
            fuelDrain = new HashMap<>();
            for (YamlTraverser fuelDrainTraverser : thrusterTraverser.getSection("fuelDrain").getSections()) {
                fuelDrain.put(fuelDrainTraverser.name(), fuelDrainTraverser.get("drain"));
            }
        }

        @Override
        public ItemComponent<?> build(@NotNull VehicleEntity vehicleEntity) {
            return new ThrusterComponent(this, vehicleEntity);
        }
    }

    private boolean active;
    private boolean sufficientResourcesCache;

    @SuppressWarnings("WeakerAccess")
    public ThrusterComponent(@NotNull ThrusterComponent.ThrusterComponentSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, () -> {
            Pig pig = vehicleEntity.entity();
            ItemDisplay item = schema.modelItem(vehicleEntity, new Vector3d(), new Vector3d()).build(pig.getLocation());
            pig.addPassenger(item);
            return item;
        });
    }

    public SpatialForce force() {
        double thrust = (active && sufficientResourcesCache) ? schema().thrust : 0;
        Vector3d force = new Vector3d(schema().direction).normalize().mul(thrust);
        Vector3d location = new Vector3d(super.schema().getLocation());
        return new SpatialForce(SpatialForceType.THRUSTER, force, location);
    }

    @SuppressWarnings("DataFlowIssue")
    public ThrusterComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
        active = reader.get("active", Boolean.class);
        sufficientResourcesCache = reader.get("sufficientResourcesCache", Boolean.class);
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("active", active);
        writer.set("sufficientResourcesCache", sufficientResourcesCache);
    }

    @Override
    public void update(@NotNull VehicleEntity vehicleEntity) {
        super.update(vehicleEntity);

        sufficientResourcesCache = true;
        for (String fuel : schema().fuels) {
            if (vehicleEntity.remainingResource(fuel) <= 0) {
                sufficientResourcesCache = false;
            }
        }

        if (!active || !sufficientResourcesCache) {
            return;
        }

        Vector3d absoluteLocation = new Vector3d(schema().getLocation())
                .add(schema().particleOffset)
                .rotate(vehicleEntity.getRotation())
                .add(Utils.PLAYER_HEAD_OFFSET)
                .add(new Vector3d(Utils.RIDING_OFFSET));
        Vector3d absoluteDirection = new Vector3d(schema().direction)
                .rotate(vehicleEntity.getRotation())
                .normalize()
                .mul(-1);

        Pig pig = vehicleEntity.entity();
        Location location = pig.getLocation()
                .add(Vector.fromJOML(absoluteLocation));

        new ParticleBuilder(schema().particle)
                .location(location)
                .count(0)
                .extra(schema().particleSpeed)
                .offset(absoluteDirection.x, absoluteDirection.y, absoluteDirection.z)
                .spawn();

        active = false;

        for (String fuel : schema().fuels) {
            if (schema().fuelDrain.containsKey(fuel)) {
                vehicleEntity.drainResource(fuel, schema().fuelDrain.get(fuel));
            }
        }
    }

    @Override
    public @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity) {
        return schema().modelItem(vehicleEntity, new Vector3d(), new Vector3d());
    }

    @Override
    public @NotNull Set<VehicleSurface> getSurfaces() {
        return new HashSet<>();
    }

    @Override
    public void onSignal(String signal) {
        if (schema().signals.contains(signal)) {
            active = true;
        }
    }
}
