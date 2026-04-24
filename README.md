# Drakes Slimefun Labs

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.1](https://img.shields.io/badge/Paper-1.21.1-3b82f6?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![CI Gates](https://img.shields.io/badge/CI-Gates%201--5%20Green-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions)
[![Monorepo](https://img.shields.io/badge/Monorepo-Slimefun%20Ecosystem-7c3aed?style=for-the-badge)](#estado-global-de-plugins)
[![GPLv3](https://img.shields.io/badge/License-GPLv3-ef4444?style=for-the-badge)](LICENSE)

Laboratorio de integracion, porteo y estabilizacion para addons de Slimefun sobre baseline unificado: `Paper 1.21.1 + Java 21`.

---

## Tabla de contenidos

- [Estado global de plugins](#estado-global-de-plugins)
- [Metodologia de clasificacion](#metodologia-de-clasificacion)
- [Inventario completo de plugins y addons](#inventario-completo-de-plugins-y-addons)
- [Arbol resumido del monorepo](#arbol-resumido-del-monorepo)
- [Comandos de validacion rapida](#comandos-de-validacion-rapida)

---

## Estado global de plugins

> Corte auditado: **2026-04-24**  
> Universo total: **86 modulos** (union de `pom.xml`, `settings.gradle.kts` y modulos con `plugin.yml` en `sources`).

| Estado | Conteo | Criterio ejecutivo |
|---|---:|---|
| ✅ Listo | **12** | Modulo incluido en gates CI (`ci-gate-*.yml`) y referencia de estado en verde. |
| 🟡 En progreso | **74** | Sin evidencia reciente por modulo en gates CI (clasificacion conservadora). |
| ❌ Bloqueado | **0** | No se detectaron modulos declarados sin directorio o bloqueo duro auditable por modulo. |

### Bloque visual de estado

```text
███████████████████████████████████████████████████████████████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
✅ Listo      12/86  (14.0%)
🟡 En progreso 74/86  (86.0%)
❌ Bloqueado   0/86  (0.0%)
```

---

## Metodologia de clasificacion

### Fuentes objetivas usadas

1. `pom.xml` (reactor Maven principal).
2. `settings.gradle.kts` (reactor Gradle activo).
3. `sources/**/src/main/resources/plugin.yml` (deteccion de addons reales en codigo fuente).
4. `.github/workflows/ci-gate-*.yml` (evidencia de compilacion por gates).
5. `docs/es/pending-modules.md` y `docs/es/release-and-ci-strategy.md` (criterio documental de madurez).

### Regla auditable aplicada

- ✅ **Listo**: modulo explicitamente incluido en alguno de los gates CI del repo.
- 🟡 **En progreso**: modulo detectado en inventario total sin evidencia reciente de build por modulo en gates.
- ❌ **Bloqueado**: modulo declarado en reactores pero no resoluble estructuralmente (ejemplo: ruta inexistente) o bloqueo duro documentado por modulo.

> Criterio conservador: cuando no hay evidencia directa de compilacion reciente por modulo, se marca **En progreso**.

---

## Inventario completo de plugins y addons

Leyenda de tipo: `addon`, `core`, `libreria`, `interno`.

| Plugin / Addon | Tipo | Estado | Nota breve | Ruta | Fuente inventario |
|---|---|---|---|---|---|
| Cultivation_Updated | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/batch-2-expansion/Cultivation_Updated` | Maven + plugin.yml |
| Galactifun | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/batch-2-expansion/Galactifun` | Gradle |
| InfinityLib | libreria | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/batch-2-expansion/InfinityLib` | Maven |
| LiteXpansion | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/batch-2-expansion/LiteXpansion` | Maven + plugin.yml |
| Networks_Better_Compatibility | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/batch-2-expansion/Networks_Better_Compatibility` | Maven + plugin.yml |
| SMG | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/batch-2-expansion/SMG` | Maven + plugin.yml |
| SefiLib | libreria | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/batch-2-expansion/SefiLib` | Maven |
| SlimeTinker | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/batch-2-expansion/SlimeTinker` | Maven + plugin.yml |
| Supreme | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/batch-2-expansion/Supreme` | Maven + plugin.yml |
| TranscEndence | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/batch-2-expansion/TranscEndence` | Maven + plugin.yml |
| AdvancedTech | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/AdvancedTech` | Maven + plugin.yml |
| AlchimiaVitae | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/AlchimiaVitae` | Maven + plugin.yml |
| Better-Nuclear-Generator | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Better-Nuclear-Generator` | Maven + plugin.yml |
| Bump | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Bump` | Gradle |
| CompressionCraft | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/CompressionCraft` | Maven + plugin.yml |
| CrystamaeHistoria | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/CrystamaeHistoria` | Maven + plugin.yml |
| CustomItemGenerators | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/CustomItemGenerators` | Gradle |
| DankTech2 | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/community-addons/DankTech2` | Maven + plugin.yml |
| DyeBench | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/community-addons/DyeBench` | Maven + plugin.yml |
| EMCTech | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/EMCTech` | Maven + plugin.yml |
| Element-Manipulation | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Element-Manipulation` | Maven + plugin.yml |
| ExtraTools | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/ExtraTools` | Maven + plugin.yml |
| FN-FAL-s-Amplifications | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/FN-FAL-s-Amplifications` | Maven + plugin.yml |
| FastMachines | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/FastMachines` | Gradle |
| FlowerPower | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/FlowerPower` | Maven + plugin.yml |
| FoxyMachines | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/FoxyMachines` | Maven + plugin.yml |
| Gastronomicon | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Gastronomicon` | Maven + plugin.yml |
| GeneticChickengineering-Reborn | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/GeneticChickengineering-Reborn` | Maven + plugin.yml |
| Geyser-Slimefun-Heads | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Geyser-Slimefun-Heads` | Maven + plugin.yml |
| HeadLimiter | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/HeadLimiter` | Maven + plugin.yml |
| Liquid | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Liquid` | Maven + plugin.yml |
| Magic-8-Ball | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Magic-8-Ball` | Maven + plugin.yml |
| MapJammers | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/MapJammers` | Maven + plugin.yml |
| MiniBlocks | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/community-addons/MiniBlocks` | Maven + plugin.yml |
| MissileWarfare | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/MissileWarfare` | Maven + plugin.yml |
| MoreResearches | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/MoreResearches` | Maven + plugin.yml |
| Netheopoiesis | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Netheopoiesis` | Maven + plugin.yml |
| PotionExpansion | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/PotionExpansion` | Maven + plugin.yml |
| Quaptics | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/community-addons/Quaptics` | Maven + plugin.yml |
| RelicsOfCthonia | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/RelicsOfCthonia` | Maven + plugin.yml |
| RykenSlimeCustomizer-EN | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/RykenSlimeCustomizer-EN` | Maven + plugin.yml |
| SaneCrafting | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SaneCrafting` | Maven + plugin.yml |
| SfBetterChests | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SfBetterChests` | Maven + plugin.yml |
| SfChunkInfo | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SfChunkInfo` | Maven + plugin.yml |
| Simple-Storage | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Simple-Storage` | Maven + plugin.yml |
| SlimeCustomizer | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SlimeCustomizer` | Maven + plugin.yml |
| SlimeFrame | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SlimeFrame` | Maven + plugin.yml |
| SlimeHUD | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SlimeHUD` | Maven + plugin.yml |
| SlimefunAdvancements | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SlimefunAdvancements` | Maven + plugin.yml |
| SlimefunTranslation | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SlimefunTranslation` | Gradle |
| SmallSpace | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SmallSpace` | Maven + plugin.yml |
| SpiritsUnchained | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/SpiritsUnchained` | Maven + plugin.yml |
| UltimateGenerators2 | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/UltimateGenerators2` | Maven + plugin.yml |
| VillagerTrade | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/VillagerTrade` | Maven + plugin.yml |
| VillagerUtil | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/VillagerUtil` | Maven + plugin.yml |
| Wildernether | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/Wildernether` | Maven + plugin.yml |
| WorldEditSlimefun | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/community-addons/WorldEditSlimefun` | Maven + plugin.yml |
| dough-core | libreria | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/dough-core` | Maven |
| commons-lang-drake-patched | interno | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/internal-metadata/patches/commons-lang-drake-patched` | Maven |
| ColoredEnderChests | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/ColoredEnderChests` | Maven + plugin.yml |
| DyedBackpacks | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/DyedBackpacks` | Maven + plugin.yml |
| DynaTech | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/DynaTech` | Maven + plugin.yml |
| EcoPower | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/EcoPower` | Maven + plugin.yml |
| ElectricSpawners | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/ElectricSpawners` | Maven + plugin.yml |
| ExoticGarden | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/ExoticGarden` | Maven + plugin.yml |
| ExtraGear | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/ExtraGear` | Maven + plugin.yml |
| ExtraHeads | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/ExtraHeads` | Maven |
| ExtraUtils | addon | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/repos-to-port/ExtraUtils` | Maven |
| FluffyMachines | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/FluffyMachines` | Maven + plugin.yml |
| GlobalWarming | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/GlobalWarming` | Maven + plugin.yml |
| HardcoreSlimefun | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/HardcoreSlimefun` | Maven + plugin.yml |
| HotbarPets | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/HotbarPets` | Maven + plugin.yml |
| InfinityExpansion | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/InfinityExpansion` | Maven + plugin.yml |
| MobCapturer | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/MobCapturer` | Maven |
| PrivateStorage | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/PrivateStorage` | Maven + plugin.yml |
| SFCalc | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SFCalc` | Maven + plugin.yml |
| SFMobDrops | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SFMobDrops` | Maven + plugin.yml |
| SimpleUtils | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SimpleUtils` | Maven + plugin.yml |
| SlimeChem | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SlimeChem` | Maven + plugin.yml |
| SlimefunOreChunks | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SlimefunOreChunks` | Maven + plugin.yml |
| SlimyRepair | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SlimyRepair` | Maven + plugin.yml |
| SlimyTreeTaps | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SlimyTreeTaps` | Maven + plugin.yml |
| SoulJars | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SoulJars` | Maven + plugin.yml |
| SoundMuffler | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/SoundMuffler` | Maven + plugin.yml |
| luckyblocks-sf | addon | 🟡 En progreso | Sin evidencia de build reciente por modulo en CI gate. | `sources/repos-to-port/luckyblocks-sf` | Maven + plugin.yml |
| Slimefun4-src | core | ✅ Listo | Incluido en gates CI con estado verde 2026-04-24. | `sources/slimefun-core/Slimefun4-src` | Maven + plugin.yml |

---

## Arbol resumido del monorepo

```text
drakes-slimefun-labs/
├─ .github/
│  └─ workflows/
│     ├─ ci-gate-1-foundation.yml
│     ├─ ci-gate-2-stable.yml
│     ├─ ci-gate-3-community.yml
│     ├─ ci-gate-4-complex.yml
│     └─ ci-gate-5-gradle.yml
├─ sources/
│  ├─ slimefun-core/                 # core principal
│  ├─ dough-core/                    # libreria comun
│  ├─ batch-2-expansion/             # expansiones y libs activas
│  ├─ community-addons/              # addons comunitarios
│  ├─ repos-to-port/                 # addons en porteo
│  └─ internal-metadata/             # patches internos
├─ docs/
├─ scripts/
├─ pom.xml
└─ settings.gradle.kts
```

---

## Comandos de validacion rapida

```bash
# Audit de estado global
python scripts/manager.py audit

# Build base Maven (ejemplo)
mvn -B clean install -DskipTests -pl sources/dough-core,sources/slimefun-core/Slimefun4-src -am

# Build Gradle (ejemplo gate 5)
cd sources/batch-2-expansion/Galactifun
./gradlew build
```

---

## Licencia

Proyecto bajo `GPLv3`. Revisar `LICENSE`.
