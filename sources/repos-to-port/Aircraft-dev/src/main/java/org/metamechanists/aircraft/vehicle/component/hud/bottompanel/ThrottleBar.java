package org.metamechanists.aircraft.vehicle.component.hud.bottompanel;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.component.base.HudItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.HudSection;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;


public class ThrottleBar extends HudItemComponent<ThrottleBar.ThrottleBarSchema> {
    public static class ThrottleBarSchema extends HudItemComponentSchema {
        private final Material material;
        private final double width;
        private final double height;
        private final double verticalOffset;
        private final double horizontalOffset;

        public ThrottleBarSchema(
                @NotNull String id,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser
        ) {
            super(id + "_bottom_panel_throttle_bar", EntityType.ITEM_DISPLAY, sectionSchema, traverser, ThrottleBar.class);
            material = Material.getMaterial(traverser.get("barMaterial"));
            assert material != null;
            height = traverser.get("barHeight");
            width = traverser.get("barWidth");
            verticalOffset = traverser.get("barVerticalOffset");
            horizontalOffset = traverser.get("barHorizontalOffset");
        }
    }

    public ThrottleBar(@NotNull ThrottleBar.ThrottleBarSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public ThrottleBar(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
    }

    @Override
    protected @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity) {
        float fraction = vehicleEntity.getThrottle() / 100.0F;
        double offsetX = -0.5F * schema().width + schema().horizontalOffset;
        return schema().getSectionSchema().rollCuboid(vehicleEntity)
                .material(schema().material)
                .translate(offsetX, schema().verticalOffset, 0.001F)
                .scale(new Vector3f((float) schema().width * fraction, (float) schema().height, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}
