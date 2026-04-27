# DrakesLabPresence

Addon **opcional** (DrakesCraft Labs) para enlazar tu servidor Paper con **Discord u otros relays HTTP** mediante **webhooks HTTPS** que tú configuras. Pensado como capa ligera de “puente” **solo saliente** (servidor → Discord), sin meter tokens de bot ni secretos de terceros en el JAR.

## Qué hace hoy

1. **Presencia (latido)** — Igual que antes: si `enabled: true` y `webhook-url` es HTTPS público, envía cada `heartbeat-minutes` un JSON o embed con `instance-id`, versión de MC, Slimefun, etc. (`schema: drakeslab-presence/1` en relays no Discord).
2. **Eventos de servidor** (opt-in) — `events.server-startup` / `events.server-shutdown`: un POST al arrancar (tras `startup-delay-seconds`) y uno síncrono best-effort al apagar/recargar.
3. **Eventos de juego** (opt-in) — Hacia `webhook-events-url` **o** la misma `webhook-url` si no definís canal aparte:
   - `player-join`, `player-quit`, `player-death`, `advancement`
   - `events.rate-limit-per-minute` para no inundar el canal
   - `events.show-player-uuid` para incluir UUID en payload/embed
4. **Relay genérico** — Si la URL **no** es de Discord, los eventos usan `schema: drakeslab-bridge/1` con campo `event` y `data` { clave: valor }.
5. **Firma opcional** — Misma cabecera `X-Drakes-Signature: sha256=…` (HMAC del cuerpo) si configurás `shared-secret`.
6. **Comandos** — `/drakeslabpresence reload` y `/drakeslabpresence test` (embed o JSON según URL).

## Qué **no** hace (y alternativas)

- **No** es un bot de Discord con sesión permanente: no lee mensajes del canal ni ejecuta comandos de Discord → Minecraft. Eso requiere **token de bot** + librería (p. ej. JDA) o un servicio aparte; no lo empaquetamos aquí por seguridad y tamaño.
- Para chat bidireccional masivo y enlace maduro servidor ↔ Discord, la opción habitual en el ecosistema es **[DiscordSRV](https://github.com/DiscordSRV/DiscordSRV)** u otro plugin establecido; DrakesLabPresence puede **complementar** (p. ej. solo presencia + alertas técnicas a un webhook privado).
- **GitHub / correo directo** — Igual que antes: montá un **relay** (Worker, n8n, VPS) que reciba el POST y llame a APIs con credenciales que **no** viven en el servidor de Minecraft.

## Configuración rápida (Discord)

1. Webhook en el canal de **presencia** (latidos poco frecuentes).
2. (Opcional) Otro webhook en canal de **alertas / eventos** y poné `webhook-events-url`.
3. `enabled: true`, activá solo los flags de `events` que quieras (los de jugador van **false** por defecto por privacidad).
4. Reinicio o `/drakeslabpresence reload`. Probad con `/drakeslabpresence test`.

## Privacidad y operadores

Por defecto no se reenvían entradas/salidas/muertes. Si las activás, informá en las normas del servidor / staff. El rate limit evita abusos por farms de muertes o picos de jugadores.

## English summary

Lightweight **outbound-only** bridge: periodic presence heartbeat, optional startup/shutdown posts, optional player/advancement events to **your** HTTPS webhooks (Discord or generic JSON `drakeslab-bridge/1`). Optional HMAC. No Discord bot token in the JAR; use DiscordSRV or a custom bot service for full bidirectional chat.
