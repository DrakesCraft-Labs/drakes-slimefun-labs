package org.metamechanists.aircraft.vehicle;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.utils.Utils;
import org.metamechanists.aircraft.vehicle.component.Protector;
import org.metamechanists.aircraft.vehicle.component.base.ItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.VehicleComponent;
import org.metamechanists.aircraft.vehicle.component.hud.bottompanel.BottomPanel;
import org.metamechanists.aircraft.vehicle.component.hud.compass.Compass;
import org.metamechanists.aircraft.vehicle.component.hud.horizon.Horizon;
import org.metamechanists.aircraft.vehicle.component.vehicle.ThrusterComponent;
import org.metamechanists.aircraft.vehicle.forces.SpatialForce;
import org.metamechanists.aircraft.vehicle.forces.SpatialForceType;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@SuppressWarnings({"WeakerAccess", "OverlyComplexClass"})
@Getter
public class VehicleEntity extends KinematicEntity<Pig, VehicleEntitySchema> {
    public static final int TICK_INTERVAL = 2;
    private static final int PHYSICS_INTERVAL = 1;
    private static final int PHYSICS_UPDATES_PER_SECOND = 20 / PHYSICS_INTERVAL;
    private static final int PHYSICS_UPDATES_PER_AIRCRAFT_UPDATE = TICK_INTERVAL / PHYSICS_INTERVAL;
    private static final int MIN_THROTTLE = 0;
    private static final int MAX_THROTTLE = 100;
    private static final int THROTTLE_INCREMENT = 5;

    private @Nullable UUID pilot;
    private int throttle;
    private final List<String> signalsThisTick;
    private final Map<String, Double> resources;
    private final Vector3d velocity;
    private final Quaterniond rotation;
    private final Vector3d angularVelocity;
    private final Map<String, UUID> components;
    private final Set<UUID> itemDisplays;
    private final Set<UUID> textDisplays;
    private @Nullable UUID interactor;
    private @Nullable UUID horizon;
    private @Nullable UUID compass;
    private @Nullable UUID bottomPanel;

    public VehicleEntity(@NotNull VehicleEntitySchema schema, @NotNull Block block, @NotNull Player player) {
        super(schema, () -> {
            Location location = block.getLocation().clone().toCenterLocation().add(new Vector(0, 0.5, 0));
            Pig pig = (Pig) block.getWorld().spawnEntity(location, EntityType.PIG);
            pig.setInvulnerable(true);
            pig.setGravity(false);
            pig.setInvisible(true);
            pig.setSilent(true);
            Protector.protect(pig);
            return pig;
        });

        throttle = 0;
        signalsThisTick = new ArrayList<>();
        resources = new HashMap<>();
        for (Map.Entry<String, VehicleResource> pair : schema.getResources().entrySet()) {
            resources.put(pair.getKey(), 0.0);
        }
        velocity = new Vector3d();
        rotation = new Quaterniond().rotationY(Math.toRadians(-90.0-player.getEyeLocation().getYaw()));
        angularVelocity = new Vector3d();
        components = new HashMap<>();
        itemDisplays = new HashSet<>();
        textDisplays = new HashSet<>();
        interactor = new VehicleInteractor(this).uuid();
        if (schema.getHorizonSchema() != null) {
            horizon = new Horizon(schema.getHorizonSchema(), this).uuid();
        }
        if (schema.getCompassSchema() != null) {
            compass = new Compass(schema.getCompassSchema(), this).uuid();
        }
        if (schema.getBottomPanelSchema() != null) {
            bottomPanel = new BottomPanel(schema.getBottomPanelSchema(), this).uuid();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public VehicleEntity(StateReader reader, Pig pig) {
        super(reader, pig);
        throttle = reader.get("throttle", Integer.class);
        signalsThisTick = reader.get("signalsThisTick", new ArrayList<>());
        resources = reader.get("resources", new HashMap<>());
        velocity = reader.get("velocity", Vector3d.class);
        rotation = reader.get("rotation", Quaterniond.class);
        angularVelocity = reader.get("angularVelocity", Vector3d.class);
        interactor = reader.get("interaction", UUID.class);
        components = reader.get("components", new HashMap<>());
        itemDisplays = reader.get("itemDisplays", new HashSet<>());
        textDisplays = reader.get("textDisplays", new HashSet<>());
        horizon = reader.get("horizon", UUID.class);
        compass = reader.get("compass", UUID.class);
        bottomPanel = reader.get("bottomPanel", UUID.class);

        Bukkit.getScheduler().runTask(Aircraft.getInstance(), () -> {
            // Fixes an edge case when a player rejoins and loads the chunk, where kinematic entities
            // do not finish loading when the Mount event iscalled, meaning KinematicEntity.get()
            // returns null and the onMount() method is never called
            //noinspection VariableNotUsedInsideIf
            if (pilot != null) {
                return;
            }

            Protector.protect(pig);
            for (Entity entity : pig.getPassengers()) {
                if (entity instanceof Player player) {
                    onMount(player);
                }
            }
        });
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("throttle", throttle);
        writer.set("signalsThisTick", signalsThisTick);
        writer.set("resources", resources);
        writer.set("velocity", velocity);
        writer.set("rotation", rotation);
        writer.set("angularVelocity", angularVelocity);
        writer.set("interaction", interactor);
        writer.set("components", components);
        writer.set("itemDisplays", itemDisplays);
        writer.set("textDisplays", textDisplays);
        writer.set("horizon", horizon);
        writer.set("compass", compass);
        writer.set("bottomPanel", bottomPanel);
    }

    @Override
    public void tick(@NotNull Pig pig, long tick) {
        if (tick % TICK_INTERVAL != 0) {
            return;
        }

        Map<String, VehicleComponent.VehicleComponentSchema> expectedComponents = schema().getComponents();

        // Remove any additional vehicle components
        List<String> toRemove = components.keySet()
                .stream()
                .filter(id -> !expectedComponents.containsKey(id))
                .toList();
        for (String id : toRemove) {
            KinematicEntity<?, ?> kinematicEntity = KinematicEntity.get(components.remove(id));
            if (kinematicEntity != null) {
                kinematicEntity.entity().remove();
            }
        }

        // Add any new vehicle components
        //noinspection KeySetIterationMayUseEntrySet
        for (String id : expectedComponents.keySet()) {
            if (!components.containsKey(id)) {
                components.put(id, expectedComponents.get(id).build(this).uuid());
            }
        }

        // Do physics update (before component updates for immediate effect)
        for (int i = 0; i < PHYSICS_UPDATES_PER_AIRCRAFT_UPDATE; i++) {
            updatePhysics(pig);
        }

        // Components
        for (UUID uuid : components.values()) {
            if (KinematicEntity.get(uuid) instanceof ItemComponent<?> component) {
                component.update(this);
            }
        }

        // Resources
        for (String resource : resources.keySet()) {
            double drainedThisTick = schema().getResources().get(resource).drainedThisTick(this);
            drainResource(resource, drainedThisTick);
        }

        // HUD
        //noinspection VariableNotUsedInsideIf
        if (pilot != null) {
            updateHud();
        }

        // Interactor
        KinematicEntity<?, ?> interactor = KinematicEntity.get(this.interactor);
        if (interactor == null && pilot == null) {
            this.interactor = new VehicleInteractor(this).uuid();
        } else if (interactor != null && pilot != null) {
            interactor.entity().remove();
        }

        // Engine
        if (!isEngineOn()) {
            throttle = 0;
        }

        // Signals
        signalsThisTick.clear();
        onSignal("TICK");
        //noinspection VariableNotUsedInsideIf
        if (pilot != null) {
            onSignal("HAS_PILOT_TICK");
        }
        if (isEngineOn()) {
            onSignal("ENGINE_TICK");
        }

        // Pig velocity
        Vector3d pigVelocityJoml = absoluteVelocity().div(PHYSICS_UPDATES_PER_SECOND);
        if (pigVelocityJoml.length() > 1.0e2) {
            pigVelocityJoml = new Vector3d(0.001, 0.0, 0.0);
        }
        Vector pigVelocity = Vector.fromJOML(pigVelocityJoml);
        pig.setVelocity(pigVelocity);
    }

    private void updateHud() {
        if (horizon != null) {
            if (KinematicEntity.get(horizon) instanceof Horizon horizon) {
                horizon.update(this, pilotAsPlayer());
            }
        }
        if (compass != null) {
            if (KinematicEntity.get(compass) instanceof Compass compass) {
                compass.update(this, pilotAsPlayer());
            }
        }
        if (bottomPanel != null) {
            if (KinematicEntity.get(bottomPanel) instanceof BottomPanel bottomPanel) {
                bottomPanel.update(this, pilotAsPlayer());
            }
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (horizon != null) {
            if (KinematicEntity.get(horizon) instanceof Horizon horizon) {
                horizon.entity().remove();
                horizon.onRemove();
            }
        }
        if (compass != null) {
            if (KinematicEntity.get(compass) instanceof Compass compass) {
                compass.entity().remove();
                compass.onRemove();
            }
        }
        if (bottomPanel != null) {
            if (KinematicEntity.get(bottomPanel) instanceof BottomPanel bottomPanel) {
                bottomPanel.entity().remove();
                bottomPanel.onRemove();
            }
        }

        for (UUID uuid : components.values()) {
            if (KinematicEntity.get(uuid) instanceof ItemComponent<?> component) {
                component.entity().remove();
                component.onRemove();
            }
        }

        if (KinematicEntity.get(interactor) instanceof VehicleInteractor vehicleInteractor) {
            vehicleInteractor.entity().remove();
        }
    }

    public boolean isEngineOn() {
        return pilot != null && resources.keySet()
                .stream()
                .noneMatch(resource -> resources.get(resource) <= 0);
    }

    public @Nullable Player pilotAsPlayer() {
        if (pilot == null) {
            return null;
        }
        return Bukkit.getPlayer(pilot);
    }

    public void onSignal(String signal) {
        signalsThisTick.add(signal);

        Pig pig = entity();

        if (Objects.equals(signal, "THROTTLE_UP")) {
            throttle = Math.min(MAX_THROTTLE, throttle + THROTTLE_INCREMENT);
        }

        if (Objects.equals(signal, "THROTTLE_DOWN")) {
            throttle = Math.max(MIN_THROTTLE, throttle - THROTTLE_INCREMENT);
        }

        if (Objects.equals(signal, "STEER_RIGHT") && isOnGround(pig)) {
            angularVelocity.y -= schema().getSteeringSpeed();
        }

        if (Objects.equals(signal, "STEER_LEFT") && isOnGround(pig)) {
            angularVelocity.y += schema().getSteeringSpeed();
        }

        for (UUID uuid : components.values()) {
            if (KinematicEntity.get(uuid) instanceof VehicleComponent<?> component) {
                component.onSignal(signal);
            }
        }
    }

    public int signalCountThisTick(String signal) {
        return signalsThisTick.stream()
                .filter(s -> s.equals(signal))
                .toList()
                .size();
    }

    public double remainingResource(String resource) {
        return resources.get(resource);
    }

    public void drainResource(String resource, double drainAmount) {
        double newAmount = Math.max(0.0, resources.get(resource) - drainAmount);
        resources.put(resource, newAmount);
    }

    @SuppressWarnings("Convert2streamapi")
    public Set<VehicleSurface> getSurfaces() {
        Set<VehicleSurface> surfaces = new HashSet<>();
        for (UUID uuid : components.values()) {
            if (KinematicEntity.get(uuid) instanceof VehicleComponent<?> component) {
                surfaces.addAll(component.getSurfaces());
            }
        }
        return surfaces;
    }

    public static boolean isOnGround(@NotNull Pig pig) {
        return pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(0.0, -0.1, 0.0)));
    }

    private void updatePhysics(Pig pig) {
        boolean isOnGround = isOnGround(pig);

        Set<SpatialForce> forces = getForces(isOnGround);

        velocity.mul(1.0 - schema().getVelocityDamping());
        Vector3d acceleration = acceleration(forces);
        velocity.add(new Vector3d(acceleration).div(PHYSICS_UPDATES_PER_SECOND));
        cancelVelocityAndAcceleration(pig, velocity);

        angularVelocity.mul(1.0 - schema().getAngularVelocityDamping());
        Vector3d angularAcceleration = angularAcceleration(forces);
        if (isOnGround) {
            angularAcceleration.x -= schema().getGroundRollDamping() * roll();
            angularAcceleration.z -= schema().getGroundPitchDamping() * pitch();
        }
        angularVelocity.add(new Vector3d(angularAcceleration).div(PHYSICS_UPDATES_PER_SECOND));
        if (angularVelocity.length() > 0.00001) { // Check to avoid dividing by 0 in normalize
            rotation.rotateAxis(angularVelocity.length() / PHYSICS_UPDATES_PER_SECOND, new Vector3d(angularVelocity).normalize());
        }
    }

    public Vector3d acceleration(@NotNull Set<SpatialForce> forces) {
        Vector3d resultantForce = new Vector3d();
        forces.stream().map(SpatialForce::force).forEach(resultantForce::add);
        return new Vector3d(resultantForce).div(schema().getMass());
    }

    public Vector3d angularAcceleration(@NotNull Set<SpatialForce> forces) {
        Set<Vector3d> torqueVectors = forces.stream().map(SpatialForce::getTorqueVector).collect(Collectors.toSet());
        Vector3d resultantTorque = new Vector3d();
        torqueVectors.forEach(resultantTorque::add);

        return new Vector3d(resultantTorque).div(schema().getMomentOfInertia());
    }

    public void cancelVelocityAndAcceleration(@NotNull Pig pig, @NotNull Vector3d velocity) {
        velocity.rotate(rotation);

        if (pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(-0.1, 0.0, 0.0)))) {
            velocity.x = Math.max(velocity.x, 0.0);
        }

        if (pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(0.1, 0.0, 0.0)))) {
            velocity.x = Math.min(velocity.x, 0.0);
        }

        if (pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(0.0, -0.1, 0.0)))) {
            velocity.y = Math.max(velocity.y, 0.0);
        }

        if (pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(0.0, 0.1, 0.0)))) {
            velocity.y = Math.min(velocity.y, 0.0);
        }

        if (pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(0.0, 0.0, -0.1)))) {
            velocity.z = Math.max(velocity.z, 0.0);
        }

        if (pig.wouldCollideUsing(pig.getBoundingBox().shift(new Vector(0.0, 0.0, 0.1)))) {
            velocity.z = Math.min(velocity.z, 0.0);
        }

        velocity.rotate(new Quaterniond(rotation).invert());
    }

    public @NotNull Set<SpatialForce> getForces(boolean isOnGround) {
        Set<SpatialForce> forces = new HashSet<>();
        forces.add(getWeightForce());
        forces.add(getEngineForce());
        forces.addAll(getThrusterForces());
        forces.addAll(getDragForces());
        forces.addAll(getLiftForces());
        forces.add(getFrictionForce(isOnGround, acceleration(forces).mul(schema().getMass())));
        return forces;
    }

    private @NotNull SpatialForce getWeightForce() {
        Vector3d force = Utils.rotateBackwards(new Vector3d(0, schema().getGravityAcceleration() * schema().getMass(), 0), rotation);
        Vector3d location = schema().getWeightLocation();
        return new SpatialForce(SpatialForceType.WEIGHT, force, location);
    }

    private @NotNull SpatialForce getEngineForce() {
        double throttleFraction = throttle / 100.0;
        Vector3d force = new Vector3d(schema().getEngineForce()).mul(throttleFraction);
        Vector3d location = schema().getEngineLocation();
        return new SpatialForce(SpatialForceType.ENGINE, force, location);
    }

    @SuppressWarnings("Convert2streamapi")
    private @NotNull Set<SpatialForce> getThrusterForces() {
        Set<SpatialForce> set = new HashSet<>();
        for (UUID uuid : components.values()) {
            KinematicEntity<?, ?> kinematicEntity = KinematicEntity.get(uuid);
            if (kinematicEntity instanceof ThrusterComponent thruster) {
                set.add(thruster.force());
            }
        }
        return set;
    }

    @SuppressWarnings("Convert2streamapi")
    private @NotNull Set<SpatialForce> getDragForces() {
        Set<SpatialForce> set = new HashSet<>();
        for (VehicleSurface vehicleSurface : getSurfaces()) {
            set.add(vehicleSurface.getDragForce(schema().getAirDensity(), velocity, angularVelocity));
        }
        return set;
    }

    @SuppressWarnings("Convert2streamapi")
    private @NotNull Set<SpatialForce> getLiftForces() {
        Set<SpatialForce> set = new HashSet<>();
        for (VehicleSurface vehicleSurface : getSurfaces()) {
            set.add(vehicleSurface.getLiftForce(schema().getAirDensity(), velocity, angularVelocity));
        }
        return set;
    }

    private @NotNull SpatialForce getFrictionForce(boolean isOnGround, Vector3d force) {
        if (!isOnGround || velocity.length() < 0.0001) {
            return new SpatialForce(SpatialForceType.FRICTION, new Vector3d(), new Vector3d());
        }

        double horizontalForce = new Vector3d(velocity.x, 0.0, velocity.z)
                .mul(schema().getMass())
                .length();
        double horizontalVelocity = new Vector3d(velocity.x, 0.0, velocity.z)
                .length();

        double frictionAmount = Math.abs(force.y) * schema().getFrictionCoefficient();
        if (horizontalVelocity < 0.01) {
            // Stationary; limiting equilibrium
            frictionAmount = Math.min(frictionAmount, horizontalForce);
        }

        Vector3d frictionForce = new Vector3d(velocity)
                .normalize()
                .mul(-frictionAmount);
        return new SpatialForce(SpatialForceType.FRICTION, frictionForce, new Vector3d());
    }

    public Vector3d absoluteVelocity() {
        return new Vector3d(velocity).rotate(rotation);
    }

    public double getVelocityPitch() {
        Vector3d absoluteVelocity = absoluteVelocity();
        double pitch = absoluteVelocity.angle(new Vector3d(absoluteVelocity.x, 0, absoluteVelocity.z));
        return absoluteVelocity.y > 0 ? pitch : -pitch;
    }

    // https://stackoverflow.com/questions/5782658/extracting-yaw-from-a-quaternion
    public double roll() {
        Vector3d lookingAtSide = new Vector3d(0, 0, 1).rotate(rotation);
        double roll = lookingAtSide.angle(new Vector3d(lookingAtSide.x, 0, lookingAtSide.z));
        return lookingAtSide.y > 0 ? -roll : roll;
    }

    public double pitch() {
        Vector3d lookingAtForward = new Vector3d(1, 0, 0).rotate(rotation);
        double pitch = lookingAtForward.angle(new Vector3d(lookingAtForward.x, 0, lookingAtForward.z));
        return lookingAtForward.y > 0 ? pitch : -pitch;
    }

    public double yaw() {
        Vector3d lookingAtForward = new Vector3d(1, 0, 0).rotate(rotation);
        double yaw = new Vector3d(lookingAtForward.x, 0, lookingAtForward.z).angle(new Vector3d(1, 0, 0));
        return lookingAtForward.z > 0 ? -yaw : yaw;
    }

    public int altitude() {
        return entity().getLocation().getBlockY();
    }

    public boolean canBecomePilot(@NotNull Player player) {
        return pilot == null;
    }

    public void onMount(@NotNull Player player) {
        pilot = player.getUniqueId();
        player.setInvisible(true);
    }

    public void onUnmount(@NotNull Player player) {
        pilot = null;
        player.setInvisible(false);
        updateHud();
    }

    public boolean canPickUp(@NotNull Player player) {
        return pilot == null;
    }

    public void pickUp() {
        Location location = entity().getLocation();
        location.getWorld().dropItemNaturally(location, schema().getItemStack());
        entity().remove();
        onRemove();
    }
}
