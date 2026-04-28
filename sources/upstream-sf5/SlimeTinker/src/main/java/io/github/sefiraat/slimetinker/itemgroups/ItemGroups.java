package io.github.sefiraat.slimetinker.itemgroups;

import io.github.sefiraat.slimetinker.SlimeTinker;
import io.github.sefiraat.slimetinker.utils.Keys;
import io.github.sefiraat.slimetinker.utils.SkullTextures;
import io.github.sefiraat.slimetinker.utils.ThemeUtils;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import org.bukkit.Material;

public final class ItemGroups {

    private ItemGroups() {
        throw new UnsupportedOperationException("Utility Class");
    }

    public static final MainFlexGroup MAIN = new MainFlexGroup(Keys.ITEM_GROUP_MAIN, CustomItemStack.create($$$));
    public static final DummyItemGroup WORKSTATIONS = new DummyItemGroup(Keys.ITEM_GROUP_WORKSTATIONS, CustomItemStack.create($$$));
    public static final DummyItemGroup MATERIALS = new DummyItemGroup(Keys.ITEM_GROUP_MATERIALS, CustomItemStack.create($$$));
    public static final DummyItemGroup MOLTEN_METALS = new DummyItemGroup(Keys.ITEM_GROUP_MOLTEN, CustomItemStack.create($$$));
    public static final DummyItemGroup ALLOYS = new DummyItemGroup(Keys.ITEM_GROUP_ALLOYS, CustomItemStack.create($$$));
    public static final DummyItemGroup CASTS = new DummyItemGroup(Keys.ITEM_GROUP_CASTS, CustomItemStack.create($$$));
    public static final DummyItemGroup PARTS = new DummyItemGroup(Keys.ITEM_GROUP_PARTS, CustomItemStack.create($$$));
    public static final DummyItemGroup TOOLS = new DummyItemGroup(Keys.ITEM_GROUP_TOOLS, CustomItemStack.create($$$));
    public static final DummyItemGroup ARMOUR = new DummyItemGroup(Keys.ITEM_GROUP_ARMOUR, CustomItemStack.create($$$));
    public static final DummyItemGroup TRAITS = new DummyItemGroup(Keys.ITEM_GROUP_PROPERTIES, CustomItemStack.create($$$));
    public static final DummyItemGroup MODIFICATIONS = new DummyItemGroup(Keys.ITEM_GROUP_MODIFICATIONS, CustomItemStack.create($$$));
    public static final DummyItemGroup PART_DICT = new DummyItemGroup(Keys.ITEM_GROUP_PART_DICT, CustomItemStack.create($$$));
    public static final DummyItemGroup DUMMY = new DummyItemGroup(Keys.ITEM_GROUP_DUMMY, CustomItemStack.create($$$));

    public static void set(SlimeTinker p) {
        MAIN.register(p);
        WORKSTATIONS.register(p);
        MATERIALS.register(p);
        MOLTEN_METALS.register(p);
        ALLOYS.register(p);
        CASTS.register(p);
        PARTS.register(p);
        TOOLS.register(p);
        ARMOUR.register(p);
        TRAITS.register(p);
        MODIFICATIONS.register(p);
        PART_DICT.register(p);
    }

}
