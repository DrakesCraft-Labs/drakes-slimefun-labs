package com.github.drakescraft_labs.drakeslabpresence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.logging.Level;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * POST JSON to an operator-controlled HTTPS URL (Discord webhook or relay). SSRF-hardened.
 */
public final class WebhookSender {

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private WebhookSender() {}

    static boolean isDiscordWebhookUrl(String url) {
        if (url == null) {
            return false;
        }
        String u = url.trim().toLowerCase(Locale.ROOT);
        return u.contains("discord.com/api/webhooks") || u.contains("discordapp.com/api/webhooks");
    }

    static boolean isAllowedHttpsUrl(String url) {
        if (url == null || !url.trim().startsWith("https://")) {
            return false;
        }
        try {
            URI u = URI.create(url.trim());
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

    static void sendAsync(JavaPlugin plugin, String url, byte[] bodyUtf8) {
        if (!isAllowedHttpsUrl(url)) {
            return;
        }
        String trimmed = url.trim();
        try {
            var builder = HttpRequest.newBuilder(URI.create(trimmed))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bodyUtf8));
            appendSignature(plugin, builder, bodyUtf8);
            HttpRequest req = builder.build();
            HTTP.sendAsync(req, HttpResponse.BodyHandlers.discarding()).whenComplete((resp, err) -> {
                if (err != null) {
                    plugin.getLogger().log(Level.WARNING, "Webhook HTTP falló: " + err.getMessage(), err);
                } else if (plugin.getConfig().getBoolean("debug", false)) {
                    int code = resp != null ? resp.statusCode() : -1;
                    plugin.getLogger().info("Webhook enviado, HTTP " + code);
                }
            });
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "No se pudo enviar webhook", e);
        }
    }

    /**
     * Best-effort synchronous POST for shutdown (scheduler may already be stopping).
     */
    static void sendSyncBestEffort(JavaPlugin plugin, String url, byte[] bodyUtf8) {
        if (!isAllowedHttpsUrl(url)) {
            return;
        }
        String trimmed = url.trim();
        try {
            var builder = HttpRequest.newBuilder(URI.create(trimmed))
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bodyUtf8));
            appendSignature(plugin, builder, bodyUtf8);
            HTTP.send(builder.build(), HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            plugin.getLogger().log(Level.FINE, "Webhook de apagado no entregado: " + e.getMessage());
        }
    }

    private static void appendSignature(JavaPlugin plugin, HttpRequest.Builder builder, byte[] bodyUtf8)
            throws Exception {
        String secret = plugin.getConfig().getString("shared-secret", "");
        if (secret == null || secret.isBlank()) {
            return;
        }
        String sig = hmacSha256Hex(secret, bodyUtf8);
        builder.header("X-Drakes-Signature", "sha256=" + sig);
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
