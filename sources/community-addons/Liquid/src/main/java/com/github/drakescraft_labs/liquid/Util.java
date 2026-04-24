package com.github.drakescraft_labs.liquid;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import dev.drake.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.utils.ChatUtils;
import org.bukkit.inventory.ItemStack;

public final class Util {

    private Util() {}

    public static String getID(ItemStack stack) {
        if (stack instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) stack).getItemId();
        } else {
            return ChatUtils.removeColorCodes(ItemUtils.getItemName(stack))
                .toUpperCase().replace(" ", "_");
        }
    }
}
