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


public class HorizonDegree extends HudTextComponent<HorizonDegree.HorizonDegreeSchema> {
    public static class HorizonDegreeSchema extends HudTextComponentSchema {
        private final TextColor color;

        public HorizonDegreeSchema(@NotNull String id, @NotNull HudSection.HudSectionSchema sectionSchema, @NotNull YamlTraverser traverser) {
            super(id + "horizon_degree", sectionSchema, EntityType.TEXT_DISPLAY, traverser, HorizonDegree.class);
            color = traverser.getTextColor("horizonDegreeColor");
        }
    }

    private final int index;

    public HorizonDegree(@NotNull HorizonDegreeSchema schema, @NotNull VehicleEntity vehicleEntity, int index) {
        super(schema, vehicleEntity);
        this.index = index;
    }

    @SuppressWarnings("DataFlowIssue")
    public HorizonDegree(@NotNull StateReader reader, TextDisplay textDisplay) {
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
        Vector3f degreeOffset = new Vector3f(0, verticalSpacing * index, 0);
        Vector3f totalAdjustment = new Vector3f(degreeOffset).add(horizonOffset).add(0.1F, 0.0F, 0.0F);

        setVisible(Math.abs(totalAdjustment.length()) < Horizon.RADIUS);

        return schema().getSectionSchema().rollIndependentText(vehicleEntity)
                .text(Component.text(index * (90 / (Horizon.BARS-1))).color(schema().color))
                .translate(totalAdjustment)
                .scale(new Vector3f(0.1F, 0.1F, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}
