package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.machine;

import com.github.drakescraft-labs.slimefun4.implementation.operations.CraftingOperation;
import com.github.drakescraft-labs.slimefun4.libraries.commons.lang.Validate;
import javax.annotation.Nonnull;
import lombok.Getter;

@Getter
public class CustomLinkedMachineOperation extends CraftingOperation {
    private final CustomLinkedMachineRecipe recipe;

    public CustomLinkedMachineOperation(@Nonnull CustomLinkedMachineRecipe recipe) {
        super(recipe.getInput(), recipe.getOutput(), recipe.getTicks());
        Validate.isTrue(
                recipe.getTicks() >= 0,
                "The amount of total ticks must be a positive integer or zero, received: " + recipe.getTicks());
        this.recipe = recipe;
    }

    public int getTotalTicks() {
        return recipe.getTicks();
    }
}
