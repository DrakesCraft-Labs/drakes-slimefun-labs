# Drakes Slimefun Labs

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.x](https://img.shields.io/badge/Paper-1.21.x-3b82f6?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![CI Monorepo 1.21](https://img.shields.io/badge/CI-Monorepo%201.21-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions/workflows/ci-monorepo-121.yml)
[![Monorepo](https://img.shields.io/badge/Monorepo-Slimefun%20Ecosystem-7c3aed?style=for-the-badge)](#inventario-completo-de-modulos-y-plugins)
[![GPLv3](https://img.shields.io/badge/License-GPLv3-ef4444?style=for-the-badge)](LICENSE)

**Rama `1.21-latin` — línea estable.** Un solo reactor **Maven + Gradle** (Java **21**) con **Slimefun 4 Drake**, Dough, librerías internas y **muchos addons** ya alineados a la API **Paper 1.21.x**. El objetivo del laboratorio es simple: *que compile en CI, que arranque en Paper real y que el pack se pueda desplegar*; el resto es mejora continua.

### Listo en el laboratorio (no bloquea seguir iterando)

| Pilar | Detalle breve |
|-------|----------------|
| **Build** | Todos los módulos del reactor cubiertos por [CI Monorepo 1.21](.github/workflows/ci-monorepo-121.yml) (`maven_full_reactor`, `foundation`, `gradle_green`). |
| **Smoke** | Servidor Paper **1.21.1** y **1.21.11** (perfiles `foundation`, `foundation-paper-12111`, `monorepo-all`, …); ver [`scripts/smoke/README.md`](scripts/smoke/README.md). |
| **Distribución** | [Release monorepo JARs](.github/workflows/release-monorepo-jars.yml): un **ZIP** con los plugins del reactor (manual en Actions); guía en [`docs/github-maintenance.md`](docs/github-maintenance.md). |

### Qué queda a propósito “para después”

No es deuda del reactor: es **vida real** en el juego.

- **Pasta fina addon por addon** (lore, recetas raras, choques con otros plugins): lo van encontrando **jugadores**, **Chagui** y quien cura el survival.
- **Bugs de gameplay** → [Issues](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/issues) y el tablero de la org.
- **Servidor de referencia (Chile):** **[DrakesCraft](https://drakescraft.cl)** — survival donde vive buena parte del stack; conexión típica **`play.drakescraft.cl`** / **`mc.drakescraft.cl`** (según lo que muestre el cliente al unirse). Ahí se valida el pack con carga real.

### Rama experimental **26.x** (`26.X-ToTheStars`)

**Qué es:** una línea de trabajo paralela para el salto a **Minecraft / Paper API 26.x** (artefactos tipo `26.1.x.build.*-alpha` en el repo de Paper). En esa rama el `pom.xml` raíz sigue con **`paper.version` 1.21.x por defecto** y se añade el perfil Maven **`paper-26-preview`** para compilar contra la API 26 cuando toque (`mvn -Ppaper-26-preview`). La documentación allí incluye avisos en los `.md` y la guía [`paper-26-base.md`](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/blob/26.X-ToTheStars/docs/paper-26-base.md) (solo en esa rama).

**Qué no es:** un reemplazo de `1.21-latin` de la noche a la mañana. Los **releases**, el **smoke** pensado para operadores y el **pack estable** siguen saliendo de **`1.21-latin`**.

**Calendencia humana:** el porte agresivo a **1.21.x** fue extremadamente exigente (“casi nos mata”). Se contempla una ventana aproximada de **un mes** antes de retomar el porte **26.x** con el mismo ritmo de sprint; en ese intervalo el foco sigue siendo **mantener verde** `1.21-latin`, Issues, smoke cuando haga falta y el survival **[DrakesCraft](https://drakescraft.cl)**. El trabajo 26.x avanza **sin presión** en `26.X-ToTheStars` hasta que el equipo vuelva a tener cabeza para API breaking.

---

## Enlaces rapidos

| Recurso | URL |
|---|---|
| **Indice de documentacion** | [`docs/README.md`](docs/README.md) |
| **Smoke (perfiles, Paper, logs)** | [`scripts/smoke/README.md`](scripts/smoke/README.md) |
| **Release ZIP monorepo (Actions)** | [`.github/workflows/release-monorepo-jars.yml`](.github/workflows/release-monorepo-jars.yml) |
| **Mantenimiento GitHub** (runs, PRs, alertas) | [`docs/github-maintenance.md`](docs/github-maintenance.md) |
| **GitHub Project (estado org)** | [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) |
| **Actions (CI)** | [Workflow runs](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions) |
| **Issues** | [Issues](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/issues) |
| **Matriz detallada (generada)** | [`docs/es/PLUGIN_MATRIX.md`](docs/es/PLUGIN_MATRIX.md) |
| **Como sincronizar el tablero** | [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md) |
| **Docs ES (indice)** | [`docs/es/home.md`](docs/es/home.md) |
| **Docs EN (indice)** | [`docs/en/home.md`](docs/en/home.md) |
| **Prompt corto para IA** | [`docs/es/ai-start-prompt.md`](docs/es/ai-start-prompt.md) · [`docs/en/ai-start-prompt.md`](docs/en/ai-start-prompt.md) |
| **Servidor de referencia (Chile)** | [drakescraft.cl](https://drakescraft.cl) |

> La matriz y la tabla larga de módulos de este README se generan con `python scripts/generate_plugin_matrix.py` para evitar desalineación manual.

---

## Tablero GitHub Projects

El estado de alto nivel del porteo se gestiona en el [**Project 1 de la organizacion**](https://github.com/orgs/DrakesCraft-Labs/projects/1).

Para actualizarlo desde tu equipo con la CLI, autoriza scopes de Projects:

```bash
gh auth refresh -h github.com -s read:project,project
```

Luego alinea cada tarjeta con la columna **Estado** y las **Observaciones** de la tabla inferior (o del archivo `docs/es/PLUGIN_MATRIX.md`). Guia paso a paso: [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md).

---

## Resumen de estado (auditable)

> Corte generado automáticamente a partir de `ci-monorepo-121.yml`, `pom.xml`, `settings.gradle.kts` y el script de matriz. Los números miden **cobertura de build en CI**, no “porcentaje de perfección” del juego en survival.

| Estado | Cantidad | Significado |
|---:|---:|---|
| **Listo (CI)** | **86** | Aparece en `ci-monorepo-121.yml` (`maven_full_reactor`, `foundation` o `gradle_green`) y compila alli. |
| **Listo (local)** | **0** | `mvn compile -fae` o `gradlew <proyecto>:compileJava` verde en revision auditada; **pendiente** promover a un job de `ci-monorepo-121.yml`. |
| **En curso** | **0** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **0** | Fallo reproducible de compilacion en el reactor local. |
| **Total modulos** | **86** | Maven + Gradle en reactor; ver conteo exacto en esta fila. |

### Barra de proporcion (CI vs resto)

```text
Listo CI:     86/86  (100.0%)
Listo local:  0/86
En curso:     0/86
Bloqueado:    0/86
```

---

## Metodologia (criterios)

1. **Listo (CI)**: modulo cubierto por un job de [`ci-monorepo-121.yml`](.github/workflows/ci-monorepo-121.yml) (`foundation`, `maven_full_reactor`, `gradle_green`).
2. **Listo (local)**: compilacion Maven/Gradle exitosa en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build local documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`, bridges locales de compatibilidad para addons Gradle (`MenuBlock`, `TickingMenuBlock`, `DrakeItemBuilderCompat`) y bridges BusyBiscuit en Slimefun core (`io.github.thebusybiscuit.slimefun4.*`).

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |
|---|---|:---:|---|---|---|
| Cultivation_Updated | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/Cultivation_Updated` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| InfinityLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/InfinityLib` | Stack base Paper 1.21.1 + Java 21. |
| LiteXpansion | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/LiteXpansion` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Networks_Better_Compatibility | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/Networks_Better_Compatibility` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SMG | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/SMG` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SefiLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/SefiLib` | Stack base Paper 1.21.1 + Java 21. |
| SlimeTinker | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/SlimeTinker` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Supreme | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/Supreme` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| TranscEndence | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/TranscEndence` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| AdvancedTech | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/AdvancedTech` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| AlchimiaVitae | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/AlchimiaVitae` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Better-Nuclear-Generator | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Better-Nuclear-Generator` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| CompressionCraft | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/CompressionCraft` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| CrystamaeHistoria | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/CrystamaeHistoria` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DankTech2 | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/DankTech2` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DyeBench | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/DyeBench` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| EMCTech | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/EMCTech` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Element-Manipulation | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Element-Manipulation` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraTools | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/ExtraTools` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FN-FAL-s-Amplifications | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/FN-FAL-s-Amplifications` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FlowerPower | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/FlowerPower` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FoxyMachines | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/FoxyMachines` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Gastronomicon | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Gastronomicon` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| GeneticChickengineering-Reborn | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/GeneticChickengineering-Reborn` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Geyser-Slimefun-Heads | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Geyser-Slimefun-Heads` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| HeadLimiter | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/HeadLimiter` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Liquid | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Liquid` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Magic-8-Ball | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Magic-8-Ball` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MapJammers | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MapJammers` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MiniBlocks | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MiniBlocks` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MissileWarfare | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MissileWarfare` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MoreResearches | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MoreResearches` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Netheopoiesis | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Netheopoiesis` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| PotionExpansion | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/PotionExpansion` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Quaptics | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Quaptics` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| RelicsOfCthonia | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/RelicsOfCthonia` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| RykenSlimeCustomizer-EN | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/RykenSlimeCustomizer-EN` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SaneCrafting | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SaneCrafting` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SfBetterChests | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SfBetterChests` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SfChunkInfo | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SfChunkInfo` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Simple-Storage | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Simple-Storage` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeCustomizer | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimeCustomizer` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeFrame | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimeFrame` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeHUD | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimeHUD` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimefunAdvancements | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimefunAdvancements` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SmallSpace | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SmallSpace` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SpiritsUnchained | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SpiritsUnchained` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| UltimateGenerators2 | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/UltimateGenerators2` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| VillagerTrade | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/VillagerTrade` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| VillagerUtil | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/VillagerUtil` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Wildernether | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Wildernether` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| WorldEditSlimefun | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/WorldEditSlimefun` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| dough-core | libreria | Listo (CI) | CI Monorepo · foundation | `sources/dough-core` | Stack base Paper 1.21.1 + Java 21. |
| commons-lang-drake-patched | interno | Listo (CI) | CI Monorepo · foundation | `sources/internal-metadata/patches/commons-lang-drake-patched` | Stack base Paper 1.21.1 + Java 21. |
| ColoredEnderChests | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ColoredEnderChests` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DyedBackpacks | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/DyedBackpacks` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DynaTech | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/DynaTech` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| EcoPower | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/EcoPower` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ElectricSpawners | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ElectricSpawners` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExoticGarden | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExoticGarden` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraGear | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExtraGear` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraHeads | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExtraHeads` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraUtils | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExtraUtils` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FluffyMachines | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/FluffyMachines` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| GlobalWarming | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/GlobalWarming` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| HardcoreSlimefun | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/HardcoreSlimefun` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| HotbarPets | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/HotbarPets` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| InfinityExpansion | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/InfinityExpansion` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MobCapturer | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/MobCapturer` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| PrivateStorage | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/PrivateStorage` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SFCalc | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SFCalc` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SFMobDrops | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SFMobDrops` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SimpleUtils | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SimpleUtils` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeChem | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimeChem` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimefunOreChunks | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimefunOreChunks` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimyRepair | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimyRepair` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimyTreeTaps | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimyTreeTaps` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SoulJars | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SoulJars` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SoundMuffler | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SoundMuffler` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| luckyblocks-sf | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/luckyblocks-sf` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Slimefun4-src | core | Listo (CI) | CI Monorepo · foundation | `sources/slimefun-core/Slimefun4-src` | Stack base Paper 1.21.1 + Java 21. |
| Galactifun | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/batch-2-expansion/Galactifun` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| Bump | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/Bump` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| CustomItemGenerators | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/CustomItemGenerators` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| FastMachines | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/FastMachines` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| SlimefunTranslation | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/SlimefunTranslation` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |

---

## Arbol resumido del monorepo

```text
drakes-slimefun-labs/
├─ .github/workflows/     # ci-monorepo-121.yml (CI unificado)
├─ docs/                  # guias ES/EN + PROJECT_BOARD_SYNC + PLUGIN_MATRIX
├─ scripts/               # generate_plugin_matrix.py, port_paper_121.py, manager.py
├─ sources/
│  ├─ slimefun-core/Slimefun4-src
│  ├─ dough-core/
│  ├─ batch-2-expansion/
│  ├─ community-addons/
│  ├─ repos-to-port/
│  └─ internal-metadata/
├─ pom.xml
└─ settings.gradle.kts
```

---

## Comandos utiles

```bash
# Regenerar matriz + README (tabla alineada)
python scripts/generate_plugin_matrix.py

# Parche masivo Paper 1.21.1 (dry-run primero)
python scripts/port_paper_121.py --dry-run --path sources/community-addons/MiAddon

# Smoke completo local (Paper + JARs; tarda; subir timeout si hace falta)
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 420

# Build base Maven (ejemplo)
mvn -B clean install -DskipTests -pl sources/dough-core,sources/slimefun-core/Slimefun4-src -am

# Verificacion local del corte completo
mvn -B -DskipTests compile -fae
./gradlew :sources:batch-2-expansion:Galactifun:compileJava :sources:community-addons:Bump:compileJava :sources:community-addons:CustomItemGenerators:compileJava :sources:community-addons:FastMachines:compileJava :sources:community-addons:SlimefunTranslation:compileJava --no-daemon

# Dependencia local necesaria para FastMachines cuando Gradle resuelve InfinityExpansion-drake
mvn -B -DskipTests install -pl sources/repos-to-port/InfinityExpansion -am
```

El **release** del ZIP de plugins se lanza desde GitHub: *Actions → Release monorepo JARs → Run workflow* (elige un tag nuevo cada vez).

---

## Licencia

Proyecto bajo **GPLv3**. Ver [`LICENSE`](LICENSE).
