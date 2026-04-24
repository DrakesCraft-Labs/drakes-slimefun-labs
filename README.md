# Drakes Slimefun Labs

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.1](https://img.shields.io/badge/Paper-1.21.1-3b82f6?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![CI Gates](https://img.shields.io/badge/CI-Gates%201--5%20Green-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions)
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

> Corte generado automaticamente a partir de `ci-gate-*.yml`, reactor `pom.xml`, `settings.gradle.kts` y evidencia de compilacion local documentada en el script.

| Estado | Cantidad | Significado |
|---:|---:|---|
| **Listo (CI)** | **12** | Aparece en un workflow `ci-gate-*` y compila alli. |
| **Listo (local)** | **5** | `mvn compile -am` verde en revision auditada; **pendiente** promover a gate CI. |
| **En curso** | **66** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **3** | Fallo reproducible de compilacion en el reactor Gradle. |
| **Total modulos** | **86** | Maven + Gradle en reactor; ver conteo exacto en esta fila. |

### Barra de proporcion (CI vs resto)

```text
Listo CI:     12/86  (14.0%)
Listo local:  5/86
En curso:     66/86
Bloqueado:    3/86
```

---

## Metodologia (criterios)

1. **Listo (CI)**: modulo listado explicitamente en `ci-gate-1-foundation.yml`, `ci-gate-2-stable.yml`, `ci-gate-3-community.yml`, `ci-gate-4-complex.yml` o paso Gradle de `ci-gate-5-gradle.yml`.
2. **Listo (local)**: compilacion Maven exitosa en cadena `-am` en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build Gradle del monorepo documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`.

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |
|---|---|:---:|---|---|---|
| Cultivation_Updated | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/Cultivation_Updated` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo a un gate CI y smoke en servidor. |
| InfinityLib | libreria | Listo (CI) | Gate 1 | `sources/batch-2-expansion/InfinityLib` | Stack base Paper 1.21.1 + Java 21. |
| LiteXpansion | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/LiteXpansion` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo a un gate CI y smoke en servidor. |
| Networks_Better_Compatibility | addon / expansion | En curso | Reactor Maven | `sources/batch-2-expansion/Networks_Better_Compatibility` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SMG | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/SMG` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo a un gate CI y smoke en servidor. |
| SefiLib | libreria | Listo (CI) | Gate 1 | `sources/batch-2-expansion/SefiLib` | Stack base Paper 1.21.1 + Java 21. |
| SlimeTinker | addon / expansion | En curso | Reactor Maven | `sources/batch-2-expansion/SlimeTinker` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Supreme | addon / expansion | Listo (CI) | Gate 4 | `sources/batch-2-expansion/Supreme` | Supreme en lote complejo; ademas compila en cadena Maven local. |
| TranscEndence | addon / expansion | Listo (local) | Maven compile 2026-04 | `sources/batch-2-expansion/TranscEndence` | Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo a un gate CI y smoke en servidor. |
| AdvancedTech | addon | En curso | Reactor Maven | `sources/community-addons/AdvancedTech` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| AlchimiaVitae | addon | En curso | Reactor Maven | `sources/community-addons/AlchimiaVitae` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Better-Nuclear-Generator | addon | En curso | Reactor Maven | `sources/community-addons/Better-Nuclear-Generator` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| CompressionCraft | addon | En curso | Reactor Maven | `sources/community-addons/CompressionCraft` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| CrystamaeHistoria | addon | En curso | Reactor Maven | `sources/community-addons/CrystamaeHistoria` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| DankTech2 | addon | Listo (CI) | Gate 3 | `sources/community-addons/DankTech2` | Repos + comunidad (subconjunto). |
| DyeBench | addon | Listo (CI) | Gate 2 | `sources/community-addons/DyeBench` | Addons estables del lote rapido. |
| EMCTech | addon | En curso | Reactor Maven | `sources/community-addons/EMCTech` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Element-Manipulation | addon | En curso | Reactor Maven | `sources/community-addons/Element-Manipulation` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| ExtraTools | addon | En curso | Reactor Maven | `sources/community-addons/ExtraTools` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| FN-FAL-s-Amplifications | addon | En curso | Reactor Maven | `sources/community-addons/FN-FAL-s-Amplifications` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| FlowerPower | addon | En curso | Reactor Maven | `sources/community-addons/FlowerPower` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| FoxyMachines | addon | En curso | Reactor Maven | `sources/community-addons/FoxyMachines` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Gastronomicon | addon | En curso | Reactor Maven | `sources/community-addons/Gastronomicon` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| GeneticChickengineering-Reborn | addon | En curso | Reactor Maven | `sources/community-addons/GeneticChickengineering-Reborn` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Geyser-Slimefun-Heads | addon | En curso | Reactor Maven | `sources/community-addons/Geyser-Slimefun-Heads` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| HeadLimiter | addon | En curso | Reactor Maven | `sources/community-addons/HeadLimiter` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Liquid | addon | En curso | Reactor Maven | `sources/community-addons/Liquid` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Magic-8-Ball | addon | En curso | Reactor Maven | `sources/community-addons/Magic-8-Ball` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| MapJammers | addon | En curso | Reactor Maven | `sources/community-addons/MapJammers` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| MiniBlocks | addon | Listo (CI) | Gate 2 | `sources/community-addons/MiniBlocks` | Addons estables del lote rapido. |
| MissileWarfare | addon | En curso | Reactor Maven | `sources/community-addons/MissileWarfare` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| MoreResearches | addon | En curso | Reactor Maven | `sources/community-addons/MoreResearches` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Netheopoiesis | addon | En curso | Reactor Maven | `sources/community-addons/Netheopoiesis` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| PotionExpansion | addon | En curso | Reactor Maven | `sources/community-addons/PotionExpansion` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Quaptics | addon | Listo (CI) | Gate 2 | `sources/community-addons/Quaptics` | Addons estables del lote rapido. |
| RelicsOfCthonia | addon | En curso | Reactor Maven | `sources/community-addons/RelicsOfCthonia` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| RykenSlimeCustomizer-EN | addon | En curso | Reactor Maven | `sources/community-addons/RykenSlimeCustomizer-EN` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SaneCrafting | addon | En curso | Reactor Maven | `sources/community-addons/SaneCrafting` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SfBetterChests | addon | En curso | Reactor Maven | `sources/community-addons/SfBetterChests` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SfChunkInfo | addon | En curso | Reactor Maven | `sources/community-addons/SfChunkInfo` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Simple-Storage | addon | En curso | Reactor Maven | `sources/community-addons/Simple-Storage` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimeCustomizer | addon | En curso | Reactor Maven | `sources/community-addons/SlimeCustomizer` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimeFrame | addon | En curso | Reactor Maven | `sources/community-addons/SlimeFrame` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimeHUD | addon | En curso | Reactor Maven | `sources/community-addons/SlimeHUD` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimefunAdvancements | addon | En curso | Reactor Maven | `sources/community-addons/SlimefunAdvancements` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SmallSpace | addon | En curso | Reactor Maven | `sources/community-addons/SmallSpace` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SpiritsUnchained | addon | En curso | Reactor Maven | `sources/community-addons/SpiritsUnchained` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| UltimateGenerators2 | addon | En curso | Reactor Maven | `sources/community-addons/UltimateGenerators2` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| VillagerTrade | addon | En curso | Reactor Maven | `sources/community-addons/VillagerTrade` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| VillagerUtil | addon | En curso | Reactor Maven | `sources/community-addons/VillagerUtil` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Wildernether | addon | En curso | Reactor Maven | `sources/community-addons/Wildernether` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| WorldEditSlimefun | addon | En curso | Reactor Maven | `sources/community-addons/WorldEditSlimefun` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| dough-core | libreria | Listo (CI) | Gate 1 | `sources/dough-core` | Stack base Paper 1.21.1 + Java 21. |
| commons-lang-drake-patched | interno | Listo (CI) | Gate 1 | `sources/internal-metadata/patches/commons-lang-drake-patched` | Stack base Paper 1.21.1 + Java 21. |
| ColoredEnderChests | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ColoredEnderChests` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| DyedBackpacks | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/DyedBackpacks` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| DynaTech | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/DynaTech` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| EcoPower | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/EcoPower` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| ElectricSpawners | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ElectricSpawners` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| ExoticGarden | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ExoticGarden` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| ExtraGear | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ExtraGear` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| ExtraHeads | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/ExtraHeads` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| ExtraUtils | addon (port) | Listo (CI) | Gate 3 | `sources/repos-to-port/ExtraUtils` | Repos + comunidad (subconjunto). |
| FluffyMachines | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/FluffyMachines` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| GlobalWarming | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/GlobalWarming` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| HardcoreSlimefun | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/HardcoreSlimefun` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| HotbarPets | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/HotbarPets` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| InfinityExpansion | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/InfinityExpansion` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| MobCapturer | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/MobCapturer` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| PrivateStorage | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/PrivateStorage` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SFCalc | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SFCalc` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SFMobDrops | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SFMobDrops` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SimpleUtils | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SimpleUtils` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimeChem | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimeChem` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimefunOreChunks | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimefunOreChunks` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimyRepair | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimyRepair` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SlimyTreeTaps | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SlimyTreeTaps` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SoulJars | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SoulJars` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| SoundMuffler | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/SoundMuffler` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| luckyblocks-sf | addon (port) | En curso | Reactor Maven | `sources/repos-to-port/luckyblocks-sf` | En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI. |
| Slimefun4-src | core | Listo (CI) | Gate 1 | `sources/slimefun-core/Slimefun4-src` | Stack base Paper 1.21.1 + Java 21. |
| Galactifun | addon (Gradle) | Listo (CI) | Gate 5 Gradle | `sources/batch-2-expansion/Galactifun` | Construido en CI; dependencias base Maven pre-instaladas en el workflow. |
| Bump | addon (Gradle) | Listo (local) | Gradle compile 2026-04 | `sources/community-addons/Bump` | Port Bump: `JavaPlugin` + SlimefunAddon Drake, maquinas sin Guizhan `MenuBlock`/BusyBiscuit, APIs 1.21 (`Enchantment`, `ItemFlag`). `compileJava` OK; falta gate CI y smoke. |
| CustomItemGenerators | addon (Gradle) | Bloqueado (build) | Gradle monorepo | `sources/community-addons/CustomItemGenerators` | Kotlin: clase base `AbstractAddon` vs `JavaPlugin` (`dataFolder`, `server`, etc.); dependencia `sf4k` y firma `SlimefunAddon`. |
| FastMachines | addon (Gradle) | Bloqueado (build) | Gradle monorepo | `sources/community-addons/FastMachines` | Kotlin: extensiones y tipos esperan `SlimefunAddon`/`SlimefunItemStack` del fork; ajustar imports y bridge de addon. |
| SlimefunTranslation | addon (Gradle) | Bloqueado (build) | Gradle monorepo | `sources/community-addons/SlimefunTranslation` | Java: decenas de errores de API (SlimefunAddon, permisos, traducciones); migracion profunda contra Slimefun4 Drake. |

---

## Arbol resumido del monorepo

```text
drakes-slimefun-labs/
├─ .github/workflows/     # ci-gate-1 .. ci-gate-5
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
