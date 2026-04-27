package org.metamechanists.aircraft.vehicle.component.base;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.utils.Utils;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.VehicleSurface;
import org.metamechanists.aircraft.vehicle.component.vehicle.ThrusterComponent;
import org.metamechanists.aircraft.vehicle.component.vehicle.VisibleComponent;
import org.metamechanists.aircraft.vehicle.forces.AerodynamicCoefficients;
import org.metamechanists.aircraft.vehicle.component.vehicle.FixedComponent;
import org.metamechanists.aircraft.vehicle.component.vehicle.HingedComponent;
import org.metamechanists.aircraft.vehicle.component.vehicle.PropellerComponent;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.displaymodellib.transformations.TransformationMatrixBuilder;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;


public abstract class VehicleComponent<T extends KinematicEntitySchema> extends ItemComponent<T> {
    @Getter
    public abstract static class VehicleComponentSchema extends KinematicEntitySchema {
        private static final double MIN_AREA = 0.01;
        private final Material material;
        private final Vector3f size;
        private final Vector3f location;
        private final Vector3d rotation;
        private @Nullable AerodynamicCoefficients aerodynamicCoefficients;

        @SuppressWarnings("DataFlowIssue")
        protected VehicleComponentSchema(
                @NotNull String id,
                @NotNull Class<? extends KinematicEntity<?, ?>> kinematicClass,
                @NotNull YamlTraverser traverser,
                @NotNull Vector3f translation,
                boolean mirror
        ) {
            super(id + (mirror ? "_mirror" : ""), EntityType.ITEM_DISPLAY, kinematicClass);

            material = Material.valueOf(traverser.get("material"));
            size = traverser.getVector3f("size");
            location = traverser.getVector3f("location").sub(translation);
            rotation = traverser.getVector3d("rotation");

            YamlTraverser aerodynamicSection = traverser.getSection("aerodynamic", YamlTraverser.ErrorSetting.NO_BEHAVIOUR);
            if (aerodynamicSection != null) {
                aerodynamicCoefficients = new AerodynamicCoefficients(
                        aerodynamicSection.get("dragCoefficient"),
                        aerodynamicSection.get("liftCoefficient")
                );
            }

            if (mirror) {
                location.z = -location.z;
                rotation.x = -rotation.x;
                rotation.y = -rotation.y;
            }

            register(Aircraft.getInstance());
        }

        public abstract ItemComponent<?> build(@NotNull VehicleEntity vehicleEntity);

        public ModelItem modelItem(@NotNull VehicleEntity vehicleEntity, @NotNull Vector3d extraRotation, @NotNull Vector3d extraTranslation) {
            return Utils.defaultModelItem(vehicleEntity.getRotation())
                    .material(material)
                    .translate(location)
                    .rotate(rotation)
                    .translate(new Vector3f((float) extraTranslation.x, (float) extraTranslation.y, (float) extraTranslation.z))
                    .rotate(extraRotation)
                    .scale(size);
        }

        @SuppressWarnings("DataFlowIssue")
        private @Nullable VehicleSurface getSurface(
                @NotNull Vector3d startingLocation,
                double surfaceWidth,
                double surfaceHeight,
                @NotNull Vector3d extraRotation
        ) {
            double area = surfaceWidth * surfaceHeight;
            if (area < MIN_AREA) {
                return null;
            }
            Matrix4f rotationMatrix = new TransformationMatrixBuilder().rotate(rotation).rotate(extraRotation).buildForItemDisplay();
            Vector4d relativeLocation4 = new Vector4d(startingLocation, 1.0).mul(rotationMatrix);
            Vector3d relativeLocation = new Vector3d(relativeLocation4.x, relativeLocation4.y, relativeLocation4.z);
            Vector3d normal = new Vector3d(relativeLocation).normalize();
            return new VehicleSurface(aerodynamicCoefficients.dragCoefficient(), aerodynamicCoefficients.liftCoefficient(), area, normal, new Vector3d(location).add(relativeLocation));
        }

        @SuppressWarnings("SuspiciousNameCombination")
        public @NotNull Set<VehicleSurface> getSurfaces(Vector3d extraRotation) {
            if (aerodynamicCoefficients == null) {
                return new HashSet<>();
            }

            Set<VehicleSurface> surfaces = new HashSet<>();

            VehicleSurface surface = getSurface(new Vector3d(0, 0, size.z / 2), size.x, size.y, extraRotation);
            if (surface != null) {surfaces.add(surface);}
            surface = getSurface(new Vector3d(0, 0, -size.z / 2), size.x, size.y, extraRotation);
            if (surface != null) {surfaces.add(surface);}

            surface = getSurface(new Vector3d(0, size.y / 2, 0), size.x, size.z, extraRotation);
            if (surface != null) {surfaces.add(surface);}
            surface = getSurface(new Vector3d(0, -size.y / 2, 0), size.x, size.z, extraRotation);
            if (surface != null) {surfaces.add(surface);}

            surface = getSurface(new Vector3d(size.x / 2, 0, 0), size.y, size.z, extraRotation);
            if (surface != null) {surfaces.add(surface);}
            surface = getSurface(new Vector3d(-size.x / 2, 0, 0), size.y, size.z, extraRotation);
            if (surface != null) {surfaces.add(surface);}

            return surfaces;
        }

        public static @NotNull VehicleComponent.VehicleComponentSchema fromTraverser(
                @NotNull String id, @NotNull YamlTraverser traverser, @NotNull Vector3f translation, boolean mirror) {
            YamlTraverser hingedTraverser = traverser.getSection("hinged", YamlTraverser.ErrorSetting.NO_BEHAVIOUR);
            if (hingedTraverser != null) {
                return new HingedComponent.HingedComponentSchema(id, traverser, hingedTraverser, translation, mirror);
            }

            YamlTraverser propellerTraverser = traverser.getSection("propeller", YamlTraverser.ErrorSetting.NO_BEHAVIOUR);
            if (propellerTraverser != null) {
                return new PropellerComponent.PropellerComponentSchema(id, traverser, propellerTraverser, translation, mirror);
            }

            YamlTraverser thrusterTraverser = traverser.getSection("thruster", YamlTraverser.ErrorSetting.NO_BEHAVIOUR);
            if (thrusterTraverser  != null) {
                return new ThrusterComponent.ThrusterComponentSchema(id, traverser, thrusterTraverser, translation, mirror);
            }

            YamlTraverser visibleTraverser = traverser.getSection("visible", YamlTraverser.ErrorSetting.NO_BEHAVIOUR);
            if (visibleTraverser  != null) {
                return new VisibleComponent.VisibleComponentSchema(id, traverser, visibleTraverser, translation, mirror);
            }

            return new FixedComponent.FixedComponentSchema(id, traverser, translation, mirror);
        }
    }

    protected VehicleComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
    }

    protected VehicleComponent(@NotNull T schema, @NotNull Supplier<ItemDisplay> spawnEntity) {
        super(schema, spawnEntity);
    }

    public abstract @NotNull Set<VehicleSurface> getSurfaces();

    public void onSignal(String signal) {}
}
