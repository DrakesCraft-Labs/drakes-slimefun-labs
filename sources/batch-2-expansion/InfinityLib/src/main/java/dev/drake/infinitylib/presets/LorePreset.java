package dev.drake.infinitylib.presets;

import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;
import org.bukkit.ChatColor;

/**
 * Compatibility class for InfinityLib LorePreset.
 */
public final class LorePreset {

    private LorePreset() {}

    public static String energy(int energy) {
        return ChatColor.GOLD + "⚡ " + ChatColor.WHITE + energy + " J ";
    }
}
