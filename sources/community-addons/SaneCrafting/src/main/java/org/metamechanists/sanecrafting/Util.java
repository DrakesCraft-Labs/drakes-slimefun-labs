package org.metamechanists.sanecrafting;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class Util {
    private Util() {}

    // Technically could lead to clashes if two shaped recipes for same item but... hopefully not...
    public static @NotNull String generateRecipeId(@NotNull ItemStack output) {
        return generateRecipeId(SlimefunItem.getByItem(output));
    }

    public static @NotNull String generateRecipeId(@NotNull SlimefunItem item) {
        String normalisedName = item.getId().toLowerCase()
                .replace(' ', '_')
                .replaceAll("[^a-z0-9/._\\-]", ""); // remove characters not allowed in id
        return "sanecrafting_" + normalisedName;
    }

    public static @Nullable <T extends SlimefunItem> T findMultiblock(Class<T> clazz) {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (clazz.isInstance(item)) {
                return clazz.cast(item);
            }
        }

        SaneCrafting.getInstance().getLogger().severe("Failed to initialise SaneCrafting; EnhancedCraftingTable does not exist!");
        return null;
    }
}
