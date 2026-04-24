package me.ncbpfluffybear.slimyrepair;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class SRItems {

    // Category
    public static final ItemGroup slimy_repair = new ItemGroup(
        new NamespacedKey(SlimyRepair.getInstance(), "slimy_repair"),
        new CustomItemStack(Material.ANVIL, "&aSlimy Repair"));

    public static final SlimefunItemStack SLIMY_ANVIL = new SlimefunItemStack("SLIMY_ANVIL",
        Material.SLIME_BLOCK,
        "&a&lSlimy Anvil",
        "",
        "&7Used to repair Slimefun tools easily"
    );
}
