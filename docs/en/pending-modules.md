# Pending modules

This document reflects the real state after the current CI stabilization work.

## Single source of truth (inventory)

- Per-module status (CI-ready / local-only / in progress / build-blocked) and notes: [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) (generated; run `python scripts/generate_plugin_matrix.py`).
- The same table is embedded in the root [`README.md`](../../README.md).
- Org board: [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — see [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md) to align cards with the matrix.

## Current state

- Gates 1–5: green on branch `1.21-latin`
- Main gap: widen CI coverage beyond the curated gate subsets; most Maven modules are still **in progress** at per-module build evidence level
- Work style: reintroduce modules in small batches + smoke tests for sensitive addons

## Recent technical audit (Gradle batch, root reactor)

Cut date: `2026-04-24`.

### Count summary (local evidence on this date)

| Tool | OK | FAIL | Notes |
|------|---:|-----:|-------|
| Maven (`-pl` LiteXpansion, Supreme, TranscEndence `-am compile`) | 3 target modules + reactor | 0 | `BUILD SUCCESS` |
| Gradle (`gradlew build -x test`, root reactor) | Galactifun + empty meta-projects | Bump stops the run | Task order fails at `:sources:community-addons:Bump:compileJava` before CIG/FastMachines/SlimefunTranslation are re-checked in the same invocation |

### Maven batch (batch-2 expansion)

- `LiteXpansion-drake`, `Supreme-drake`, `TranscEndence-drake` (and `-am` deps): **BUILD SUCCESS** after `libraries-paperlib` in `port_paper_121.py` and bStats wiring in `LiteXpansion.java`.

### Gradle slice (same cut)

- `sources:batch-2-expansion:Galactifun`: **BUILD SUCCESSFUL** (also covered by CI Gate 5)
- `sources:community-addons:Bump`: **FAIL** (~92 `compileJava` errors). Root cause: `net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon` is compiled against `io.github.thebusybiscuit.slimefun4.*`, which is **not** on the classpath for Drake’s `slimefun-core` (`com.github.drakescraft_labs.slimefun4.*`), so type-checking breaks and errors cascade (e.g. Lombok-generated accessors until the hierarchy resolves). Repo mitigations already applied: `io.freefair.lombok`, GuizhanLib 2.x `common.utils` imports, `LocalizationService` extends `SlimefunLocalization`, corrected Bukkit `main` in `build.gradle`. Design follow-up: vendor/fork `AbstractAddon` for Drake’s namespace or a compile-only strategy that stays runtime-safe. Additional 1.21 API drift shows up (`ItemFlag.HIDE_POTION_EFFECTS`, `Enchantment.LUCK`, `Attribute.HORSE_JUMP_STRENGTH`).
- `sources:community-addons:CustomItemGenerators`: **not re-checked** in the latest root `build` (Bump fails first in current task order).
- `sources:community-addons:FastMachines`: **not re-checked** (same).
- `sources:community-addons:SlimefunTranslation`: **not re-checked** (same); earlier cuts: broad Java API mismatches.

## Automated porting (batch patches)

Tool: `scripts/port_paper_121.py` (conservative textual rules for Paper 1.21.1). Includes the `libraries-paperlib` rule (`com.github.drakescraft_labs.slimefun4.libraries.paperlib` → `io.papermc.lib`).

```bash
python scripts/port_paper_121.py --list-rules
python scripts/port_paper_121.py --dry-run --path sources/community-addons/MyAddon
python scripts/port_paper_121.py --apply --path sources/community-addons/MyAddon --rules libraries-dough,libraries-commons
```

Always run `--dry-run` first, review the diff, then `--apply`. With `--backup`, a `.portbak` is written per file (do not commit backups).

## Suggested work blocks

1. Promote **local-compile-green** Maven modules into an appropriate `ci-gate-*.yml` slice.
2. Fix Gradle-blocked addons with the highest leverage ordering (see matrix notes).
3. Run runtime smoke tests for mechanics-heavy addons.
4. Keep the GitHub Project board aligned after each documentation cut.

## Definition of done (per module)

A module is considered closed when:

- it compiles in the matching CI gate (or a dedicated workflow),
- it does not break the global pipeline,
- and it has minimal runtime validation when gameplay risk is high.

<!-- DRAKES-STATUS:BEGIN -->
> Sync cut: **2026-04-24 (updated after Gradle batch audit)**.
> Active baseline: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **Gates 1–5 green**.
> Note: the full monorepo remains on incremental migration; latest cut: Maven batch LiteXpansion/Supreme/TranscEndence green; root Gradle with Galactifun green and Bump blocked (GuizhanLib vs Drake Slimefun namespace).
<!-- DRAKES-STATUS:END -->
