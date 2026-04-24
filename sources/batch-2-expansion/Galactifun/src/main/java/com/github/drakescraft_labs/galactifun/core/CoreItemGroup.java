package com.github.drakescraft_labs.galactifun.core;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;

import com.github.drakescraft_labs.galactifun.Galactifun;
import com.github.drakescraft_labs.galactifun.base.GalactifunHead;
import com.github.drakescraft_labs.galactifun.core.categories.AssemblyItemGroup;
import com.github.drakescraft_labs.galactifun.core.categories.GalacticItemGroup;
import com.github.drakescraft_labs.infinitylib.groups.MultiGroup;
import com.github.drakescraft_labs.infinitylib.groups.SubGroup;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import dev.drake.dough.items.CustomItemStack;

/**
 * Slimefun item categories
 *
 * @author Mooy1
 */
// TODO move these categories somewhere not public, addons should use their own
@UtilityClass
public final class CoreItemGroup {

    /* cheat categories */
    public static final ItemGroup ASSEMBLY = new SubGroup(
            "assembly", new CustomItemStack(Material.SMITHING_TABLE, "&fAssembly Table Recipes")
    );

    /* normal categories */
    public static final ItemGroup EQUIPMENT = new SubGroup(
            "equipment", new CustomItemStack(Material.IRON_HELMET, "&fEquipment")
    );
    public static final ItemGroup ITEMS = new SubGroup(
            "items", new CustomItemStack(GalactifunHead.ROCKET, "&fGalactifun")
    );
    public static final ItemGroup COMPONENTS = new SubGroup(
            "components", new CustomItemStack(Material.IRON_INGOT, "&fGalactifun Components")
    );
    public static final ItemGroup MACHINES = new SubGroup(
            "machines", new CustomItemStack(Material.REDSTONE_LAMP, "&fGalactifun Machines")
    );
    public static final ItemGroup BLOCKS = new SubGroup(
            "blocks", new CustomItemStack(Material.COBBLESTONE, "&fGalactifun Blocks")
    );
    public static final ItemGroup RELICS = new SubGroup(
            "relics", new CustomItemStack(Material.CHISELED_POLISHED_BLACKSTONE, "&fGalactifun Relics")
    );

    public static final AssemblyItemGroup ASSEMBLY_CATEGORY = new AssemblyItemGroup(
            Galactifun.createKey("assembly_flex"),
            new CustomItemStack(Material.SMITHING_TABLE, "&fAssembly Table Recipes"));

    public static void setup(Galactifun galactifun) {
        ItemGroup universe = new GalacticItemGroup(Galactifun.createKey("galactic_flex"),
                new CustomItemStack(Material.END_STONE, "&bThe Universe"));

        new MultiGroup("main",
                new CustomItemStack(Material.BEACON, "&bGalactifun"),
                EQUIPMENT, ITEMS, COMPONENTS, MACHINES, BLOCKS, universe, ASSEMBLY_CATEGORY, RELICS
        ).register(galactifun);
    }

}
