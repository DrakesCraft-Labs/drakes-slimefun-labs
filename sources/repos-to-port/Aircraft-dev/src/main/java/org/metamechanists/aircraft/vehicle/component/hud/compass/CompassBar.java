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


public class CompassBar extends HudTextComponent<CompassBar.CompassBarSchema> {
    public static class CompassBarSchema extends HudTextComponentSchema {
        private final TextColor majorColor;
        private final TextColor minorColor;
        private final TextColor detailColor;
        private final String majorText;
        private final String minorText;
        private final String detailText;
        private final double majorSize;
        private final double minorSize;
        private final double detailSize;

        public CompassBarSchema(@NotNull String id, @NotNull HudSection.HudSectionSchema sectionSchema, @NotNull YamlTraverser traverser) {
            super(id + "_compass_bar", sectionSchema, EntityType.TEXT_DISPLAY, traverser, CompassBar.class);
            majorColor = traverser.getTextColor("majorColor");
            minorColor = traverser.getTextColor("minorColor");
            detailColor = traverser.getTextColor("detailColor");
            majorText = traverser.get("majorText");
            minorText = traverser.get("minorText");
            detailText = traverser.get("detailText");
            majorSize = traverser.get("majorSize");
            minorSize = traverser.get("minorSize");
            detailSize = traverser.get("detailSize");
        }
    }

    private final int index;

    public CompassBar(@NotNull CompassBar.CompassBarSchema schema, @NotNull VehicleEntity vehicleEntity, int index) {
        super(schema, vehicleEntity);
        this.index = index;
    }

    @SuppressWarnings("DataFlowIssue")
    public CompassBar(@NotNull StateReader reader, TextDisplay textDisplay) {
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

        String text;
        TextColor color;
        double size;

        if (index % 30 == 0) {
            text = schema().majorText;
            color = schema().majorColor;
            size = schema().majorSize;
        } else if (index % 10 == 0) {
            text = schema().minorText;
            color = schema().minorColor;
            size = schema().minorSize;
        } else {
            text = schema().detailText;
            color = schema().detailColor;
            size = schema().detailSize;
        }

        setVisible(Math.abs(totalAdjustment.length()) < Compass.RADIUS);

        return schema().getSectionSchema().rollText(vehicleEntity)
                .text(Component.text(text).color(color))
                .translate(totalAdjustment)
                .translate(0.0F, 0.0F, -0.01F)
                .scale(new Vector3f((float) size, (float) size, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}

