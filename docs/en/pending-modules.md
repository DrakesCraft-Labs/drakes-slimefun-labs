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

- `sources:batch-2-expansion:Galactifun`: **BUILD SUCCESSFUL** (also covered by CI Gate 5)
- `sources:community-addons:Bump`: **FAIL** (large Java API drift vs Drake baseline; partial mitigation via `port_paper_121`)
- `sources:community-addons:CustomItemGenerators`: **FAIL** (Kotlin addon base / Slimefun wiring)
- `sources:community-addons:FastMachines`: **FAIL** (Kotlin types vs fork APIs)
- `sources:community-addons:SlimefunTranslation`: **FAIL** (broad Java API mismatches)

## Automated porting (batch patches)

Tool: `scripts/port_paper_121.py` (conservative textual rules for Paper 1.21.1).

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
> Note: the full monorepo remains on incremental migration; Gradle batch shows 1 green module and 4 failing migration/API/code paths in that slice.
<!-- DRAKES-STATUS:END -->
