package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.machine;

import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public record MachineTemplate(ItemStack template, List<CustomMachineRecipe> recipes) {
    public boolean isItemSimilar(ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, template, true);
    }
}
