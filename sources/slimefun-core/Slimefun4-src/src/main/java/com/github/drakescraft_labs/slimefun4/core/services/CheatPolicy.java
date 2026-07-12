package com.github.drakescraft_labs.slimefun4.core.services;

import java.util.List;
import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

/**
 * Applies the server-wide safety boundary for non-staff Slimefun cheat access.
 */
public final class CheatPolicy {

    private static final String GUARD = "sfmaster-guard";
    private static final String CHEAT_PERMISSION = "slimefun.cheat.items";

    private CheatPolicy() {
    }

    public static boolean canClaim(Player player, SlimefunItem item) {
        if (!isLimitedPlayer(player) || item == null) {
            return true;
        }

        String id = item.getId().toUpperCase(Locale.ROOT);
        return !matchesAny(id, list("blocked-id-prefixes"), true)
                && !matchesAny(id, list("blocked-id-fragments"), false)
                && !blockedAddon(item.getAddon())
                && !blockedMaterial(item.getItem().getType());
    }

    public static void deny(Player player) {
        player.sendMessage("§cSFMaster solo permite automatización y componentes técnicos.");
        player.sendMessage("§7Armas, armaduras, herramientas y objetos de endgame están bloqueados.");
    }

    private static boolean isLimitedPlayer(Player player) {
        Boolean enabled = Slimefun.getCfg().getBoolean(GUARD + ".enabled");
        if ((enabled != null && !enabled) || player.isOp()) {
            return false;
        }

        String bypass = valueOrDefault(Slimefun.getCfg().getString(GUARD + ".bypass-permission"), "slimefun.cheat.items.bypass");
        if (player.hasPermission(bypass)) {
            return false;
        }

        String limitedPermission = valueOrDefault(Slimefun.getCfg().getString(GUARD + ".limited-permission"), "odysseia.sfmaster.active");
        return player.hasPermission(CHEAT_PERMISSION) || player.hasPermission(limitedPermission);
    }

    private static boolean blockedAddon(SlimefunAddon addon) {
        return addon != null && matchesAny(addon.getName().toUpperCase(Locale.ROOT), list("blocked-addons"), false);
    }

    private static boolean blockedMaterial(Material material) {
        String name = material.name().toUpperCase(Locale.ROOT);
        return list("blocked-materials").stream().anyMatch(value -> value.equalsIgnoreCase(name))
                || matchesAny(name, list("blocked-id-fragments"), false);
    }

    private static boolean matchesAny(String value, List<String> patterns, boolean prefix) {
        for (String pattern : patterns) {
            String normalized = pattern.toUpperCase(Locale.ROOT);
            if ((prefix && value.startsWith(normalized)) || (!prefix && value.contains(normalized))) {
                return true;
            }
        }
        return false;
    }

    private static List<String> list(String path) {
        return Slimefun.getCfg().getStringList(GUARD + "." + path);
    }

    private static String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
