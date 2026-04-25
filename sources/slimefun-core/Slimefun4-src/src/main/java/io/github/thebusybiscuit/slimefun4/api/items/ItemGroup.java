package io.github.thebusybiscuit.slimefun4.api.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

@Deprecated(forRemoval = false)
public class ItemGroup extends com.github.drakescraft_labs.slimefun4.api.items.ItemGroup {

    public ItemGroup(NamespacedKey key, ItemStack item) {
        super(key, item);
    }

    public ItemGroup(NamespacedKey key, ItemStack item, int tier) {
        super(key, item, tier);
    }
}
