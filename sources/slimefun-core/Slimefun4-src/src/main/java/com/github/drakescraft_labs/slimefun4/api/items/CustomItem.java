package com.github.drakescraft_labs.slimefun4.api.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.drake.dough.items.CustomItemStack;

/**
 * A legacy alias for {@link CustomItemStack}.
 * Useful for maintaining compatibility with older addons.
 * 
 * @deprecated Use {@link CustomItemStack} instead.
 */
@Deprecated
public class CustomItem extends CustomItemStack {

    @ParametersAreNonnullByDefault
    public CustomItem(Material type, String name, String... lore) {
        super(type, name, lore);
    }

    @ParametersAreNonnullByDefault
    public CustomItem(ItemStack item, String name, String... lore) {
        super(item, name, lore);
    }

    @ParametersAreNonnullByDefault
    public CustomItem(Material type, String name) {
        super(type, name);
    }

}
