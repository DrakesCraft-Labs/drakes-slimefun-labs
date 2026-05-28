package dev.drake.infinitylib.presets;

import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * Compatibility class for InfinityLib MenuPreset.
 */
public final class MenuPreset {

    private MenuPreset() {}

    public static final ItemStack borderItemInput = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, "&9Input");
    public static final ItemStack borderItemOutput = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Output");
    public static final ItemStack borderItemStatus = new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, "&bStatus");

}
