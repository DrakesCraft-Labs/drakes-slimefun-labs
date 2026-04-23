package com.github.drakescraft_labs.slimefun4.api.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * A legacy alias for {@link ItemGroup}.
 * Useful for maintaining compatibility with older addons.
 * 
 * @deprecated Use {@link ItemGroup} instead.
 */
@Deprecated
public class Category extends ItemGroup {

    @ParametersAreNonnullByDefault
    public Category(NamespacedKey key, ItemStack item) {
        super(key, item);
    }

    @ParametersAreNonnullByDefault
    public Category(NamespacedKey key, ItemStack item, int tier) {
        super(key, item, tier);
    }

}
