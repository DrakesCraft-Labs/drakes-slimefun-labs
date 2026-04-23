package com.github.drakescraft_labs.mobcapturer.setup;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import com.github.drakescraft_labs.mobcapturer.MobCapturer;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.items.groups.NestedItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.groups.SubItemGroup;
import dev.drake.dough.items.CustomItemStack;

import lombok.experimental.UtilityClass;

/**
 * All the {@link ItemGroup}s in MobCapturer.
 *
 * @author TheBusyBiscuit
 * @author ybw0014
 */
@UtilityClass
public final class ItemGroups {

    public static final NestedItemGroup MAIN = new NestedItemGroup(
        new NamespacedKey(MobCapturer.getInstance(), "mob_capturer"),
        new SlimefunItemStack(
            "MOB_CAPTURER",
            "d429ff1d2015cb11398471bb2f895f7b4c3ccec201e4ad7a86ff24b744878c",
            "&dMob Capturer"
        )
    );
    public static final SubItemGroup TOOLS = new SubItemGroup(
        new NamespacedKey(MobCapturer.getInstance(), "tools"),
        MAIN,
        new CustomItemStack(
            ItemStacks.MOB_CANNON,
            "&dTools"
        )
    );
    public static final SubItemGroup MOB_EGGS = new SubItemGroup(
        new NamespacedKey(MobCapturer.getInstance(), "mob_eggs"),
        MAIN,
        new CustomItemStack(
            Material.CHICKEN_SPAWN_EGG,
            "&aMob Eggs"
        )
    );
}
