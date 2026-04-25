# Pending modules

This document reflects the real state after the current CI stabilization work.

## Single source of truth (inventory)

- Per-module status (CI-ready / local-only / in progress / build-blocked) and notes: [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) (generated; run `python scripts/generate_plugin_matrix.py`).
- The same table is embedded in the root [`README.md`](../../README.md).
- Org board: [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — see [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md) to align cards with the matrix.

## Current state

- **CI Monorepo 1.21** (`ci-monorepo-121.yml`): green on branch `1.21-latin` for curated Maven/Gradle slices
- Main gap: widen CI coverage beyond the curated gate subsets; most Maven modules are still **in progress** at per-module build evidence level
- Work style: reintroduce modules in small batches + smoke tests for sensitive addons

## Recent technical audit (Gradle batch, root reactor)

Cut date: `2026-04-24`.

### Count summary (local evidence on this date)

| Tool | OK | FAIL | Notes |
|------|---:|-----:|-------|
| Maven (`-pl` LiteXpansion, Supreme, TranscEndence `-am compile`) | 3 target modules + reactor | 0 | `BUILD SUCCESS` |
| Gradle (`gradlew build -x test`, root reactor) | Bump + Galactifun + empty meta-projects | Other Gradle addons fail | Bump and Galactifun compile; the full root `build` still fails on CustomItemGenerators / FastMachines / SlimefunTranslation depending on task order |

### Maven batch (batch-2 expansion)

- `LiteXpansion-drake`, `Supreme-drake`, `TranscEndence-drake` (and `-am` deps): **BUILD SUCCESS** after `libraries-paperlib` in `port_paper_121.py` and bStats wiring in `LiteXpansion.java`.

### Gradle slice (same cut)

- `sources:batch-2-expansion:Galactifun`: **BUILD SUCCESSFUL** (also covered by CI job `gradle_green` in `ci-monorepo-121.yml`)
- `sources:community-addons:Bump`: **BUILD SUCCESS** on `compileJava` (April 2026). Main port: `JavaPlugin` + Drake `SlimefunAddon` (no Guizhan `AbstractAddon` / BusyBiscuit), native `SimpleMenuBlock` on `SlimefunItem` + `BlockMenuPreset`, `LocalizationService` extends `MinecraftLocalization`, 1.21 enchant/flag updates (`POWER`, `SHARPNESS`, `UNBREAKING`, `HIDE_ADDITIONAL_TOOLTIP`), `GuizhanBuildsUpdater.start(...)`. The full root `build` can still fail on other Gradle modules (CIG/FastMachines/SlimefunTranslation).
- `sources:community-addons:CustomItemGenerators`: **FAIL** (Kotlin / `SlimefunAddon` wiring); run `./gradlew :sources:community-addons:CustomItemGenerators:compileKotlin` for the current log.
- `sources:community-addons:FastMachines`: **FAIL** (Kotlin / BusyBiscuit vs Drake types; InfinityExpansion wiring); see matrix.
- `sources:community-addons:SlimefunTranslation`: **FAIL** (broad Java API drift vs Drake Slimefun); see matrix.

## Automated porting (batch patches)

Tool: `scripts/port_paper_121.py` (conservative textual rules for Paper 1.21.1). Includes the `libraries-paperlib` rule (`com.github.drakescraft_labs.slimefun4.libraries.paperlib` → `io.papermc.lib`).

```bash
python scripts/port_paper_121.py --list-rules
python scripts/port_paper_121.py --dry-run --path sources/community-addons/MyAddon
python scripts/port_paper_121.py --apply --path sources/community-addons/MyAddon --rules libraries-dough,libraries-commons
```

Always run `--dry-run` first, review the diff, then `--apply`. With `--backup`, a `.portbak` is written per file (do not commit backups).

## Suggested work blocks

1. Promote **local-compile-green** Maven modules into a new job or `-pl` slice inside [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml).
2. Fix Gradle-blocked addons with the highest leverage ordering (see matrix notes).
3. Run runtime smoke tests for mechanics-heavy addons.
4. Keep the GitHub Project board aligned after each documentation cut.

## Definition of done (per module)

A module is considered closed when:

- it compiles in the matching CI job in [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml) (or a dedicated workflow),
- it does not break the global pipeline,
- and it has minimal runtime validation when gameplay risk is high.

<!-- DRAKES-STATUS:BEGIN -->
> Sync cut: **2026-04-24 (updated after Gradle batch audit)**.
> Active baseline: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **CI Monorepo 1.21** green (curated jobs).
> Note: the full monorepo remains on incremental migration; latest cut: Maven batch LiteXpansion/Supreme/TranscEndence green; Gradle Bump + Galactifun compile; CustomItemGenerators / FastMachines / SlimefunTranslation remain blocked in the Gradle reactor.
<!-- DRAKES-STATUS:END -->
