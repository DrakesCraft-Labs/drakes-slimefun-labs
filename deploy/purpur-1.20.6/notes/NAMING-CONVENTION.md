# Naming Convention for Plugins

Para mantener coherencia operativa y saber rápidamente la procedencia y madurez de un jar, usaremos el siguiente formato estricto dentro de `plugins-confirmed` y `plugins-testing`.

### Formato General:
`[PluginName]-[Version]-[Origin/Author]-[MC-Version].jar`

### Ejemplos:
- **Port local (Drake):** `Slimefun-v4.3-Drake-1.20.6.jar`
- **Descarga directa oficial:** `ExoticGarden-v1.6-Official-1.20.6.jar`
- **Fork Comunitario:** `MobCapturer-v2.0-Community-1.20.6.jar`

### Reglas:
1. No usar espacios en los nombres de los Jars.
2. Añadir siempre la versión del plugin (por más que cambie internamente, ayuda visualmente).
3. Etiquetar la versión de Minecraft al final (para evitar accidentes al migrar a una 1.21).

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
