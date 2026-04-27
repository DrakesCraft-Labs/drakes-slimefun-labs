package com.github.drakescraft_labs.drakeslabpresence;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Webhook HTTPS opt-in: latido periódico + eventos de servidor/juego hacia Discord o relay.
 * Sin tokens de bot embebidos; el operador configura URLs y flags.
 */
public final class DrakesLabPresencePlugin extends JavaPlugin {

    private static final String SCHEMA_PRESENCE = "drakeslab-presence/1";

    private final AtomicInteger heartbeatTaskId = new AtomicInteger(-1);
    private final AtomicInteger startupTaskId = new AtomicInteger(-1);
    private volatile String instanceIdCached = "";
    private GameEventListener gameEventListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getCommand("drakeslabpresence") != null) {
            getCommand("drakeslabpresence").setExecutor(this::onCommand);
        }
        refreshBridgeState();
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("enabled", false)
                && getConfig().getBoolean("events.server-shutdown", false)) {
            sendShutdownWebhookSync();
        }
        cancelTasks();
        unregisterGameEvents();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("drakeslabpresence.admin")) {
            return false;
        }
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            reloadConfig();
            refreshBridgeState();
            sender.sendMessage("[DrakesLabPresence] Config recargada (latido + eventos + webhooks).");
            return true;
        }
        if (args.length == 1 && "test".equalsIgnoreCase(args[0])) {
            if (!getConfig().getBoolean("enabled", false)) {
                sender.sendMessage("[DrakesLabPresence] enabled=false — actívalo para probar el webhook.");
                return true;
            }
            String evUrl = resolveEventsWebhookUrl();
            final String targetUrl = evUrl.isBlank() ? resolvePresenceWebhookUrl() : evUrl;
            if (targetUrl.isBlank() || !WebhookSender.isAllowedHttpsUrl(targetUrl)) {
                sender.sendMessage("[DrakesLabPresence] No hay webhook-url válida.");
                return true;
            }
            ensureInstanceIdLoadedAsync(() -> {
                String serverLbl = resolveServerLabel();
                byte[] body;
                if (WebhookSender.isDiscordWebhookUrl(targetUrl)) {
                    String footer = instanceIdCached + " · test";
                    String json = BridgePayloads.discordEmbed(
                            "Prueba DrakesLabPresence",
                            "Webhook de **eventos** (o presencia si no hay `webhook-events-url`).",
                            5814783,
                            footer,
                            Collections.singletonMap("Emisor", sender.getName()));
                    body = json.getBytes(StandardCharsets.UTF_8);
                } else {
                    Map<String, String> data = new java.util.LinkedHashMap<>();
                    data.put("note", "manual test");
                    data.put("sender", sender.getName());
                    String json = BridgePayloads.genericBridgeEvent(
                            "manual_test", instanceIdCached, serverLbl, data);
                    body = json.getBytes(StandardCharsets.UTF_8);
                }
                WebhookSender.sendAsync(this, targetUrl, body);
                sender.sendMessage("[DrakesLabPresence] POST de prueba encolado hacia el webhook de eventos.");
            });
            return true;
        }
        sender.sendMessage("[DrakesLabPresence] Uso: /drakeslabpresence reload | test");
        return true;
    }

    /**
     * Llamado desde {@link GameEventListener} en el hilo principal del servidor.
     */
    public void deliverGameEvent(
            String eventKey,
            String discordTitle,
            String discordDescription,
            int colorArgb,
            Map<String, String> data
    ) {
        if (!getConfig().getBoolean("enabled", false)) {
            return;
        }
        String url = resolveEventsWebhookUrl();
        if (url.isBlank() || !WebhookSender.isAllowedHttpsUrl(url)) {
            return;
        }
        Map<String, String> payload = data != null ? data : Collections.emptyMap();
        String instanceId = instanceIdCached;
        String serverLabel = resolveServerLabel();
        byte[] body;
        if (WebhookSender.isDiscordWebhookUrl(url)) {
            String footer = instanceId + " · " + serverLabel;
            String json = BridgePayloads.discordEmbed(discordTitle, discordDescription, colorArgb, footer, payload);
            body = json.getBytes(StandardCharsets.UTF_8);
        } else {
            String json = BridgePayloads.genericBridgeEvent(eventKey, instanceId, serverLabel, payload);
            body = json.getBytes(StandardCharsets.UTF_8);
        }
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> WebhookSender.sendAsync(this, url, body));
    }

    private void refreshBridgeState() {
        cancelTasks();
        unregisterGameEvents();
        if (!getConfig().getBoolean("enabled", false)) {
            getLogger().info("Bridge desactivado (enabled: false).");
            return;
        }
        String presence = resolvePresenceWebhookUrl();
        if (presence.isBlank()) {
            getLogger().warning("enabled=true pero webhook-url vacío; latido y eventos requieren URL.");
        } else if (!WebhookSender.isAllowedHttpsUrl(presence)) {
            getLogger().severe("webhook-url no permitida (solo https:// hacia hosts públicos).");
        }
        ensureInstanceIdLoadedAsync(() -> {
            scheduleHeartbeat();
            scheduleStartupWebhook();
            registerGameEventsIfNeeded();
        });
    }

    private void ensureInstanceIdLoadedAsync(Runnable thenMainThread) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                instanceIdCached = loadOrCreateInstanceId();
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "No se pudo leer instance-id", e);
                instanceIdCached = "";
            }
            Bukkit.getScheduler().runTask(this, thenMainThread);
        });
    }

    private void scheduleHeartbeat() {
        String url = resolvePresenceWebhookUrl();
        if (url.isBlank() || !WebhookSender.isAllowedHttpsUrl(url)) {
            getLogger().info("Sin webhook-url de presencia válida: no se programa latido.");
            return;
        }
        int minutes = Math.max(15, getConfig().getInt("heartbeat-minutes", 360));
        long periodTicks = minutes * 60L * 20L;
        long delayTicks = Math.max(20L, getConfig().getInt("startup-delay-seconds", 60) * 20L);
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->
                Bukkit.getScheduler().runTaskAsynchronously(this, this::sendHeartbeat), delayTicks, periodTicks);
        heartbeatTaskId.set(id);
        getLogger().info("Latido programado cada " + minutes + " min (primer envío tras " + (delayTicks / 20) + " s).");
    }

    private void scheduleStartupWebhook() {
        if (!getConfig().getBoolean("events.server-startup", false)) {
            return;
        }
        String url = resolveEventsWebhookUrl();
        if (url.isBlank() || !WebhookSender.isAllowedHttpsUrl(url)) {
            return;
        }
        long delayTicks = Math.max(20L, getConfig().getInt("startup-delay-seconds", 60) * 20L);
        int id = Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::sendStartupWebhook, delayTicks);
        startupTaskId.set(id);
    }

    private void sendStartupWebhook() {
        if (!getConfig().getBoolean("enabled", false) || !getConfig().getBoolean("events.server-startup", false)) {
            return;
        }
        String url = resolveEventsWebhookUrl();
        if (url.isBlank() || !WebhookSender.isAllowedHttpsUrl(url)) {
            return;
        }
        String label = resolveServerLabel();
        String mc = safeMcVersion();
        boolean sf = Bukkit.getPluginManager().getPlugin("Slimefun") != null;
        Map<String, String> data = new java.util.LinkedHashMap<>();
        data.put("minecraftVersion", mc);
        data.put("slimefun", sf ? "yes" : "no");
        String desc = "Servidor **" + JsonStrings.truncate(label, 200) + "** en línea · MC `" + mc + "` · Slimefun: "
                + (sf ? "sí" : "no");
        deliverGameEvent("server_startup", "Arranque", desc, 3066993, data);
    }

    private void sendShutdownWebhookSync() {
        String url = resolveEventsWebhookUrl();
        if (url.isBlank() || !WebhookSender.isAllowedHttpsUrl(url)) {
            return;
        }
        String label = resolveServerLabel();
        Map<String, String> data = new java.util.LinkedHashMap<>();
        data.put("reason", "plugin_disable");
        String desc = "Apagado o recarga de **" + JsonStrings.truncate(label, 200) + "**.";
        byte[] body;
        if (WebhookSender.isDiscordWebhookUrl(url)) {
            String json = BridgePayloads.discordEmbed(
                    "Apagado",
                    desc,
                    15105570,
                    instanceIdCached + " · " + label,
                    data);
            body = json.getBytes(StandardCharsets.UTF_8);
        } else {
            String json = BridgePayloads.genericBridgeEvent(
                    "server_shutdown", instanceIdCached, label, data);
            body = json.getBytes(StandardCharsets.UTF_8);
        }
        WebhookSender.sendSyncBestEffort(this, url, body);
    }

    private void sendHeartbeat() {
        if (!getConfig().getBoolean("enabled", false)) {
            return;
        }
        String url = resolvePresenceWebhookUrl();
        if (url.isBlank() || !WebhookSender.isAllowedHttpsUrl(url)) {
            return;
        }
        url = url.trim();
        try {
            String instanceId = instanceIdCached.isEmpty() ? loadOrCreateInstanceId() : instanceIdCached;
            instanceIdCached = instanceId;
            String serverLabel = resolveServerLabel();
            boolean sf = Bukkit.getPluginManager().getPlugin("Slimefun") != null;
            String sfVer = "";
            if (sf) {
                var pl = Bukkit.getPluginManager().getPlugin("Slimefun");
                if (pl != null) {
                    sfVer = pl.getPluginMeta().getVersion();
                }
            }
            String paperMc = safeMcVersion();
            String bodyJson = buildHeartbeatPayload(url, instanceId, serverLabel, paperMc, sf, sfVer);
            WebhookSender.sendAsync(this, url, bodyJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "No se pudo preparar el latido", e);
        }
    }

    private String buildHeartbeatPayload(
            String url,
            String instanceId,
            String serverLabel,
            String paperMc,
            boolean slimefunPresent,
            String slimefunVersion
    ) {
        String pluginVer = getPluginMeta().getVersion();
        if (pluginVer == null || pluginVer.isEmpty()) {
            pluginVer = getDescription().getVersion();
        }
        String ts = Instant.now().toString();
        if (WebhookSender.isDiscordWebhookUrl(url)) {
            String desc = "**instanceId:** `" + instanceId + "` | **MC:** " + paperMc
                    + " | **Slimefun:** " + (slimefunPresent ? "sí (`" + slimefunVersion + "`)" : "no")
                    + " | **Plugin:** `" + pluginVer + "` | **UTC:** " + ts;
            return "{\"username\":\"DrakesLabPresence\",\"embeds\":[{\"title\":\"Drakes lab heartbeat\",\"description\":\""
                    + JsonStrings.esc(desc) + "\",\"color\":5814783,\"fields\":[{\"name\":\"Servidor\",\"value\":\""
                    + JsonStrings.esc(JsonStrings.truncate(serverLabel, 900)) + "\",\"inline\":false}]}]}";
        }
        return "{"
                + "\"schema\":\"" + SCHEMA_PRESENCE + "\","
                + "\"instanceId\":\"" + JsonStrings.esc(instanceId) + "\","
                + "\"serverLabel\":\"" + JsonStrings.esc(serverLabel) + "\","
                + "\"minecraftVersion\":\"" + JsonStrings.esc(paperMc) + "\","
                + "\"bukkitVersion\":\"" + JsonStrings.esc(Bukkit.getVersion()) + "\","
                + "\"pluginVersion\":\"" + JsonStrings.esc(pluginVer) + "\","
                + "\"slimefunPresent\":" + slimefunPresent + ","
                + "\"slimefunVersion\":\"" + JsonStrings.esc(slimefunVersion) + "\","
                + "\"timestampUtc\":\"" + JsonStrings.esc(ts) + "\""
                + "}";
    }

    private String loadOrCreateInstanceId() throws java.io.IOException {
        File dataFile = new File(getDataFolder(), "data.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        String id = data.getString("instance-id");
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
            data.set("instance-id", id);
            data.save(dataFile);
        }
        return id;
    }

    private String safeMcVersion() {
        try {
            return Bukkit.getMinecraftVersion();
        } catch (Throwable ignored) {
            return Bukkit.getVersion();
        }
    }

    String resolvePresenceWebhookUrl() {
        String u = getConfig().getString("webhook-url", "");
        return u != null ? u.trim() : "";
    }

    String resolveEventsWebhookUrl() {
        String ev = getConfig().getString("webhook-events-url", "");
        if (ev != null && !ev.isBlank()) {
            return ev.trim();
        }
        return resolvePresenceWebhookUrl();
    }

    private String resolveServerLabel() {
        String serverLabel = getConfig().getString("server-label", "");
        if (serverLabel == null || serverLabel.isBlank()) {
            serverLabel = Bukkit.getServer().getName();
        }
        return JsonStrings.truncate(serverLabel, 500);
    }

    private void registerGameEventsIfNeeded() {
        if (!anyPlayerLikeEventEnabled()) {
            return;
        }
        gameEventListener = new GameEventListener(this);
        Bukkit.getPluginManager().registerEvents(gameEventListener, this);
        getLogger().info("Eventos de jugador hacia Discord: listener registrado.");
    }

    private boolean anyPlayerLikeEventEnabled() {
        return getConfig().getBoolean("events.player-join", false)
                || getConfig().getBoolean("events.player-quit", false)
                || getConfig().getBoolean("events.player-death", false)
                || getConfig().getBoolean("events.advancement", false);
    }

    private void unregisterGameEvents() {
        if (gameEventListener != null) {
            HandlerList.unregisterAll(gameEventListener);
            gameEventListener = null;
        }
    }

    private void cancelTasks() {
        int h = heartbeatTaskId.getAndSet(-1);
        if (h != -1) {
            Bukkit.getScheduler().cancelTask(h);
        }
        int s = startupTaskId.getAndSet(-1);
        if (s != -1) {
            Bukkit.getScheduler().cancelTask(s);
        }
    }
}
