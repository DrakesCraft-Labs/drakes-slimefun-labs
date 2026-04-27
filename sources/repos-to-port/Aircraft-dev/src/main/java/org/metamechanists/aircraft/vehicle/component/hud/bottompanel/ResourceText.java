package org.metamechanists.aircraft.vehicle.component.hud.bottompanel;

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


public class ResourceText extends HudTextComponent<ResourceText.ResourceTextSchema> {
    public static class ResourceTextSchema extends HudTextComponentSchema {
        private final String resource;
        private final TextColor color;
        private final double size;
        private final double verticalOffset;
        private final double horizontalOffset;

        @SuppressWarnings("DataFlowIssue")
        public ResourceTextSchema(
                @NotNull String id,
                @NotNull String resource,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser
        ) {
            super(id + "_bottom_panel_" + resource + "_text", sectionSchema, EntityType.TEXT_DISPLAY, traverser, ResourceText.class);
            this.resource = resource;
            color = traverser.getTextColor("textColor");
            size = traverser.get("textSize");
            verticalOffset = traverser.get("textVerticalOffset");
            horizontalOffset = traverser.get("textHorizontalOffset");
        }
    }

    public ResourceText(@NotNull ResourceText.ResourceTextSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public ResourceText(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
    }

    @Override
    protected @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity) {
        long amount = Math.round(vehicleEntity.getResources().get(schema().resource));
        return schema().getSectionSchema().rollText(vehicleEntity)
                .text(Component.text(amount).color(schema().color))
                .translate(schema().horizontalOffset, schema().verticalOffset, 0.001F)
                .scale(new Vector3f((float) schema().size, (float) schema().size, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}
