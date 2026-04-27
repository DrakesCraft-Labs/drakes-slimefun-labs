package com.github.drakescraft_labs.drakeslabpresence;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * Opt-in HTTPS heartbeat. No third-party URLs or secrets are embedded; operators configure their own webhook.
 */
public final class DrakesLabPresencePlugin extends JavaPlugin {

    private static final String SCHEMA = "drakeslab-presence/1";
    private static final HttpClient HTTP = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

    private final AtomicInteger heartbeatTaskId = new AtomicInteger(-1);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getCommand("drakeslabpresence") != null) {
            getCommand("drakeslabpresence").setExecutor(this::onCommand);
        }
        scheduleHeartbeat();
    }

    @Override
    public void onDisable() {
        cancelHeartbeat();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("drakeslabpresence.admin")) {
            return false;
        }
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            reloadConfig();
            scheduleHeartbeat();
            sender.sendMessage("[DrakesLabPresence] Config recargada y latido reprogramado.");
            return true;
        }
        sender.sendMessage("[DrakesLabPresence] Uso: /drakeslabpresence reload");
        return true;
    }

    private void scheduleHeartbeat() {
        cancelHeartbeat();
        if (!getConfig().getBoolean("enabled", false)) {
            getLogger().info("Telemetry desactivada (enabled: false). Activala en config.yml + webhook-url.");
            return;
        }
        String url = getConfig().getString("webhook-url", "");
        if (url == null || url.isBlank()) {
            getLogger().warning("enabled=true pero webhook-url vacío; no se enviará nada.");
            return;
        }
        if (!isAllowedHttpsUrl(url.trim())) {
            getLogger().severe("webhook-url no permitida (solo https:// hacia hosts públicos). Revisa config.yml.");
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

    private void cancelHeartbeat() {
        int id = heartbeatTaskId.getAndSet(-1);
        if (id != -1) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    private void sendHeartbeat() {
        if (!getConfig().getBoolean("enabled", false)) {
            return;
        }
        String url = getConfig().getString("webhook-url", "");
        if (url == null || url.isBlank() || !isAllowedHttpsUrl(url.trim())) {
            return;
        }
        url = url.trim();

        try {
            String instanceId = loadOrCreateInstanceId();
            String serverLabel = getConfig().getString("server-label", "");
            if (serverLabel == null || serverLabel.isBlank()) {
                serverLabel = Bukkit.getServer().getName();
            }
            boolean sf = Bukkit.getPluginManager().getPlugin("Slimefun") != null;
            String sfVer = "";
            if (sf) {
                var pl = Bukkit.getPluginManager().getPlugin("Slimefun");
                if (pl != null) {
                    sfVer = pl.getPluginMeta().getVersion();
                }
            }
            String paperMc = safeMcVersion();
            String bodyJson = buildPayloadJson(url, instanceId, serverLabel, paperMc, sf, sfVer);
            String secret = getConfig().getString("shared-secret", "");
            byte[] bodyBytes = bodyJson.getBytes(StandardCharsets.UTF_8);

            var builder = HttpRequest.newBuilder(URI.create(url))
                    .timeout(java.time.Duration.ofSeconds(20))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bodyBytes));
            if (secret != null && !secret.isBlank()) {
                String sig = hmacSha256Hex(secret, bodyBytes);
                builder.header("X-Drakes-Signature", "sha256=" + sig);
            }
            HttpRequest req = builder.build();
            HTTP.sendAsync(req, HttpResponse.BodyHandlers.discarding()).whenComplete((resp, err) -> {
                if (err != null) {
                    getLogger().log(Level.WARNING, "Latido HTTP falló: " + err.getMessage(), err);
                } else if (getConfig().getBoolean("debug", false)) {
                    int code = resp != null ? resp.statusCode() : -1;
                    getLogger().info("Latido enviado, HTTP " + code);
                }
            });
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "No se pudo preparar el latido", e);
        }
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

    private String buildPayloadJson(
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
        if (url.contains("discord.com/api/webhooks") || url.contains("discordapp.com/api/webhooks")) {
            String desc = "**instanceId:** `" + instanceId + "` | **MC:** " + paperMc
                    + " | **Slimefun:** " + (slimefunPresent ? "sí (`" + slimefunVersion + "`)" : "no")
                    + " | **Plugin:** `" + pluginVer + "` | **UTC:** " + ts;
            return "{\"username\":\"DrakesLabPresence\",\"embeds\":[{\"title\":\"Drakes lab heartbeat\",\"description\":\""
                    + jstr(desc) + "\",\"color\":5814783,\"fields\":[{\"name\":\"Servidor\",\"value\":\""
                    + jstr(truncate(serverLabel, 900)) + "\",\"inline\":false}]}]}";
        }
        return "{"
                + "\"schema\":\"" + SCHEMA + "\","
                + "\"instanceId\":\"" + jstr(instanceId) + "\","
                + "\"serverLabel\":\"" + jstr(serverLabel) + "\","
                + "\"minecraftVersion\":\"" + jstr(paperMc) + "\","
                + "\"bukkitVersion\":\"" + jstr(Bukkit.getVersion()) + "\","
                + "\"pluginVersion\":\"" + jstr(pluginVer) + "\","
                + "\"slimefunPresent\":" + slimefunPresent + ","
                + "\"slimefunVersion\":\"" + jstr(slimefunVersion) + "\","
                + "\"timestampUtc\":\"" + jstr(ts) + "\""
                + "}";
    }

    private static String jstr(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }

    private static boolean isAllowedHttpsUrl(String url) {
        if (!url.startsWith("https://")) {
            return false;
        }
        try {
            URI u = URI.create(url);
            String host = u.getHost();
            if (host == null) {
                return false;
            }
            String h = host.toLowerCase(Locale.ROOT);
            if (h.equals("localhost") || h.equals("127.0.0.1") || h.endsWith(".local")) {
                return false;
            }
            if (h.startsWith("10.") || h.startsWith("192.168.") || isPrivate172Host(h)) {
                return false;
            }
            return u.getScheme().equalsIgnoreCase("https");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isPrivate172Host(String host) {
        if (!host.startsWith("172.")) {
            return false;
        }
        String[] p = host.split("\\.");
        if (p.length < 2) {
            return false;
        }
        try {
            int oct2 = Integer.parseInt(p[1]);
            return oct2 >= 16 && oct2 <= 31;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String hmacSha256Hex(String secret, byte[] body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(body);
        StringBuilder sb = new StringBuilder(raw.length * 2);
        for (byte b : raw) {
            sb.append(String.format(Locale.ROOT, "%02x", b));
        }
        return sb.toString();
    }
}
