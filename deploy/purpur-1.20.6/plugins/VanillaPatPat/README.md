# VanillaPatPat

Un plugin de servidor de PaperMC/Purpur que replica la mecánica de acariciar mobs del famoso mod [PatPat](https://github.com/LopyMine/PatPat) — sin necesidad de que los jugadores instalen ningún mod en el cliente.

## ¿Cómo funciona?

1. **Agáchate** (Shift) frente a cualquier mob vivo.
2. Haz **clic derecho** con la **mano vacía**.
3. ¡Tu personaje dará una palmada, saldrán corazones y escucharás un ronroneo!

## Características

- ✅ 100% server-side — no requiere mods en el cliente
- ✅ Compatible con cualquier mob vivo (animales, monstruos, etc.)
- ✅ Cooldown de 500ms anti-spam
- ✅ Partículas de corazón sobre la cabeza del mob
- ✅ Sonido de ronroneo (`ENTITY_CAT_PURR`)
- ✅ Mensaje en la Action Bar

## Compatibilidad

| Versión de Minecraft | Estado |
|---|---|
| 1.20.6 | ✅ Soportado |
| 1.21.x  | No testeado (probablemente compatible) |

**Server Software compatibles:** Paper, Purpur, Spigot (y forks).

## Instalación

1. Descarga el `.jar` desde la sección [Releases](../../releases).
2. Colócalo en la carpeta `plugins/` de tu servidor.
3. Reinicia el servidor.
4. ¡Listo! No requiere configuración adicional.

## Créditos

Inspirado en el mod original **PatPat** de [LopyMine](https://github.com/LopyMine/PatPat). Este plugin es una reimplementación independiente del lado del servidor usando la API de Bukkit/Paper.

## Licencia

MIT

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
