package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.machine;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetProvider;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.CustomMenu;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.machine.MachineRecord;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.script.ScriptEval;
import org.lins.mmmjjkx.rykenslimefuncustomizer.utils.ExceptionHandler;

import java.util.List;

public class CustomEnergyGenerator extends CustomMachine implements EnergyNetProvider {
    private final ScriptEval eval;
    private final int defaultOutput;

    public CustomEnergyGenerator(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            @Nullable CustomMenu menu,
            List<Integer> input,
            List<Integer> output,
            MachineRecord record,
            EnergyNetComponentType type,
            @Nullable ScriptEval eval,
            int defaultOutput) {
        super(itemGroup, item, recipeType, recipe, menu, input, output, record, type, eval);

        this.eval = eval;
        this.defaultOutput = defaultOutput;
    }

    @Override
    public int getGeneratedOutput(@NotNull Location l, @NotNull Config data) {
        if (eval == null) {
            return defaultOutput;
        } else {
            try {
                Object result = eval.evalFunction("getGeneratedOutput", l, data);
                if (result instanceof Integer i) {
                    return i;
                } else {
                    ExceptionHandler.handleWarning("getGeneratedOutput() returned a non integer value: "+result+" causing the default output value of the custom generator to be used. Please contact the corresponding author to fix this issue!");
                    return defaultOutput;
                }
            } catch (Exception e) {
                return defaultOutput;
            }
        }
    }
}
