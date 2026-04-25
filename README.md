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
| **Listo (local)** | **4** | `mvn compile -am` verde en revision auditada; **pendiente** promover a un job de `ci-monorepo-121.yml`. |
| **En curso** | **66** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **3** | Fallo reproducible de compilacion en el reactor Gradle. |
| **Total modulos** | **86** | Maven + Gradle en reactor; ver conteo exacto en esta fila. |

### Barra de proporcion (CI vs resto)

```text
Listo CI:     13/86  (15.1%)
Listo local:  4/86
En curso:     66/86
Bloqueado:    3/86
```

---

## Metodologia (criterios)

1. **Listo (CI)**: modulo cubierto por un job de [`ci-monorepo-121.yml`](.github/workflows/ci-monorepo-121.yml) (`foundation`, `maven_*`, `gradle_green`).
2. **Listo (local)**: compilacion Maven exitosa en cadena `-am` en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build Gradle del monorepo documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`.

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |
|---|---|:---:|---|---|---|
| Cultivation_Updated | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/Cultivation_Updated` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo al workflow CI (`ci-monorepo-121.yml`) y smoke en servidor. |
| InfinityLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/InfinityLib` | Stack base Paper 1.21.1 + Java 21. |
| LiteXpansion | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/LiteXpansion` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo al workflow CI (`ci-monorepo-121.yml`) y smoke en servidor. |
| Networks_Better_Compatibility | addon / expansion | En curso | Reactor Maven | `sources/batch-2-expansion/Networks_Better_Compatibility` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SMG | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/SMG` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo al workflow CI (`ci-monorepo-121.yml`) y smoke en servidor. |
| SefiLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/SefiLib` | Stack base Paper 1.21.1 + Java 21. |
| SlimeTinker | addon / expansion | En curso | Reactor Maven | `sources/batch-2-expansion/SlimeTinker` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Supreme | addon / expansion | Listo (CI) | CI Monorepo · maven_complex | `sources/batch-2-expansion/Supreme` | Supreme en lote complejo; ademas compila en cadena Maven local. |
| TranscEndence | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/TranscEndence` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo al workflow CI (`ci-monorepo-121.yml`) y smoke en servidor. |
| AdvancedTech | addon | En curso | Reactor Maven | `sources/community-addons/AdvancedTech` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| AlchimiaVitae | addon | En curso | Reactor Maven | `sources/community-addons/AlchimiaVitae` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Better-Nuclear-Generator | addon | En curso | Reactor Maven | `sources/community-addons/Better-Nuclear-Generator` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| CompressionCraft | addon | En curso | Reactor Maven | `sources/community-addons/CompressionCraft` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| CrystamaeHistoria | addon | En curso | Reactor Maven | `sources/community-addons/CrystamaeHistoria` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| DankTech2 | addon | Listo (CI) | CI Monorepo · maven_community | `sources/community-addons/DankTech2` | Repos + comunidad (subconjunto). |
| DyeBench | addon | Listo (CI) | CI Monorepo · maven_stable | `sources/community-addons/DyeBench` | Addons estables del lote rapido. |
| EMCTech | addon | En curso | Reactor Maven | `sources/community-addons/EMCTech` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Element-Manipulation | addon | En curso | Reactor Maven | `sources/community-addons/Element-Manipulation` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| ExtraTools | addon | En curso | Reactor Maven | `sources/community-addons/ExtraTools` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| FN-FAL-s-Amplifications | addon | En curso | Reactor Maven | `sources/community-addons/FN-FAL-s-Amplifications` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| FlowerPower | addon | En curso | Reactor Maven | `sources/community-addons/FlowerPower` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| FoxyMachines | addon | En curso | Reactor Maven | `sources/community-addons/FoxyMachines` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Gastronomicon | addon | En curso | Reactor Maven | `sources/community-addons/Gastronomicon` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| GeneticChickengineering-Reborn | addon | En curso | Reactor Maven | `sources/community-addons/GeneticChickengineering-Reborn` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Geyser-Slimefun-Heads | addon | En curso | Reactor Maven | `sources/community-addons/Geyser-Slimefun-Heads` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| HeadLimiter | addon | En curso | Reactor Maven | `sources/community-addons/HeadLimiter` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Liquid | addon | En curso | Reactor Maven | `sources/community-addons/Liquid` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Magic-8-Ball | addon | En curso | Reactor Maven | `sources/community-addons/Magic-8-Ball` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| MapJammers | addon | En curso | Reactor Maven | `sources/community-addons/MapJammers` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| MiniBlocks | addon | Listo (CI) | CI Monorepo · maven_stable | `sources/community-addons/MiniBlocks` | Addons estables del lote rapido. |
| MissileWarfare | addon | En curso | Reactor Maven | `sources/community-addons/MissileWarfare` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| MoreResearches | addon | En curso | Reactor Maven | `sources/community-addons/MoreResearches` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Netheopoiesis | addon | En curso | Reactor Maven | `sources/community-addons/Netheopoiesis` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| PotionExpansion | addon | En curso | Reactor Maven | `sources/community-addons/PotionExpansion` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Quaptics | addon | Listo (CI) | CI Monorepo · maven_stable | `sources/community-addons/Quaptics` | Addons estables del lote rapido. |
| RelicsOfCthonia | addon | En curso | Reactor Maven | `sources/community-addons/RelicsOfCthonia` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| RykenSlimeCustomizer-EN | addon | En curso | Reactor Maven | `sources/community-addons/RykenSlimeCustomizer-EN` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SaneCrafting | addon | En curso | Reactor Maven | `sources/community-addons/SaneCrafting` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SfBetterChests | addon | En curso | Reactor Maven | `sources/community-addons/SfBetterChests` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SfChunkInfo | addon | En curso | Reactor Maven | `sources/community-addons/SfChunkInfo` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Simple-Storage | addon | En curso | Reactor Maven | `sources/community-addons/Simple-Storage` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimeCustomizer | addon | En curso | Reactor Maven | `sources/community-addons/SlimeCustomizer` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimeFrame | addon | En curso | Reactor Maven | `sources/community-addons/SlimeFrame` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimeHUD | addon | En curso | Reactor Maven | `sources/community-addons/SlimeHUD` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimefunAdvancements | addon | En curso | Reactor Maven | `sources/community-addons/SlimefunAdvancements` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SmallSpace | addon | En curso | Reactor Maven | `sources/community-addons/SmallSpace` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SpiritsUnchained | addon | En curso | Reactor Maven | `sources/community-addons/SpiritsUnchained` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| UltimateGenerators2 | addon | En curso | Reactor Maven | `sources/community-addons/UltimateGenerators2` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| VillagerTrade | addon | En curso | Reactor Maven | `sources/community-addons/VillagerTrade` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| VillagerUtil | addon | En curso | Reactor Maven | `sources/community-addons/VillagerUtil` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Wildernether | addon | En curso | Reactor Maven | `sources/community-addons/Wildernether` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| WorldEditSlimefun | addon | En curso | Reactor Maven | `sources/community-addons/WorldEditSlimefun` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| dough-core | libreria | Listo (CI) | CI Monorepo · foundation | `sources/dough-core` | Stack base Paper 1.21.1 + Java 21. |
| commons-lang-drake-patched | interno | Listo (CI) | CI Monorepo · foundation | `sources/internal-metadata/patches/commons-lang-drake-patched` | Stack base Paper 1.21.1 + Java 21. |
| ColoredEnderChests | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ColoredEnderChests` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| DyedBackpacks | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/DyedBackpacks` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| DynaTech | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/DynaTech` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| EcoPower | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/EcoPower` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| ElectricSpawners | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ElectricSpawners` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| ExoticGarden | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ExoticGarden` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| ExtraGear | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ExtraGear` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| ExtraHeads | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ExtraHeads` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| ExtraUtils | addon (port) | Listo (CI) | CI Monorepo · maven_community | `sources/repos-to-port/ExtraUtils` | Repos + comunidad (subconjunto). |
| FluffyMachines | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/FluffyMachines` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| GlobalWarming | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/GlobalWarming` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| HardcoreSlimefun | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/HardcoreSlimefun` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| HotbarPets | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/HotbarPets` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| InfinityExpansion | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/InfinityExpansion` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| MobCapturer | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/MobCapturer` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| PrivateStorage | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/PrivateStorage` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SFCalc | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SFCalc` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SFMobDrops | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SFMobDrops` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SimpleUtils | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SimpleUtils` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimeChem | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimeChem` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimefunOreChunks | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimefunOreChunks` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimyRepair | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimyRepair` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SlimyTreeTaps | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimyTreeTaps` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SoulJars | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SoulJars` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| SoundMuffler | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SoundMuffler` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| luckyblocks-sf | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/luckyblocks-sf` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en job CI del monorepo. |
| Slimefun4-src | core | Listo (CI) | CI Monorepo · foundation | `sources/slimefun-core/Slimefun4-src` | Stack base Paper 1.21.1 + Java 21. |
| Galactifun | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/batch-2-expansion/Galactifun` | Compila en job `gradle_green` (`compileJava`); Maven base instalado antes en el mismo workflow. |
| Bump | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/Bump` | Compila en job `gradle_green` (`compileJava`); Maven base instalado antes en el mismo workflow. |
| CustomItemGenerators | addon (Gradle) | Bloqueado (build) | Gradle monorepo | `sources/community-addons/CustomItemGenerators` | Kotlin: clase base `AbstractAddon` vs `JavaPlugin` (`dataFolder`, `server`, etc.); dependencia `sf4k` y firma `SlimefunAddon`. |
| FastMachines | addon (Gradle) | Bloqueado (build) | Gradle monorepo | `sources/community-addons/FastMachines` | Kotlin: extensiones y tipos esperan `SlimefunAddon`/`SlimefunItemStack` del fork; ajustar imports y bridge de addon. |
| SlimefunTranslation | addon (Gradle) | Bloqueado (build) | Gradle monorepo | `sources/community-addons/SlimefunTranslation` | Java: decenas de errores de API (SlimefunAddon, permisos, traducciones); migracion profunda contra Slimefun4 Drake. |

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
```

---

## Licencia

Proyecto bajo **GPLv3**. Ver [`LICENSE`](LICENSE).
