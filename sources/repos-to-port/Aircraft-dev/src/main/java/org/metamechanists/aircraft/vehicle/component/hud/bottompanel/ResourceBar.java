package org.metamechanists.aircraft.vehicle.component.hud.bottompanel;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.aircraft.vehicle.component.base.HudItemComponent;
import org.metamechanists.aircraft.vehicle.component.base.HudSection;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;


public class ResourceBar extends HudItemComponent<ResourceBar.ResourceBarSchema> {
    public static class ResourceBarSchema extends HudItemComponentSchema {
        private final String resource;
        private final Material material;
        private final double width;
        private final double height;
        private final double verticalOffset;
        private final double horizontalOffset;

        public ResourceBarSchema(
                @NotNull String id,
                @NotNull String resource,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull YamlTraverser traverser
        ) {
            super(id + "_bottom_panel_" + resource + "_bar", EntityType.ITEM_DISPLAY, sectionSchema, traverser, ResourceBar.class);
            this.resource = resource;
            material = Material.getMaterial(traverser.get("barMaterial"));
            assert material != null;
            height = traverser.get("barHeight");
            width = traverser.get("barWidth");
            verticalOffset = traverser.get("barVerticalOffset");
            horizontalOffset = traverser.get("barHorizontalOffset");
        }
    }

    public ResourceBar(@NotNull ResourceBar.ResourceBarSchema schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, vehicleEntity);
    }

    public ResourceBar(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
    }

    @Override
    protected @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity) {
        double remaining = vehicleEntity.getResources().get(schema().resource);
        double capacity = vehicleEntity.schema().getResources().get(schema().resource).capacity();
        float fraction = (float) (remaining / capacity);
        double offsetX = -0.5F * schema().width + schema().horizontalOffset;
        return schema().getSectionSchema().rollCuboid(vehicleEntity)
                .material(schema().material)
                .translate(offsetX, schema().verticalOffset, 0.001F)
                .scale(new Vector3f((float) schema().width * fraction, (float) schema().height, 0.001F))
                .translate(0.5F, 0.35F, 0.0F);
    }
}
