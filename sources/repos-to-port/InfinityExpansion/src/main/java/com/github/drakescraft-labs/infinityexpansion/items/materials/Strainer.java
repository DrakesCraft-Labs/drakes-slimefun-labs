package com.github.drakescraft-labs.infinityexpansion.items.materials;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.github.drakescraft-labs.infinityexpansion.InfinityExpansion;
import com.github.drakescraft-labs.infinityexpansion.categories.Groups;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;

/**
 * Items to be used in the Strainer Base
 *
 * @author Mooy1
 */
public final class Strainer extends SlimefunItem implements NotPlaceable {

    private static final NamespacedKey KEY = InfinityExpansion.createKey("strainer_speed");

    public Strainer(SlimefunItemStack item, ItemStack[] recipe, int speed) {
        super(Groups.BASIC_MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER, speed);
        item.setItemMeta(meta);
    }

    /**
     * This method gets the speed of strainer from an item
     *
     * @return speed
     */
    public static int getStrainer(@Nullable ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            return item.getItemMeta().getPersistentDataContainer().getOrDefault(Strainer.KEY, PersistentDataType.INTEGER, 0);
        }
        return 0;
    }

}
