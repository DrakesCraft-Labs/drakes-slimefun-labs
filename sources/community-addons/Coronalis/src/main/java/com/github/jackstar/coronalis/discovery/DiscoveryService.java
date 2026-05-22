package com.github.jackstar.coronalis.discovery;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import com.github.jackstar.coronalis.Coronalis;

/**
 * Persiste descubrimientos por jugador y entrega XP la primera vez (o bonus reducido).
 */
public final class DiscoveryService {

    private static final String PDC_KEY = "CORONALIS_discoveries";

    private final Coronalis plugin;
    private final NamespacedKey storageKey;

    public DiscoveryService(@Nonnull Coronalis plugin) {
        this.plugin = plugin;
        this.storageKey = new NamespacedKey(plugin, PDC_KEY);
    }

    /**
     * Registra un hito; devuelve true si era la primera vez para ese jugador.
     */
    public boolean tryDiscover(@Nonnull Player player, @Nonnull String discoveryId, @Nonnull String displayName, @Nonnull String configXpPath) {
        try {
            boolean firstTime = mark(player, discoveryId);
            int xp = plugin.getConfig().getInt(configXpPath, firstTime ? 10 : 5);
            if (!firstTime) {
                xp = Math.max(1, xp / 4);
            }

            player.giveExp(xp);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, firstTime ? 1.3f : 0.9f);

            String prefix = color(plugin.getConfig().getString("messages.prefix", ""));
            String template = plugin.getConfig().getString(
                firstTime ? "messages.new-discovery" : "messages.repeat-discovery",
                firstTime ? "&aNuevo: &f%name% (+%xp% XP)" : "&7Repetido: &f%name% (+%xp% XP)"
            );
            player.sendMessage(prefix + color(template.replace("%name%", displayName).replace("%xp%", String.valueOf(xp))));
            return firstTime;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error registrando descubrimiento " + discoveryId, e);
            return false;
        }
    }

    public void onResearchUnlocked(@Nonnull Player player, @Nonnull NamespacedKey researchKey, @Nonnull String researchName) {
        if (!plugin.getName().equalsIgnoreCase(researchKey.getNamespace())) {
            return;
        }
        int bonus = plugin.getConfig().getInt("research-unlock-bonus-xp", 10);
        if (bonus <= 0) {
            return;
        }
        player.giveExp(bonus);
        String prefix = color(plugin.getConfig().getString("messages.prefix", ""));
        String template = plugin.getConfig().getString("messages.research-bonus", "&eInvestigación: &f%research%");
        player.sendMessage(prefix + color(template.replace("%research%", researchName).replace("%xp%", String.valueOf(bonus))));
    }

    @Nonnull
    public static String targetDiscoveryId(@Nonnull String targetName) {
        return "target_" + targetName.toLowerCase()
            .replace(" ", "_")
            .replace("*", "star")
            .replace("ó", "o")
            .replace("á", "a")
            .replace("í", "i")
            .replace("é", "e")
            .replace("ú", "u")
            .replace("ñ", "n");
    }

    @Nonnull
    public static String targetConfigPath(@Nonnull String targetName) {
        return "discovery-xp." + targetDiscoveryId(targetName);
    }

    private boolean mark(@Nonnull Player player, @Nonnull String discoveryId) {
        Set<String> known = new HashSet<>(load(player));
        if (!known.add(discoveryId)) {
            return false;
        }
        player.getPersistentDataContainer().set(storageKey, PersistentDataType.STRING, String.join(",", known));
        return true;
    }

    @Nonnull
    private Set<String> load(@Nonnull Player player) {
        String raw = player.getPersistentDataContainer().get(storageKey, PersistentDataType.STRING);
        if (raw == null || raw.isBlank()) {
            return Collections.emptySet();
        }
        Set<String> out = new HashSet<>();
        for (String part : raw.split(",")) {
            if (!part.isBlank()) {
                out.add(part.trim());
            }
        }
        return out;
    }

    @Nonnull
    private static String color(@Nonnull String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
