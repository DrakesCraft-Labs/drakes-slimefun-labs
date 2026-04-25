# Release and CI Strategy

## Current policy

Per-module readiness is tracked in the generated root [`README.md`](../../README.md) and [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) (`python scripts/generate_plugin_matrix.py`). Keep the org [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) aligned using [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md).

The repository is operated in an incremental-stability model:

- Keep the main branch (`1.21-latin`) green at all times.
- Expand module coverage in controlled batches.
- Prefer deterministic dependencies over snapshot-only external sources.

## CI shape

Single workflow [`.github/workflows/ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml) (`CI Monorepo 1.21`):

- **foundation**: Maven baseline (Dough, Slimefun, SefiLib, InfinityLib, commons-lang patch)
- **maven_stable**, **maven_community**, **maven_complex**: parallel Maven slices after `foundation` succeeds
- **gradle_green**: `compileJava` for Gradle modules that already port (Bump + Galactifun), not the whole failing Gradle reactor
- **ci_summary**: optional umbrella check for branch protection

Concurrency cancels in-progress runs on the same branch to reduce “war zone” noise from stacked pushes.

## Release posture

- Core artifacts are validated continuously through CI.
- Addon-level production readiness still requires runtime smoke checks.
- A passing pipeline is a required condition, not a sufficient production guarantee.

## Next milestones

1. Reintroduce broader module sets in `ci-monorepo-121.yml` jobs (`-pl` slices).
2. Remove legacy dependency bottlenecks.
3. Track runtime validation status per addon group.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** en verde (jobs curados en `ci-monorepo-121.yml`).
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
