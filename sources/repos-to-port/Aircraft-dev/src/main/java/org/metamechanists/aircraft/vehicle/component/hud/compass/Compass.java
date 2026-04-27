package org.metamechanists.aircraft.vehicle.component.hud.compass;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.component.base.HudSection;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Compass extends HudSection<Compass.CompassSchema> {
    public static final int BARS = 60;
    private static final int EXTRA_BARS = 8;
    public static final float RADIUS = 0.2F;

    public static class CompassSchema extends HudSectionSchema {
        private final CompassBar.CompassBarSchema compassBarSchema;
        private final CompassDegree.CompassDegreeSchema compassDegreeSchema;
        private final CompassDirection.CompassDirectionSchema compassDirectionSchema;
        private final CompassNotch.CompassNotchSchema compassNotchSchema;

        public CompassSchema(@NotNull String id, @NotNull YamlTraverser traverser) {
            super(id, EntityType.INTERACTION, traverser, Compass.class);
            compassBarSchema = new CompassBar.CompassBarSchema(id, this, traverser);
            compassDegreeSchema = new CompassDegree.CompassDegreeSchema(id, this, traverser);
            compassDirectionSchema = new CompassDirection.CompassDirectionSchema(id, this, traverser);
            compassNotchSchema = new CompassNotch.CompassNotchSchema(id, this, traverser);
        }
    }

    public Compass(@NotNull Compass.CompassSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public Compass(@NotNull StateReader reader, Interaction interaction) {
        super(reader, interaction);
    }

    @Override
    protected List<UUID> buildComponents(@NotNull VehicleEntity vehicleEntity) {
        List<UUID> components = new ArrayList<>();
        components.add(new CompassNotch(schema().compassNotchSchema, vehicleEntity).uuid());

        for (int i = -BARS - EXTRA_BARS; i <= BARS + EXTRA_BARS; i++) {
            components.add(new CompassBar(schema().compassBarSchema, vehicleEntity, i).uuid());
            int degrees = i + 420;
            if (degrees % 30 == 0) {
                components.add(new CompassDirection(schema().compassDirectionSchema, vehicleEntity, i).uuid());
            } else if (degrees % 10 == 0) {
                components.add(new CompassDegree(schema().compassDegreeSchema, vehicleEntity, i).uuid());
            }
        }

        return components;
    }
}
