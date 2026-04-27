# DrakesLabPresence

Addon **opcional** (DrakesCraft Labs) para saber **dónde** se está ejecutando el laboratorio (Paper + tu stack Slimefun Drake) **sin meter secretos en el JAR**.

## Qué hace

- Genera un `instance-id` estable en `plugins/DrakesLabPresence/data.yml` (por servidor / carpeta de datos).
- Si `enabled: true` y `webhook-url` apunta a un **HTTPS** público, envía periódicamente un JSON (o un embed de **Discord**) con: nombre del servidor, versión de MC, si Slimefun está cargado, versión del plugin, marca de tiempo UTC.
- Opcional: cabecera `X-Drakes-Signature: sha256=…` (HMAC del cuerpo) si configuras `shared-secret` (misma clave en tu relay).

## Qué **no** hace (a propósito)

- No envía correo ni abre issues en GitHub **directamente**: no es seguro incrustar tokens SMTP ni PAT de GitHub en un plugin público.
- No “espía” jugadores ni IPs en el JSON (la IP origen la ve solo quien recibe el POST HTTP).

## Notificación rápida: Discord

1. En tu servidor de Discord: *Canal → Editar → Integraciones → Webhooks → Nuevo webhook*.
2. Copia la URL `https://discord.com/api/webhooks/...`.
3. En `plugins/DrakesLabPresence/config.yml`: `enabled: true`, `webhook-url: "<pega aquí>"`.
4. Reinicia o `/drakeslabpresence reload`. Tras `startup-delay-seconds` deberías ver un mensaje del webhook.

## GitHub o correo (relay)

Monta un endpoint **tuyo** (Cloudflare Worker, Fly.io, n8n, small VPS) que:

1. Reciba el POST (y verifique `X-Drakes-Signature` si usas `shared-secret`).
2. Llame a la API de GitHub (`issues`, `repository_dispatch`) con un **PAT** que solo vive en el servidor del relay, **no** en Minecraft.

Ejemplo de cuerpo genérico (no Discord):

```json
{
  "schema": "drakeslab-presence/1",
  "instanceId": "uuid",
  "serverLabel": "paper",
  "minecraftVersion": "1.21.1",
  "bukkitVersion": "...",
  "pluginVersion": "1.0.0-DRAKE-SNAPSHOT",
  "slimefunPresent": true,
  "slimefunVersion": "…",
  "timestampUtc": "2026-04-28T12:00:00Z"
}
```

**Correo:** muchos relays (Zapier, Make, n8n) aceptan webhook HTTP y disparan “Send email”.

## Ética y operadores

Por defecto `enabled: false`. Solo quien controla el servidor debe activar el envío; avisa en tu documentación de staff si lo usas en un survival público.

## English summary

Opt-in heartbeat to **your** HTTPS URL (Discord webhook works out of the box). Optional HMAC header for a private relay. No GitHub tokens or SMTP credentials ship inside the plugin; use your own small service to forward to GitHub Issues or email.
