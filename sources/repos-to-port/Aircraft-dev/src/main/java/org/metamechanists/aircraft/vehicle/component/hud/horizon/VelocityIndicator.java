package org.metamechanists.aircraft.vehicle.component.hud.horizon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.component.base.HudTextComponent;
import org.metamechanists.displaymodellib.models.components.ModelText;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;


public class VelocityIndicator extends HudTextComponent<VelocityIndicator.VelocityIndicatorSchema> {
    public static class VelocityIndicatorSchema extends HudTextComponentSchema {
        private final TextColor color;
        private final String text;

        public VelocityIndicatorSchema(@NotNull String id, @NotNull Horizon.HorizonSchema sectionSchema, @NotNull YamlTraverser traverser) {
            super(id + "velocity_indicator", sectionSchema, EntityType.TEXT_DISPLAY, traverser, VelocityIndicator.class);
            color = traverser.getTextColor("velocityIndicatorColor");
            text = traverser.get("velocityIndicatorText");
        }
    }

    public VelocityIndicator(@NotNull VelocityIndicator.VelocityIndicatorSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public VelocityIndicator(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
    }

    @Override
    protected @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity) {
        float offset = (float) (0.5 * vehicleEntity.getVelocityPitch()) + Horizon.HorizonSchema.offset(vehicleEntity);
        return schema().getSectionSchema().rollIndependentText(vehicleEntity)
                .text(Component.text(schema().text).color(schema().color))
                .translate(new Vector3f(0, offset, 0))
                .scale(new Vector3f(0.15F, 0.15F, 0.001F))
                .translate(0.5F, 0.35F, 0.06F);
    }
}
