package org.metamechanists.aircraft.vehicle.component.hud.compass;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.component.base.HudSection;
import org.metamechanists.aircraft.vehicle.component.base.HudTextComponent;
import org.metamechanists.displaymodellib.models.components.ModelText;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;
import org.metamechanists.metalib.yaml.YamlTraverser;

import static java.lang.Math.PI;


public class CompassDegree extends HudTextComponent<CompassDegree.CompassDegreeSchema> {
    public static class CompassDegreeSchema extends HudTextComponentSchema {
        private final TextColor color;
        private final double size;
        private final double offset;

        public CompassDegreeSchema(
                @NotNull String id,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser) {
            super(id + "_compass_degree", sectionSchema, EntityType.TEXT_DISPLAY, traverser, CompassDegree.class);
            color = traverser.getTextColor("degreeColor");
            size = traverser.get("degreeSize");
            offset = traverser.get("degreeOffset");
        }
    }

    private final int index;

    public CompassDegree(@NotNull CompassDegree.CompassDegreeSchema schema, @NotNull VehicleEntity vehicleEntity, int index) {
        super(schema, vehicleEntity);
        this.index = index;
    }

    @SuppressWarnings("DataFlowIssue")
    public CompassDegree(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
        index = reader.get("index", Integer.class);
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("index", index);
    }

    @Override
    protected @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity) {
        float compassOffset = (float) (0.5 * vehicleEntity.yaw());
        float horizontalSpacing = 0.5F * (float) (PI / (Compass.BARS));
        float barOffset = horizontalSpacing * index;
        Vector3f totalAdjustment = new Vector3f(barOffset + compassOffset, 0, 0);

        int degrees = (index + 60) * 3 - 90;
        if (degrees < 0) {
            degrees += 360;
        }

        setVisible(Math.abs(totalAdjustment.length()) < Compass.RADIUS);

        return schema().getSectionSchema().rollText(vehicleEntity)
                .text(Component.text(degrees).color(schema().color))
                .translate(totalAdjustment)
                .translate(0.0F, schema().offset, -0.01F)
                .scale(new Vector3f((float) schema().size, (float) schema().size, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}

