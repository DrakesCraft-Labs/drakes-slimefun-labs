package com.github.drakescraft-labs.infinitylib.presets;

import com.github.drakescraft-labs.slimefun4.utils.LoreBuilder;
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
