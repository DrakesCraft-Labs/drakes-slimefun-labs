package com.github.drakescraft_labs.drakeslabpresence;

import java.time.Instant;
import java.util.Map;
import org.bukkit.Bukkit;

/**
 * Cuerpos JSON para Discord (webhook) o relays genéricos (schema drakeslab-bridge/1).
 */
final class BridgePayloads {

    static final String SCHEMA_BRIDGE = "drakeslab-bridge/1";

    private BridgePayloads() {}

    static String discordEmbed(
            String title,
            String description,
            int colorArgb,
            String footer,
            Map<String, String> inlineFields
    ) {
        StringBuilder fields = new StringBuilder();
        if (inlineFields != null && !inlineFields.isEmpty()) {
            for (Map.Entry<String, String> e : inlineFields.entrySet()) {
                if (fields.length() > 0) {
                    fields.append(',');
                }
                fields.append("{\"name\":\"")
                        .append(JsonStrings.esc(JsonStrings.truncate(e.getKey(), 200)))
                        .append("\",\"value\":\"")
                        .append(JsonStrings.esc(JsonStrings.truncate(e.getValue(), 900)))
                        .append("\",\"inline\":true}");
            }
        }
        String fieldBlock = fields.length() > 0 ? ",\"fields\":[" + fields + "]" : "";
        return "{\"username\":\"DrakesLabPresence\",\"embeds\":[{\"title\":\""
                + JsonStrings.esc(JsonStrings.truncate(title, 250))
                + "\",\"description\":\""
                + JsonStrings.esc(JsonStrings.truncate(description, 3500))
                + "\",\"color\":"
                + colorArgb
                + ",\"footer\":{\"text\":\""
                + JsonStrings.esc(JsonStrings.truncate(footer, 1800))
                + "\"}"
                + fieldBlock
                + "}]}";
    }

    static String genericBridgeEvent(
            String eventKey,
            String instanceId,
            String serverLabel,
            Map<String, String> dataPairs
    ) {
        StringBuilder data = new StringBuilder();
        if (dataPairs != null) {
            for (Map.Entry<String, String> e : dataPairs.entrySet()) {
                if (data.length() > 0) {
                    data.append(',');
                }
                data.append('"')
                        .append(JsonStrings.esc(e.getKey()))
                        .append("\":\"")
                        .append(JsonStrings.esc(JsonStrings.truncate(e.getValue(), 2000)))
                        .append('"');
            }
        }
        String ts = Instant.now().toString();
        return "{"
                + "\"schema\":\"" + SCHEMA_BRIDGE + "\","
                + "\"event\":\"" + JsonStrings.esc(eventKey) + "\","
                + "\"instanceId\":\"" + JsonStrings.esc(instanceId) + "\","
                + "\"serverLabel\":\"" + JsonStrings.esc(JsonStrings.truncate(serverLabel, 500)) + "\","
                + "\"bukkitVersion\":\"" + JsonStrings.esc(JsonStrings.truncate(Bukkit.getVersion(), 500)) + "\","
                + "\"timestampUtc\":\"" + JsonStrings.esc(ts) + "\","
                + "\"data\":{" + data + "}"
                + "}";
    }
}
