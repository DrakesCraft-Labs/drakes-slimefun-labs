package me.mmmjjkx.betterChests;

import com.github.drakescraft_labs.slimefun4.api.items.groups.NestedItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.groups.SubItemGroup;
import me.mmmjjkx.betterChests.items.BCItemStacks;
import org.bukkit.NamespacedKey;

public class BCGroups {
    private static final NamespacedKey MAIN_KEY = new NamespacedKey(BetterChests.INSTANCE, "group_main");
    private static final NamespacedKey STORAGES_KEY = new NamespacedKey(BetterChests.INSTANCE, "group_storages");
    private static final NamespacedKey TOOLS_KEY = new NamespacedKey(BetterChests.INSTANCE, "group_tools");
    private static final NamespacedKey MATERIALS_KEY = new NamespacedKey(BetterChests.INSTANCE, "group_materials");
    private static final NamespacedKey CARGO_KEY = new NamespacedKey(BetterChests.INSTANCE, "group_cargo");

    public static final NestedItemGroup MAIN = new NestedItemGroup(MAIN_KEY, BCItemStacks.GROUP_MAIN_ITEM, 2);
    public static final SubItemGroup STORAGES = new SubItemGroup(STORAGES_KEY, MAIN, BCItemStacks.GROUP_STORAGE_ITEM);
    public static final SubItemGroup TOOLS = new SubItemGroup(TOOLS_KEY, MAIN, BCItemStacks.GROUP_TOOL_ITEM);
    public static final SubItemGroup MATERIALS = new SubItemGroup(MATERIALS_KEY, MAIN, BCItemStacks.GROUP_MATERIAL_ITEM);
    public static final SubItemGroup CARGO = new SubItemGroup(CARGO_KEY, MAIN, BCItemStacks.GROUP_CARGO_ITEM);
}
