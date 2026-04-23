package com.github.drakescraft-labs.extraheads.setup;

import org.bukkit.NamespacedKey;

import com.github.drakescraft-labs.extraheads.ExtraHeads;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ItemGroups {

    public static final ItemGroup MAIN = new ItemGroup(
        new NamespacedKey(ExtraHeads.getInstance(), "extra_heads"),
        new CustomItemStack(SlimefunUtils.getCustomHead("5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932"), "&7Extra Heads"),
        1
    );
}
