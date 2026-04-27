package org.metamechanists.aircraft.vehicle.component.base;

import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.displaymodellib.models.components.ModelText;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;

import java.util.Objects;
import java.util.function.Supplier;


public abstract class TextComponent<T extends KinematicEntitySchema> extends KinematicEntity<TextDisplay, T> {
    private static final double MATRIX_DIFFERENCE_THRESHOLD = 1.0e-4;
    private boolean visible = true;
    private boolean visibleLastUpdate = true;
    private boolean visibleLastLastUpdate = true;
    private Matrix4f matrixLastUpdate = new Matrix4f();

    protected TextComponent(@NotNull StateReader reader, TextDisplay textDisplay) {
        super(reader, textDisplay);
    }

    protected TextComponent(@NotNull T schema, @NotNull Supplier<TextDisplay> spawnEntity) {
        super(schema, spawnEntity);
    }

    public final void update(@NotNull VehicleEntity vehicleEntity) {
        TextDisplay display = entity();
        if (display == null) {
            return;
        }

        ModelText modelText = modelText(vehicleEntity);

        // Update matrix
        Matrix4f matrix = modelText.getMatrix();
        if (visible && computeMatrixDifference(matrixLastUpdate, matrix) > MATRIX_DIFFERENCE_THRESHOLD) {
            display.setTransformationMatrix(matrix);
            display.setInterpolationDelay(0);
            display.setInterpolationDuration(VehicleEntity.TICK_INTERVAL);
            matrixLastUpdate = matrix;
        }

        // Update background if changed
        if (!Objects.equals(display.getBackgroundColor(), modelText.getMain().getBackgroundColor())) {
            display.setBackgroundColor(modelText.getMain().getBackgroundColor());
        }

        // Set text visible
        if (visible && visibleLastUpdate && visibleLastLastUpdate && modelText.getMain().getText() != null) {
            if (display.getText() == null || !display.text().equals(modelText.getMain().getText())) {
                display.text(modelText.getMain().getText());
            }
        }

        // Set text invisible
        if (!visible && display.getText() != null) {
            display.setText("");
        }

        visibleLastLastUpdate = visibleLastUpdate;
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

    protected abstract @NotNull ModelText modelText(@NotNull VehicleEntity vehicleEntity);
}
