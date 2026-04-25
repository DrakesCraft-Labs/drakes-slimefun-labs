# Release and CI Strategy

## Current policy

Per-module readiness is tracked in the generated root [`README.md`](../../README.md) and [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) (`python scripts/generate_plugin_matrix.py`). Keep the org [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) aligned using [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md).

The repository is operated in an incremental-stability model:

- Keep the main branch (`1.21-latin`) green at all times.
- Keep full Maven + Gradle compile coverage in CI.
- Prefer deterministic dependencies over snapshot-only external sources.

## CI shape

Single workflow [`.github/workflows/ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml) (`CI Monorepo 1.21`):

- **foundation**: Maven baseline (Dough, Slimefun, SefiLib, InfinityLib, commons-lang patch)
- **maven_full_reactor**: all 81 Maven reactor modules with `mvn -B compile -DskipTests -fae`
- **gradle_green**: `compileJava` for all 5 Gradle projects after publishing required Maven artifacts locally
- **ci_summary**: optional umbrella check for branch protection

Concurrency cancels in-progress runs on the same branch to reduce “war zone” noise from stacked pushes.

## Release posture

- Core artifacts are validated continuously through CI.
- Addon-level production readiness still requires runtime smoke checks.
- A passing pipeline is a required condition, not a sufficient production guarantee.

## Next milestones

1. Keep `maven_full_reactor` and `gradle_green` stable.
2. Remove legacy dependency bottlenecks.
3. Track runtime validation status per addon group.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **CI Monorepo 1.21** covers the full Maven reactor + 5 Gradle projects.
> Note: runtime smoke tests and release strategy remain; there are no compile blockers in the current cut.
<!-- DRAKES-STATUS:END -->
