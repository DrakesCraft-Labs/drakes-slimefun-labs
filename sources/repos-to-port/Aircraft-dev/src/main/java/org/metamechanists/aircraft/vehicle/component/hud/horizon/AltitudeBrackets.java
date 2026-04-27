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
import org.metamechanists.metalib.yaml.YamlTraverser;


public class AltitudeBrackets extends HudTextComponent<AltitudeBrackets.AltitudeBracketsSchema> {
    public static class AltitudeBracketsSchema extends HudTextComponentSchema {
        private final TextColor color;
        private final String text;

        public AltitudeBracketsSchema(
                @NotNull String id,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser) {
            super(id + "_altitude_brackets", sectionSchema, EntityType.TEXT_DISPLAY, traverser, AltitudeBrackets.class);
            color = traverser.getTextColor("altitudeBracketsColor");
            text = traverser.get("altitudeBracketsText");
        }
    }

    public AltitudeBrackets(@NotNull AltitudeBrackets.AltitudeBracketsSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public AltitudeBrackets(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
    }

    @Override
    protected @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity) {
        return schema().getSectionSchema().rollIndependentText(vehicleEntity)
                .text(Component.text(schema().text).color(schema().color))
                .scale(new Vector3f(0.15F, 0.15F, 0.001F))
                .translate(0.5F, 0.35F, 0.06F);
    }
}
