# Drakes Slimefun Labs

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.1](https://img.shields.io/badge/Paper-1.21.1-3b82f6?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![CI Monorepo](https://img.shields.io/badge/CI-Monorepo%201.21-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions/workflows/ci-monorepo-121.yml)
[![Monorepo](https://img.shields.io/badge/Monorepo-Slimefun%20Ecosystem-7c3aed?style=for-the-badge)](#inventario-completo-de-modulos-y-plugins)
[![GPLv3](https://img.shields.io/badge/License-GPLv3-ef4444?style=for-the-badge)](LICENSE)

Laboratorio de integracion, porteo y estabilizacion para el ecosistema **Slimefun 4** sobre baseline unificado **Paper 1.21.1 + Java 21**. Este repositorio agrupa el core Drake, librerias compartidas y decenas de addons en un reactor **Maven + Gradle** coherente.

---

## Enlaces rapidos

| Recurso | URL |
|---|---|
| **GitHub Project (estado org)** | [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) |
| **Actions (CI)** | [Workflow runs](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions) |
| **Issues** | [Issues](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/issues) |
| **Matriz detallada (generada)** | [`docs/es/PLUGIN_MATRIX.md`](docs/es/PLUGIN_MATRIX.md) |
| **Como sincronizar el tablero** | [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md) |
| **Docs ES (indice)** | [`docs/es/home.md`](docs/es/home.md) |

> La matriz y la tabla de este README se generan con `python scripts/generate_plugin_matrix.py` para evitar desalineacion manual.

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

> Corte generado automaticamente a partir de `ci-monorepo-121.yml`, reactor `pom.xml`, `settings.gradle.kts` y evidencia de compilacion local documentada en el script.

| Estado | Cantidad | Significado |
|---:|---:|---|
| **Listo (CI)** | **13** | Aparece en `ci-monorepo-121.yml` (job Maven o `gradle_green`) y compila alli. |
| **Listo (local)** | **73** | `mvn compile -fae` o `gradlew <proyecto>:compileJava` verde en revision auditada; **pendiente** promover a un job de `ci-monorepo-121.yml`. |
| **En curso** | **0** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **0** | Fallo reproducible de compilacion en el reactor local. |
| **Total modulos** | **86** | Maven + Gradle en reactor; ver conteo exacto en esta fila. |

### Barra de proporcion (CI vs resto)

```text
Listo CI:     13/86  (15.1%)
Listo local:  73/86
En curso:     0/86
Bloqueado:    0/86
```

---

## Metodologia (criterios)

1. **Listo (CI)**: modulo cubierto por un job de [`ci-monorepo-121.yml`](.github/workflows/ci-monorepo-121.yml) (`foundation`, `maven_*`, `gradle_green`).
2. **Listo (local)**: compilacion Maven/Gradle exitosa en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build local documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`, bridges locales de compatibilidad para addons Gradle (`MenuBlock`, `TickingMenuBlock`, `DrakeItemBuilderCompat`) y bridges BusyBiscuit en Slimefun core (`io.github.thebusybiscuit.slimefun4.*`).

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |
|---|---|:---:|---|---|---|
| Cultivation_Updated | addon / expansion | Listo (local) | Maven reactor compile 2026-04-24 | `sources/batch-2-expansion/Cultivation_Updated` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| InfinityLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/InfinityLib` | Stack base Paper 1.21.1 + Java 21. |
| LiteXpansion | addon / expansion | Listo (local) | Maven reactor compile 2026-04-24 | `sources/batch-2-expansion/LiteXpansion` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Networks_Better_Compatibility | addon / expansion | Listo (local) | Maven reactor compile 2026-04-24 | `sources/batch-2-expansion/Networks_Better_Compatibility` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SMG | addon / expansion | Listo (local) | Maven reactor compile 2026-04-24 | `sources/batch-2-expansion/SMG` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SefiLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/SefiLib` | Stack base Paper 1.21.1 + Java 21. |
| SlimeTinker | addon / expansion | Listo (local) | Maven reactor compile 2026-04-24 | `sources/batch-2-expansion/SlimeTinker` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Supreme | addon / expansion | Listo (CI) | CI Monorepo · maven_complex | `sources/batch-2-expansion/Supreme` | Supreme en lote complejo; ademas compila en cadena Maven local. |
| TranscEndence | addon / expansion | Listo (local) | Maven reactor compile 2026-04-24 | `sources/batch-2-expansion/TranscEndence` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| AdvancedTech | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/AdvancedTech` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| AlchimiaVitae | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/AlchimiaVitae` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Better-Nuclear-Generator | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Better-Nuclear-Generator` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| CompressionCraft | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/CompressionCraft` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| CrystamaeHistoria | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/CrystamaeHistoria` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| DankTech2 | addon | Listo (CI) | CI Monorepo · maven_community | `sources/community-addons/DankTech2` | Repos + comunidad (subconjunto). |
| DyeBench | addon | Listo (CI) | CI Monorepo · maven_stable | `sources/community-addons/DyeBench` | Addons estables del lote rapido. |
| EMCTech | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/EMCTech` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Element-Manipulation | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Element-Manipulation` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraTools | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/ExtraTools` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| FN-FAL-s-Amplifications | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/FN-FAL-s-Amplifications` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| FlowerPower | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/FlowerPower` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| FoxyMachines | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/FoxyMachines` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Gastronomicon | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Gastronomicon` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| GeneticChickengineering-Reborn | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/GeneticChickengineering-Reborn` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Geyser-Slimefun-Heads | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Geyser-Slimefun-Heads` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| HeadLimiter | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/HeadLimiter` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Liquid | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Liquid` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Magic-8-Ball | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Magic-8-Ball` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| MapJammers | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/MapJammers` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| MiniBlocks | addon | Listo (CI) | CI Monorepo · maven_stable | `sources/community-addons/MiniBlocks` | Addons estables del lote rapido. |
| MissileWarfare | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/MissileWarfare` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| MoreResearches | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/MoreResearches` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Netheopoiesis | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Netheopoiesis` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| PotionExpansion | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/PotionExpansion` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Quaptics | addon | Listo (CI) | CI Monorepo · maven_stable | `sources/community-addons/Quaptics` | Addons estables del lote rapido. |
| RelicsOfCthonia | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/RelicsOfCthonia` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| RykenSlimeCustomizer-EN | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/RykenSlimeCustomizer-EN` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SaneCrafting | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SaneCrafting` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SfBetterChests | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SfBetterChests` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SfChunkInfo | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SfChunkInfo` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Simple-Storage | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Simple-Storage` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeCustomizer | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SlimeCustomizer` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeFrame | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SlimeFrame` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeHUD | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SlimeHUD` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimefunAdvancements | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SlimefunAdvancements` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SmallSpace | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SmallSpace` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SpiritsUnchained | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/SpiritsUnchained` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| UltimateGenerators2 | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/UltimateGenerators2` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| VillagerTrade | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/VillagerTrade` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| VillagerUtil | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/VillagerUtil` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Wildernether | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/Wildernether` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| WorldEditSlimefun | addon | Listo (local) | Maven reactor compile 2026-04-24 | `sources/community-addons/WorldEditSlimefun` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| dough-core | libreria | Listo (CI) | CI Monorepo · foundation | `sources/dough-core` | Stack base Paper 1.21.1 + Java 21. |
| commons-lang-drake-patched | interno | Listo (CI) | CI Monorepo · foundation | `sources/internal-metadata/patches/commons-lang-drake-patched` | Stack base Paper 1.21.1 + Java 21. |
| ColoredEnderChests | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/ColoredEnderChests` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| DyedBackpacks | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/DyedBackpacks` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| DynaTech | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/DynaTech` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| EcoPower | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/EcoPower` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| ElectricSpawners | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/ElectricSpawners` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| ExoticGarden | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/ExoticGarden` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraGear | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/ExtraGear` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraHeads | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/ExtraHeads` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraUtils | addon (port) | Listo (CI) | CI Monorepo · maven_community | `sources/repos-to-port/ExtraUtils` | Repos + comunidad (subconjunto). |
| FluffyMachines | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/FluffyMachines` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| GlobalWarming | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/GlobalWarming` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| HardcoreSlimefun | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/HardcoreSlimefun` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| HotbarPets | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/HotbarPets` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| InfinityExpansion | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/InfinityExpansion` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| MobCapturer | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/MobCapturer` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| PrivateStorage | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/PrivateStorage` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SFCalc | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SFCalc` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SFMobDrops | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SFMobDrops` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SimpleUtils | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SimpleUtils` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeChem | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SlimeChem` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimefunOreChunks | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SlimefunOreChunks` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimyRepair | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SlimyRepair` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimyTreeTaps | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SlimyTreeTaps` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SoulJars | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SoulJars` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| SoundMuffler | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/SoundMuffler` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| luckyblocks-sf | addon (port) | Listo (local) | Maven reactor compile 2026-04-24 | `sources/repos-to-port/luckyblocks-sf` | `mvn -B -DskipTests compile -fae` verde en reactor completo; falta promover a CI y smoke en servidor si el addon tiene mecanicas sensibles. |
| Slimefun4-src | core | Listo (CI) | CI Monorepo · foundation | `sources/slimefun-core/Slimefun4-src` | Stack base Paper 1.21.1 + Java 21. |
| Galactifun | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/batch-2-expansion/Galactifun` | Compila en job `gradle_green` (`compileJava`); Maven base instalado antes en el mismo workflow. |
| Bump | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/Bump` | Compila en job `gradle_green` (`compileJava`); Maven base instalado antes en el mismo workflow. |
| CustomItemGenerators | addon (Gradle) | Listo (local) | Gradle compileJava 2026-04-24 | `sources/community-addons/CustomItemGenerators` | Gradle `compileJava` verde. Port principal: `JavaPlugin` directo, adapter `SlimefunAddon`, sin `sf4k`; proteccion usa paquete Drake sombreado. Falta promover a CI si debe quedar como gate permanente. |
| FastMachines | addon (Gradle) | Listo (local) | Gradle compileJava 2026-04-24 | `sources/community-addons/FastMachines` | Gradle `compileJava` verde. Incluye bridges locales `MenuBlock`/`TickingMenuBlock` y DSL `DrakeItemBuilderCompat`; requiere artefactos Maven base e `InfinityExpansion-drake` en `mavenLocal`. Falta promover a CI si debe quedar como gate permanente. |
| SlimefunTranslation | addon (Gradle) | Listo (local) | Gradle compileJava 2026-04-24 | `sources/community-addons/SlimefunTranslation` | Gradle `compileJava` verde. API Paper 1.21 ajustada (`EntityType.ITEM`) y SlimefunTranslation compilable contra Slimefun Drake. Falta promover a CI si debe quedar como gate permanente. |

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

# Build base Maven (ejemplo)
mvn -B clean install -DskipTests -pl sources/dough-core,sources/slimefun-core/Slimefun4-src -am

# Verificacion local del corte completo
mvn -B -DskipTests compile -fae
./gradlew :sources:batch-2-expansion:Galactifun:compileJava :sources:community-addons:Bump:compileJava :sources:community-addons:CustomItemGenerators:compileJava :sources:community-addons:FastMachines:compileJava :sources:community-addons:SlimefunTranslation:compileJava --no-daemon

# Dependencia local necesaria para FastMachines cuando Gradle resuelve InfinityExpansion-drake
mvn -B -DskipTests install -pl sources/repos-to-port/InfinityExpansion -am
```

---

## Licencia

Proyecto bajo **GPLv3**. Ver [`LICENSE`](LICENSE).
