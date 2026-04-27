package org.metamechanists.aircraft.vehicle.component.hud.horizon;

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


public class HorizonBar extends HudTextComponent<HorizonBar.HorizonBarSchema> {
    public static class HorizonBarSchema extends HudTextComponentSchema {
        private final TextColor majorColor;
        private final TextColor minorColor;
        private final TextColor detailColor;
        private final String majorText;
        private final String minorText;
        private final String detailText;

        public HorizonBarSchema(
                @NotNull String id,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser) {
            super(id + "_horizon_bar", sectionSchema, EntityType.TEXT_DISPLAY, traverser, HorizonBar.class);
            majorColor = traverser.getTextColor("majorColor");
            minorColor = traverser.getTextColor("minorColor");
            detailColor = traverser.getTextColor("detailColor");
            majorText = traverser.get("majorText");
            minorText = traverser.get("minorText");
            detailText = traverser.get("detailText");
        }
    }

    private final int index;

    public HorizonBar(@NotNull HorizonBar.HorizonBarSchema schema, @NotNull VehicleEntity vehicleEntity, int index) {
        super(schema, vehicleEntity);
        this.index = index;
    }

    @SuppressWarnings("DataFlowIssue")
    public HorizonBar(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
        index = reader.get("index", Integer.class);
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("index", index);
    }

    @Override
    protected @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity) {
        Vector3f horizonOffset = new Vector3f(0, (float) (0.5 * -vehicleEntity.pitch()), 0);
        float verticalSpacing = 0.25F * (float) ((PI / 1.14) / (Horizon.BARS));
        Vector3f barOffset = new Vector3f(0, verticalSpacing * index, 0);
        Vector3f totalAdjustment = new Vector3f(barOffset).add(horizonOffset);

        String text;
        TextColor color;

        if (index == 0) {
            color = schema().majorColor;
            text = schema().majorText;
        } else if (index % 5 == 0) {
            color = schema().minorColor;
            text = schema().minorText;
        } else {
            color = schema().detailColor;
            text = schema().detailText;
        }

        setVisible(Math.abs(totalAdjustment.length()) < Horizon.RADIUS);

        return schema().getSectionSchema().rollText(vehicleEntity)
                .text(Component.text(text).color(color))
                .translate(totalAdjustment)
                .scale(new Vector3f(0.15F, 0.15F, 0.001F))
                .translate(0.5F, 0.35F, 0.05F);
    }
}
