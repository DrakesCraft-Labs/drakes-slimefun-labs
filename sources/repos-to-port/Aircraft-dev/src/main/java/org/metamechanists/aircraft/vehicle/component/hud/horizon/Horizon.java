package org.metamechanists.aircraft.vehicle.component.hud.horizon;

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


public class Horizon extends HudSection<Horizon.HorizonSchema> {
    public static final int BARS = 30;
    public static final float RADIUS = 0.15F;

    public static class HorizonSchema extends HudSection.HudSectionSchema {
        private final AltitudeText.AltitudeTextSchema altitudeTextSchema;
        private final AltitudeBrackets.AltitudeBracketsSchema altitudeBracketsSchema;
        private final VelocityIndicator.VelocityIndicatorSchema velocityIndicatorSchema;
        private final HorizonBar.HorizonBarSchema horizonBarSchema;
        private final HorizonDegree.HorizonDegreeSchema horizonDegreeSchema;

        public HorizonSchema(@NotNull String id, @NotNull YamlTraverser traverser) {
            super(id, EntityType.INTERACTION, traverser, Horizon.class);
            altitudeTextSchema = new AltitudeText.AltitudeTextSchema(id, this, traverser);
            altitudeBracketsSchema = new AltitudeBrackets.AltitudeBracketsSchema(id, this, traverser);
            velocityIndicatorSchema = new VelocityIndicator.VelocityIndicatorSchema(id, this, traverser);
            horizonBarSchema = new HorizonBar.HorizonBarSchema(id, this, traverser);
            horizonDegreeSchema = new HorizonDegree.HorizonDegreeSchema(id, this, traverser);
        }

        public static float offset(@NotNull VehicleEntity vehicleEntity) {
            return (float) (0.5F * -vehicleEntity.pitch());
        }
    }

    public Horizon(@NotNull HorizonSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public Horizon(@NotNull StateReader reader, Interaction interaction) {
        super(reader, interaction);
    }

    @Override
    protected List<UUID> buildComponents(@NotNull VehicleEntity vehicleEntity) {
        List<UUID> components = new ArrayList<>();
        components.add(new AltitudeText(schema().altitudeTextSchema, vehicleEntity).uuid());
        components.add(new AltitudeBrackets(schema().altitudeBracketsSchema, vehicleEntity).uuid());
        components.add(new VelocityIndicator(schema().velocityIndicatorSchema, vehicleEntity).uuid());

        for (int i = -BARS; i <= BARS; i++) {
            components.add(new HorizonBar(schema().horizonBarSchema, vehicleEntity, i).uuid());
            if (i != 0 && i % 5 == 0) {
                components.add(new HorizonDegree(schema().horizonDegreeSchema, vehicleEntity, i).uuid());
            }
        }

        return components;
    }
}
