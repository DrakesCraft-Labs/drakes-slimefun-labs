package org.metamechanists.aircraft.vehicle.component.base;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.metalib.yaml.YamlTraverser;


public abstract class HudTextComponent<T extends HudTextComponent.HudTextComponentSchema> extends TextComponent<T> {
    @Getter
    public abstract static class HudTextComponentSchema extends KinematicEntitySchema {
        private final HudSection.HudSectionSchema sectionSchema;

        protected HudTextComponentSchema(
                @NotNull String id,
                @NotNull HudSection.HudSectionSchema sectionSchema,
                @NotNull EntityType entityType,
                @NotNull YamlTraverser traverser,
                @NotNull Class<? extends KinematicEntity<?, ?>> kinematicClass) {
            super(sectionSchema.idWithoutNamespace() + "_" + id, entityType, kinematicClass);
            this.sectionSchema = sectionSchema;
            register(Aircraft.getInstance());
        }
    }

    protected HudTextComponent(@NotNull T schema, @NotNull VehicleEntity vehicleEntity) {
        super(schema, () -> {
            Pig pig = vehicleEntity.entity();
            TextDisplay text = (TextDisplay) pig.getLocation().getWorld().spawnEntity(pig.getLocation(), EntityType.TEXT_DISPLAY);
            pig.addPassenger(text);
            return text;
        });
    }

    protected HudTextComponent(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
    }
}
