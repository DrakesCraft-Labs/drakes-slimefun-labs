package me.jasperchasetoq.dyebench;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.items.groups.NestedItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.groups.SubItemGroup;
import com.github.drakescraft-labs.slimefun4.core.attributes.MachineTier;
import com.github.drakescraft-labs.slimefun4.utils.LoreBuilder;
import me.jasperchasetoq.wolfylibrary.slimefun.items.api.MachineLevel;
import me.jasperchasetoq.wolfylibrary.slimefun.items.api.MachineType;
import me.jasperchasetoq.wolfylibrary.slimefun.utils.ItemLore;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class DyeBenchItems {
    private DyeBenchItems() {}

    //groups
    public static final NestedItemGroup DyeBenchNestedGeneral = new NestedItemGroup(new NamespacedKey(DyeBench.getInstance(), "JC_DB_GENERAL"), new CustomItemStack(Material.LOOM, "&bDyeBench"));
    public static final SubItemGroup DyeBenchMachines = new SubItemGroup(new NamespacedKey(DyeBench.getInstance(), "JC_DB_MACHINES"), DyeBenchNestedGeneral, new CustomItemStack(Material.LOOM, "&bMachines"));
    public static final SubItemGroup DyeBenchPigments = new SubItemGroup(new NamespacedKey(DyeBench.getInstance(), "JC_DB_PIGMENTS"), DyeBenchNestedGeneral, new CustomItemStack(Material.WHITE_DYE, "&bPigments"));

    //machines
    public static final SlimefunItemStack JC_DYEBENCH = new SlimefunItemStack("JC_DYEBENCH", Material.LOOM, "&eDye Bench", "&fDyes items using pigments", ItemLore.machine(MachineLevel.BASIC, MachineType.MACHINE), ItemLore.EnergyPerSlimefunTick(25));
    //Pigments
    public static final SlimefunItemStack JC_WHITE_PIGMENT = new SlimefunItemStack("JC_WHITE_PIGMENT", Material.WHITE_DYE, "&fWhite Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_ORANGE_PIGMENT = new SlimefunItemStack("JC_ORANGE_PIGMENT", Material.ORANGE_DYE, "&6Orange Pigment", "", "&6A reusable Pigment", "");
    public static final SlimefunItemStack JC_MAGENTA_PIGMENT = new SlimefunItemStack("JC_MAGENTA_PIGMENT", Material.MAGENTA_DYE, "&fMagenta Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_LIGHT_BLUE_PIGMENT = new SlimefunItemStack("JC_LIGHT_BLUE_PIGMENT", Material.LIGHT_BLUE_DYE, "&fLight Blue Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_YELLOW_PIGMENT = new SlimefunItemStack("JC_YELLOW_PIGMENT", Material.YELLOW_DYE, "&fYellow Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_LIME_PIGMENT = new SlimefunItemStack("JC_LIME_PIGMENT", Material.LIME_DYE, "&fLime Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_PINK_PIGMENT = new SlimefunItemStack("JC_PINK_PIGMENT", Material.PINK_DYE, "&fPink Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_GRAY_PIGMENT = new SlimefunItemStack("JC_GRAY_PIGMENT", Material.GRAY_DYE, "&fGray Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_LIGHT_GRAY_PIGMENT = new SlimefunItemStack("JC_LIGHT_GRAY_PIGMENT", Material.LIGHT_GRAY_DYE, "&fLight Gray Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_CYAN_PIGMENT = new SlimefunItemStack("JC_CYAN_PIGMENT", Material.CYAN_DYE, "&3Cyan Pigment", "", "&3A reusable Pigment", "");
    public static final SlimefunItemStack JC_PURPLE_PIGMENT = new SlimefunItemStack("JC_PURPLE_PIGMENT", Material.PURPLE_DYE, "&fPurple Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_BLUE_PIGMENT = new SlimefunItemStack("JC_BLUE_PIGMENT", Material.BLUE_DYE, "&fBlue Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_BROWN_PIGMENT = new SlimefunItemStack("JC_BROWN_PIGMENT", Material.BROWN_DYE, "&fBrown Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_GREEN_PIGMENT = new SlimefunItemStack("JC_GREEN_PIGMENT", Material.GREEN_DYE, "&fGreen Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_RED_PIGMENT = new SlimefunItemStack("JC_RED_PIGMENT", Material.RED_DYE, "&fRed Pigment", "", "&fA reusable Pigment", "");
    public static final SlimefunItemStack JC_BLACK_PIGMENT = new SlimefunItemStack("JC_BLACK_PIGMENT", Material.BLACK_DYE, "&0Black Pigment", "", "&0A reusable Pigment", "");

}

