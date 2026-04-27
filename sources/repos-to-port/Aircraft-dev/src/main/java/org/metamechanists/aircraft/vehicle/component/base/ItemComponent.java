package org.metamechanists.aircraft.vehicle.component.base;

import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Supplier;


public abstract class ItemComponent<T extends KinematicEntitySchema> extends KinematicEntity<ItemDisplay, T> {
    private static final double MATRIX_DIFFERENCE_THRESHOLD = 1.0e-4;
    private boolean visible = true;
    private boolean visibleLastUpdate = true;
    private Matrix4f matrixLastUpdate = new Matrix4f();

    protected ItemComponent(@NotNull StateReader reader, ItemDisplay itemDisplay) {
        super(reader, itemDisplay);
    }

    protected ItemComponent(@NotNull T schema, @NotNull Supplier<ItemDisplay> spawnEntity) {
        super(schema, spawnEntity);
    }

    @OverridingMethodsMustInvokeSuper
    public void update(@NotNull VehicleEntity vehicleEntity) {
        ItemDisplay display = entity();
        if (display == null) {
            return;
        }

        display.setViewRange(visible ? 1 : 0);

        ModelItem modelItem = modelItem(vehicleEntity);

        // Update matrix
        Matrix4f matrix = modelItem.getMatrix();
        if (visible && computeMatrixDifference(matrixLastUpdate, matrix) > MATRIX_DIFFERENCE_THRESHOLD) {
            display.setTransformationMatrix(matrix);
            display.setInterpolationDelay(0);
            display.setInterpolationDuration(VehicleEntity.TICK_INTERVAL);
            matrixLastUpdate = matrix;
        }

        // Update itemstack / material if changed
        if (display.getItemStack() != null
                && modelItem.getMain().getItemStack() != null
                && display.getItemStack().getType() != modelItem.getMain().getItemStack().getType()) {
            display.setItemStack(modelItem.getMain().getItemStack());
        }

        // Set stack visible
        if (visible && visibleLastUpdate && display.getItemStack() == null) {
            display.setItemStack(modelItem.getMain().getItemStack());
        }

        // Set stack invisible
        if (!visible && display.getItemStack() != null) {
            display.setItemStack(new ItemStack(Material.AIR));
        }

        visibleLastUpdate = visible;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    // Highly questionable heuristic for checking the 'difference' between two subsequent matrices
    private static float computeMatrixDifference(@NotNull Matrix4f a, @NotNull Matrix4f b) {
        Matrix4f difference = new Matrix4f(a).sub(b);
        float matrixDifference = 0.0F;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                matrixDifference += Math.abs(difference.get(x, y));
            }
        }
        return matrixDifference;
    }

    protected abstract @NotNull ModelItem modelItem(@NotNull VehicleEntity vehicleEntity);
}
