<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Gastronomicon

A Slimefun addon that adds even more foodstuffs to the game. Best used along with [ExoticGarden](https://github.com/TheBusyBiscuit/ExoticGarden).

This addon automatically integrates with DynaTech for crop automation. Both DynaTech and ExoticGarden integrations can be manually disabled in `config.yml`

## Links

- Wiki: <https://schn.pages.dev/gastronomicon>
- Download: <https://blob.build/project/Gastronomicon>

## Requirements

- Paper (or its forks)
- Minecraft 1.17+
- Java 17+

*Exotic Garden is not required but heavily recommended!*

## API

API documentation can be found on the wiki at <https://schn.pages.dev/gastronomicon/custom-food>

## Credits

Some head textures were taken and/or modified from [minecraft-heads.com](https://minecraft-heads.com/)

[![minecraft-heads banner](https://images.minecraft-heads.com/banners/minecraft-heads.webp)](https://minecraft-heads.com/)

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
