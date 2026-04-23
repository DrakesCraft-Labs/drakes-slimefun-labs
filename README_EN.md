# Drakes Slimefun Labs

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper Version](https://img.shields.io/badge/Paper-1.21.11-blue?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![Slimefun Version](https://img.shields.io/badge/Slimefun-6.0--Drake-green?style=for-the-badge)](https://github.com/Slimefun/Slimefun4)
[![License](https://img.shields.io/badge/License-GPL%20v3-red?style=for-the-badge)](LICENSE)

Working repository for the migration of the Slimefun ecosystem to `Paper 1.21.11`, `Java 21`, `Slimefun 6`, and the `Drake Framework` stack.

This repository is not a single plugin. It is a consolidation, porting, validation, and documentation lab for a broad set of addons, base libraries, and compatibility variants. The goal is not only to "make it compile", but to leave behind a migration base the team can understand, continue, and validate at runtime without ambiguity.

## Language

- Spanish main README: [README.md](README.md)
- English companion README: [README_EN.md](README_EN.md)
- English docs entry page: [English Home](docs/en/home.md)

## Quick Access

| Resource | Local Mirror (`.md`) | GitHub Wiki | Purpose |
| :--- | :--- | :--- | :--- |
| Documentation home | [English Home](docs/en/home.md) | [English Home](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Home-EN) | General English entry point. |
| Operational checklist | [Migration Checklist](docs/en/migration-checklist.md) | [Migration Checklist](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Migration-Checklist) | Executive status and priorities. |
| Remaining modules | [Pending Modules](docs/en/pending-modules.md) | [Pending Modules](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Pending-Modules) | What is still outside the reactor or blocked. |
| Project backlog | Not applicable | [Slimefun 1.21.11 Migration Backlog](https://github.com/orgs/DrakesCraft-Labs/projects/1/views/1) | Operational board for integration, technical port work, and smoke-test tracking. |
| Roadmap | [Stabilization Roadmap](docs/en/stabilization-roadmap.md) | [Stabilization Roadmap](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Stabilization-Roadmap) | Suggested closure order. |
| Daily handoff | [Tomorrow Handoff](docs/en/tomorrow-handoff.md) | [Tomorrow Handoff](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Tomorrow-Handoff-EN) | Session continuity. |
| Architecture | [Ecosystem Architecture](docs/en/ecosystem-architecture.md) | [Ecosystem Architecture](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Ecosystem-Architecture) | Explains the Drake stack and workspace layout. |
| Technical reference | [Technical Reference (Paper 1.21.1)](docs/en/technical-reference-paper-1.21.1.md) | [Technical Reference (Paper 1.21.1)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Technical-Reference-(Paper-1.21.1)) | API findings, compatibility notes, and port criteria. |
| Smoke test | [Smoke Test Guide](docs/en/smoke-test-guide.md) | [Smoke Test Guide](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Smoke-Test-Guide) | Basic runtime validation. |
| Dev setup | [Development Setup](docs/en/development-setup.md) | [Development Setup](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Development-Setup) | Local environment preparation. |
| Development standards | [Development Standards](docs/en/development-standards.md) | [Development Standards](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Development-Standards) | Conventions and quality criteria. |
| AI guide | [AI Instructions](docs/en/ai-instructions.md) | [AI Instructions](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/AI-Instructions) | Context and rules for assisted sessions. |
| Addon template guide | [New Addon Template](docs/en/new-addon-template.md) | [New Addon Template](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/New-Addon-Template-EN) | English explanation for the base template. |
| Release/CI strategy | [Release and CI Strategy](docs/en/release-and-ci-strategy.md) | [Release and CI Strategy](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Release-and-CI-Strategy) | Publication and automation policy. |
| Actual local template | [Template README](templates/slimefun-addon/README.md) | Not applicable | Local baseline for new addons in this stack. |

## Executive Status (The Great Work)

> [!IMPORTANT]
> Main working branch: `1.21-latin` | Unified Identity: `com.github.drakescraft-labs`

The lab currently covers a universe of **89 addons** plus base modules. We have achieved a high-performance hybrid architecture.

| Metric | Value | Status |
| :--- | :--- | :--- |
| **SURGICAL** (Ready + Rebrand) | `60` | 💎 Full 1.21.1 Stability |
| **STABILIZED** (Maven OK) | `18` | ⚙️ Unified Maven Reactor |
| **GRADLE REBELS** | `9` | 🐘 Master Gradle Reactor |
| **Total Progress** | **67.4%** | **89 Tracked Addons** |
| **Unified Base** | `com.github.drakescraft-labs:dough-core` | `1.3.1-DRAKE-v11-SNAPSHOT` |
| **Slimefun Core** | `com.github.drakescraft-labs:slimefun-core` | `11.0-Drake-1.21.1-SNAPSHOT` |
| **Unified Engine** | `Actions: DrakesLab Unified Engine` | 🚀 Parallel Maven + Gradle |

### How to interpret this status

- **SURGICAL**: The module compiles, is integrated into the reactor, and uses the official `-drake` identity.
- **STABILIZED**: The module is in the Maven reactor, is stable, but does not yet have final rebranding.
- **GRADLE REBELS**: Addons using Gradle, now managed by a **Master Gradle Reactor** with Java 21.
- **DrakesLab Manager**: The ecosystem is managed via `scripts/manager.py` (Python 3.12).
| Real operational backlog | `29 addons` |
| Unified base | `dev.drake.dough:dough-core:1.3.1-DRAKE` |
| Core | `dev.drake:Slimefun:6.0-Drake-1.21.11` |

### How to read this status

- `Ready in reactor` means the module compiles today inside the unified build for `1.21.11`.
- `Active with confirmed failure` means the addon is already in the root `pom.xml`, but it cannot be considered ready yet.
- `Outside the reactor` means the addon exists in the repository but has not been integrated into the unified build.
- `Real operational backlog` is the sum of what is still broken inside the reactor plus what is still outside it.

## What This Repo Is and What It Is Not

This repository:
- centralizes versions, dependencies, and Maven modules
- contains the port of both the core and `dough-core`
- serves as a migration lab for addons from different origins
- documents the real state of the ecosystem in a single working branch

This repository does not:
- represent one standalone release-ready plugin
- guarantee that every "ready" addon has already been smoke-tested in live runtime
- imply that every addon present under `sources/*` is already part of the active reactor

## Workspace Layout

| Path | Purpose |
| :--- | :--- |
| `sources/dough-core` | Unified base library relocated to `dev.drake.dough`. |
| `sources/slimefun-core/Slimefun4-src` | Slimefun core adapted to the current stack. |
| `sources/repos-to-port` | Batch 1: historically prioritized addons to port. |
| `sources/batch-2-expansion` | Libraries, expansions, and compatibility variants. |
| `sources/community-addons` | Community archive integrated progressively into the reactor. |
| `templates/slimefun-addon` | Base template for new ecosystem addons. |
| `wiki_temp` | Editable local mirror of the public repository wiki. |

## Recommended Workflow

### Isolated build

Do not compile the entire reactor unless it is strictly necessary. The recommended pattern is:

```powershell
mvn -pl path/to/module -am -DskipTests package
```

Examples:

```powershell
mvn -pl sources/repos-to-port/SimpleUtils -am -DskipTests package
mvn -pl sources/repos-to-port/DynaTech -am -DskipTests package
mvn -pl sources/community-addons/MapJammers -am -DskipTests compile
```

### Flag meaning

- `-pl`: selects the target module
- `-am`: also builds its dependencies inside the reactor
- `-DskipTests`: skips tests to speed up validation cycles

### Server smoke test

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\slimefun\smoke-test.ps1
```

### Curated CI

The repository includes a conservative GitHub Actions workflow that validates curated groups of stable modules and uploads downloadable artifacts without building the entire reactor:

- workflow: `.github/workflows/ci-curated-modules.yml`
- strategy: [Release and CI Strategy](docs/en/release-and-ci-strategy.md)

### Operational rule before touching code

1. determine whether the issue is `pom.xml`/dependency-related or API/code-related
2. if the addon already uses `dev.drake.dough.*` but still fails on imports, inspect the `pom.xml` first
3. prefer small changes with real validation
4. if the board changes, sync both `README` and wiki

## Explicit 1.21.11 Inventory

This section is the human source of truth for the repository. If an old count or a historical note contradicts this list, this list wins.

### Base/Core ready for 1.21.11

| Component | Status | Note |
| :--- | :--- | :--- |
| `sources/dough-core` | `Ready` | Unified `1.3.1-DRAKE` base used by the full stack. |
| `sources/slimefun-core/Slimefun4-src` | `Ready` | Core stabilized with compatibility bridges for legacy addons. |

### Batch 1 `sources/repos-to-port` ready for 1.21.11

All `26/26` modules in this folder are considered ready in this branch.

| Addon | Status | Note |
| :--- | :--- | :--- |
| `ColoredEnderChests` | `Ready` | No special note at this time. |
| `DyedBackpacks` | `Ready` | No special note at this time. |
| `DynaTech` | `Ready` | Aligned with the Drake reactor and current dependencies. |
| `EcoPower` | `Ready` | Parent inheritance and `dough-core` dependency were corrected. |
| `ElectricSpawners` | `Ready` | No special note at this time. |
| `ExoticGarden` | `Ready` | Already adapted to this stack; runtime review is still recommended if external integrations are used. |
| `ExtraGear` | `Ready` | No special note at this time. |
| `ExtraHeads` | `Ready` | Converted and integrated into the unified Maven reactor. |
| `ExtraUtils` | `Ready` | Useful reference for old community dependency patterns. |
| `FluffyMachines` | `Ready` | No special note at this time. |
| `GlobalWarming` | `Ready` | No special note at this time. |
| `HardcoreSlimefun` | `Ready` | No special note at this time. |
| `HotbarPets` | `Ready` | API breakages were corrected during the 1.21.11 port. |
| `InfinityExpansion` | `Ready` | Depends on the local reactor `InfinityLib`. |
| `luckyblocks-sf` | `Ready` | No special note at this time. |
| `MobCapturer` | `Ready` | Converted and integrated into the unified Maven reactor. |
| `PrivateStorage` | `Ready` | API errors were fixed during migration. |
| `SFCalc` | `Ready` | Validated as a quick win. |
| `SFMobDrops` | `Ready` | No special note at this time. |
| `SimpleUtils` | `Ready` | Final closure depended on aligning `InfinityLib` with the current reactor. |
| `SlimeChem` | `Ready` | API errors were fixed during the port. |
| `SlimefunOreChunks` | `Ready` | No special note at this time. |
| `SlimyRepair` | `Ready` | Validated as a quick win. |
| `SlimyTreeTaps` | `Ready` | Compiles in this branch; runtime review is recommended if protection plugins are involved. |
| `SoulJars` | `Ready` | No special note at this time. |
| `SoundMuffler` | `Ready` | Legacy API issues were fixed. A smoke test is recommended because it touches sounds and events. |

### Active Batch 2 ready for 1.21.11

These are the libraries and active variants currently used by the stack.

| Component | Status | Note |
| :--- | :--- | :--- |
| `SefiLib` | `Ready` | Support library for expansions. |
| `InfinityLib` | `Ready` | Aligned with the parent and used by multiple ecosystem addons. |
| `Cultivation_Updated` | `Ready` | Adopted active variant. Do not confuse it with the original `Cultivation`. |
| `LiteXpansion` | `Ready` | No special note at this time. |
| `Networks_Better_Compatibility` | `Ready` | Adopted active variant. Do not confuse it with the original `Networks`. |
| `SlimeTinker` | `Ready` | Aligned with the reactor parent and `Slimefun 6.0-Drake-1.21.11`. |
| `SMG` | `Ready` | Listed in the reactor as `SimpleMaterialGenerators`. |
| `Supreme` | `Ready` | No special note at this time. |
| `TranscEndence` | `Ready` | No special note at this time. |

### Active community addons ready for 1.21.11

These modules are already integrated into the reactor and compile today in this branch.

| Addon | Status | Note |
| :--- | :--- | :--- |
| `AlchimiaVitae` | `Ready` | No special note at this time. |
| `CrystamaeHistoria` | `Ready` | No special note at this time. |
| `DankTech2` | `Ready` | No special note at this time. |
| `DyeBench` | `Ready` | Integrated into the reactor and migrated to `dev.drake.dough.*`. |
| `Element-Manipulation` | `Ready` | Required a real API and serializer port; runtime smoke testing is still recommended. |
| `ExtraTools` | `Ready` | Verified through isolated build. |
| `FlowerPower` | `Ready` | Adjusted to modern Bukkit/Paper attribute APIs. |
| `FN-FAL-s-Amplifications` | `Ready` | Verified through isolated build. |
| `FoxyMachines` | `Ready` | Cleaned from old legacy utility usage. |
| `HeadLimiter` | `Ready` | Integrated into the reactor. `Towny` is optional and should be runtime-tested if used. |
| `GeneticChickengineering-Reborn` | `Ready` | Compiles in the reactor with targeted optimization and dependency fixes; runtime validation is still recommended. |
| `Liquid` | `Ready` | Verified through isolated build. |
| `Magic-8-Ball` | `Ready` | Validated as a quick win. |
| `MapJammers` | `Ready` | Requires `squaremap` or `dynmap` at runtime to be functionally useful. |
| `MiniBlocks` | `Ready` | Integrated with the local `InfinityLib`. Still emits deprecated Bukkit API warnings, but compiles on Java 21. |
| `MissileWarfare` | `Ready` | Obsolete particle usage was fixed; smoke testing is recommended because it affects combat and effects. |
| `PotionExpansion` | `Ready` | Ported to the current potion API and now compiles in the reactor. |
| `RykenSlimeCustomizer-EN` | `Ready` | Verified through isolated build. |
| `SfChunkInfo` | `Ready` | Validated as a quick win. |
| `Simple-Storage` | `Ready` | Verified through isolated build. Runtime testing is recommended because it touches inventories and networks. |
| `SlimeCustomizer` | `Ready` | Verified through isolated build. Runtime testing is recommended because it is highly configurable. |
| `UltimateGenerators2` | `Ready` | Converted from Gradle to Maven, aligned to `dev.drake.dough.*`, and integrated into the unified reactor. |
| `VillagerUtil` | `Ready` | Verified through isolated build. |

### Active in reactor but still NOT ready

These modules are already part of the unified build, but still have confirmed breakage or require further technical porting.

| Addon | Status | Note |
| :--- | :--- | :--- |
| - | - | All modules currently active in the reactor are stable at build level. |

### Addons present in the repo but still outside the reactor

These addons still need integration, review, or closure. As long as they remain here, they must not be counted as ready for `1.21.11`.

| Addon | Status | Note |
| :--- | :--- | :--- |
| `Cultivation` | `Outside reactor` | Old variant; the active one is `Cultivation_Updated`. |
| `EMC2` | `Outside reactor` | Historical variant; requires triage before spending time on it. |
| `Galactifun` | `Outside reactor` | Likely a heavier case or one with its own tooling. |
| `Networks` | `Outside reactor` | Old variant; the active one is `Networks_Better_Compatibility`. |
| `AdvancedTech` | `Outside reactor` | Pending triage. |
| `Better-Nuclear-Generator` | `Outside reactor` | Pending triage. |
| `Bump` | `Outside reactor` | Suspicious of requiring extra build or tooling work. |
| `CompressionCraft` | `Outside reactor` | Pending integration. |
| `CustomItemGenerators` | `Outside reactor` | Suspicious of requiring extra build or tooling work. |
| `EMCTech` | `Outside reactor` | Pending integration. |
| `FastMachines` | `Outside reactor` | Suspicious of requiring extra build or tooling work. |
| `Gastronomicon` | `Outside reactor` | Mid-tier candidate. |
| `Geyser-Slimefun-Heads` | `Outside reactor` | Mid-tier candidate. |
| `MoreResearches` | `Outside reactor` | Quick win candidate. |
| `Netheopoiesis` | `Outside reactor` | Pending triage. |
| `Quaptics` | `Outside reactor` | Quick win candidate. |
| `RelicsOfCthonia` | `Outside reactor` | Mid-tier candidate. |
| `SaneCrafting` | `Outside reactor` | Pending integration. |
| `SfBetterChests` | `Outside reactor` | Quick win candidate. |
| `SlimeFrame` | `Outside reactor` | Likely a heavy case. |
| `SlimefunAdvancements` | `Outside reactor` | Likely a heavy case. |
| `SlimefunTranslation` | `Outside reactor` | Suspicious of requiring extra tooling or conversion work. |
| `SlimefunWarfare` | `Outside reactor` | Historical variant; triage before integration. |
| `SlimeHUD` | `Outside reactor` | Quick win candidate. |
| `SmallSpace` | `Outside reactor` | Quick win candidate. |
| `SpiritsUnchained` | `Outside reactor` | Pending integration. |
| `VillagerTrade` | `Outside reactor` | Mid-tier candidate. |
| `Wildernether` | `Outside reactor` | Mid-tier candidate. |
| `WorldEditSlimefun` | `Outside reactor` | Mid-tier candidate; current WorldEdit integration should be reviewed. |

## Important Operational Notes

- `MapJammers`: compiles, but its real functionality depends on `squaremap` or `dynmap`.
- `HeadLimiter`: compiles and loads its own logic; if `Towny` is used, wilderness/claim behavior should be runtime-tested.
- `MiniBlocks`: compiles on Java 21, but still emits deprecated Bukkit API warnings.
- `MissileWarfare`: compiles after particle fixes; because it is a combat/visual addon, it needs a real smoke test.
- `Element-Manipulation`: now aligned with modern serializers and utilities; runtime validation is still recommended for effects and mechanics.
- `Simple-Storage`: compiles, but because it touches inventories and network behavior, a live server test is recommended.
- `SlimeCustomizer` and `RykenSlimeCustomizer-EN`: compile, but because they are highly configurable, they should be validated with real configs.
- `Cultivation_Updated` and `Networks_Better_Compatibility`: these are the active variants. Do not mix them with their original variants when discussing what is "ready".
- `GeneticChickengineering-Reborn`: now compiles in the reactor; runtime validation is still recommended for machines, entities, and performance-sensitive behavior.
- `PotionExpansion`: now compiles in the reactor; runtime validation is still recommended for potion behavior and gameplay compatibility.
- `UltimateGenerators2`: now compiles in the reactor after Maven conversion; runtime validation is still recommended for generators, multiblocks, and Kotlin/GuizhanLib interactions.

## Stack Architecture

The ecosystem uses the **Drake Framework** as its technical consolidation base:

- a root `pom.xml` controlling versions, modules, and dependencies
- `dough-core` relocated to `dev.drake.dough`
- a Slimefun core adapted for `1.21.11`
- active compatibility variants where the original historical addon is not the correct current target

For deeper context:

- [Ecosystem Architecture](docs/en/ecosystem-architecture.md)
- [Ecosystem Architecture on the Wiki](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Ecosystem-Architecture)
- [Technical Reference (Paper 1.21.1)](docs/en/technical-reference-paper-1.21.1.md)
- [Technical Reference (Paper 1.21.1) on the Wiki](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Technical-Reference-(Paper-1.21.1))

## Supporting Documentation

| Topic | Local Mirror | GitHub Wiki |
| :--- | :--- | :--- |
| Status and priorities | [Migration Checklist](docs/en/migration-checklist.md) | [Migration Checklist](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Migration-Checklist) |
| Detailed backlog | [Pending Modules](docs/en/pending-modules.md) | [Pending Modules](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Pending-Modules) |
| Operational roadmap | [Stabilization Roadmap](docs/en/stabilization-roadmap.md) | [Stabilization Roadmap](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Stabilization-Roadmap) |
| Smoke test | [Smoke Test Guide](docs/en/smoke-test-guide.md) | [Smoke Test Guide](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Smoke-Test-Guide) |
| Handoff | [Tomorrow Handoff](docs/en/tomorrow-handoff.md) | [Tomorrow Handoff](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Tomorrow-Handoff-EN) |
| Local setup | [Development Setup](docs/en/development-setup.md) | [Development Setup](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Development-Setup) |
| Standards | [Development Standards](docs/en/development-standards.md) | [Development Standards](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Development-Standards) |
| AI guide | [AI Instructions](docs/en/ai-instructions.md) | [AI Instructions](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/AI-Instructions) |
| AI start prompt | [AI Start Prompt](docs/en/ai-start-prompt.md) | [AI Start Prompt](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/AI-Start-Prompt-EN) |
| Addon template guide | [New Addon Template](docs/en/new-addon-template.md) | [New Addon Template](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/New-Addon-Template-EN) |
| Release/CI strategy | [Release and CI Strategy](docs/en/release-and-ci-strategy.md) | [Release and CI Strategy](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Release-and-CI-Strategy) |

## New Addon Template

For new modules aligned with the current stack:

- guide: [New Addon Template](docs/en/new-addon-template.md)
- public wiki: [New Addon Template](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/New-Addon-Template-EN)
- actual local base: [templates/slimefun-addon/README.md](templates/slimefun-addon/README.md)

## Credits

This project exists on top of the accumulated work of several Slimefun ecosystem developers.

| Dev | Contribution |
| :--- | :--- |
| **TheBusyBiscuit** | Original author of Slimefun 4. |
| **Sefiraat** | Mentor of expansion libraries and technical addons. |
| **Mooy1** | Creator of InfinityExpansion and a major part of the modular architecture around it. |
| **Chagui68** | Compatibility variants and transition logic. |
| **[Pablo Elías](https://github.com/JackStar6677-1)** | Lead of the `1.21.11` port and Drake Framework consolidation. |

Drakes Slimefun Labs is an independent project. All trademarks belong to their respective owners.
