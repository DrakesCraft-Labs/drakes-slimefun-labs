package org.metamechanists.aircraft.vehicle;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.yaml.YamlTraverser;

import java.util.Map;


@Accessors(fluent = true)
@Getter
public class VehicleResource {
    private final double capacity;
    private final double passiveEngineDrain;
    private final double throttleDrain;
    private final ResourceType type;
    private final char guiKey;

    public enum ResourceType {
        COMBUSTIBLE,
        WATER;

        public String color() {
            return switch (this) {
                case COMBUSTIBLE -> "<color:#e08523>";
                case WATER -> "<color:#237aed>";
            };
        }

        public Material icon() {
            return switch (this) {
                case COMBUSTIBLE -> Material.CHARCOAL;
                case WATER -> Material.WATER_BUCKET;
            };
        }

        public Map<Material, Double> acceptedFuels() {
            return switch (this) {
                case COMBUSTIBLE -> Map.of(
                        Material.CHARCOAL, 50.0,
                        Material.COAL, 50.0
                );
                case WATER -> Map.of(
                    Material.WATER_BUCKET, 1000.0
                );
            };
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public VehicleResource(@NotNull YamlTraverser traverser) {
        type = ResourceType.valueOf(traverser.get("type"));
        capacity = traverser.get("capacity");
        passiveEngineDrain = traverser.get("passiveEngineDrain");
        throttleDrain = traverser.get("throttleDrain");
        guiKey = traverser.getChar("guiKey");
    }

    public double drainedThisTick(@NotNull VehicleEntity vehicleEntity) {
        double total = vehicleEntity.getThrottle() * throttleDrain / 100.0;
        if (vehicleEntity.isEngineOn()) {
            total += passiveEngineDrain;
        }
        return total;
    }
}
