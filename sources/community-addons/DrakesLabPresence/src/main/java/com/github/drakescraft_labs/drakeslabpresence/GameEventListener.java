package com.github.drakescraft_labs.drakeslabpresence;

import java.util.LinkedHashMap;
import java.util.Map;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Eventos de juego hacia webhook (opt-in por flags en config).
 */
public final class GameEventListener implements Listener {

    private final DrakesLabPresencePlugin plugin;
    private int countInWindow;
    private long windowStartMs;

    GameEventListener(DrakesLabPresencePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("events.player-join", false)) {
            return;
        }
        if (!allowRate()) {
            return;
        }
        Player p = event.getPlayer();
        Map<String, String> data = basePlayerData(p);
        plugin.deliverGameEvent(
                "player_join",
                "Entrada",
                "**" + p.getName() + "** entró al servidor.",
                3447003,
                data);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.getConfig().getBoolean("events.player-quit", false)) {
            return;
        }
        if (!allowRate()) {
            return;
        }
        Player p = event.getPlayer();
        Map<String, String> data = basePlayerData(p);
        plugin.deliverGameEvent(
                "player_quit",
                "Salida",
                "**" + p.getName() + "** salió del servidor.",
                9807270,
                data);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("events.player-death", false)) {
            return;
        }
        if (!allowRate()) {
            return;
        }
        Player p = event.getEntity();
        String msg = "";
        if (event.deathMessage() != null) {
            msg = PlainTextComponentSerializer.plainText().serialize(event.deathMessage());
        }
        Map<String, String> data = basePlayerData(p);
        data.put("deathMessage", msg);
        plugin.deliverGameEvent(
                "player_death",
                "Muerte",
                JsonStrings.truncate(msg.isEmpty() ? p.getName() + " murió." : msg, 500),
                15158332,
                data);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (!plugin.getConfig().getBoolean("events.advancement", false)) {
            return;
        }
        if (!allowRate()) {
            return;
        }
        Player p = event.getPlayer();
        String key = event.getAdvancement().getKey().toString();
        Map<String, String> data = basePlayerData(p);
        data.put("advancement", key);
        plugin.deliverGameEvent(
                "advancement",
                "Logro",
                "**" + p.getName() + "** completó `" + key + "`",
                10181046,
                data);
    }

    private Map<String, String> basePlayerData(Player p) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("player", p.getName());
        if (plugin.getConfig().getBoolean("events.show-player-uuid", false)) {
            m.put("uuid", p.getUniqueId().toString());
        }
        return m;
    }

    private synchronized boolean allowRate() {
        int max = plugin.getConfig().getInt("events.rate-limit-per-minute", 12);
        if (max <= 0) {
            return true;
        }
        long now = System.currentTimeMillis();
        if (now - windowStartMs > 60_000L) {
            windowStartMs = now;
            countInWindow = 0;
        }
        if (countInWindow >= max) {
            return false;
        }
        countInWindow++;
        return true;
    }
}
