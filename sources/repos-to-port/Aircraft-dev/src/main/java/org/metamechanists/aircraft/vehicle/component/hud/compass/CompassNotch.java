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
import org.metamechanists.metalib.yaml.YamlTraverser;


public class CompassNotch extends HudTextComponent<CompassNotch.CompassNotchSchema> {
    public static class CompassNotchSchema extends HudTextComponentSchema {
        private final TextColor color;
        private final String text;
        private final double size;
        private final double offset;

        public CompassNotchSchema(@NotNull String id, @NotNull HudSection.HudSectionSchema sectionSchema, @NotNull YamlTraverser traverser) {
            super(id + "_compass_notch", sectionSchema, EntityType.TEXT_DISPLAY, traverser, CompassNotch.class);
            color = traverser.getTextColor("notchColor");
            text = traverser.get("notchText");
            size = traverser.get("notchSize");
            offset = traverser.get("notchOffset");
        }
    }

    public CompassNotch(@NotNull CompassNotch.CompassNotchSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public CompassNotch(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
    }

    @Override
    protected @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity) {
        return schema().getSectionSchema().rollText(vehicleEntity)
                .text(Component.text(schema().text).color(schema().color))
                .translate(0.0F, schema().offset, -0.01F)
                .scale(new Vector3f((float) schema().size, (float) schema().size, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}
