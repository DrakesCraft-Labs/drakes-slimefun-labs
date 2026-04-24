# Tomorrow handoff

## Operational snapshot (April 2026)

- **Branch**: `1.21-latin` | **Identity**: `com.github.drakescraft_labs`
- **Audited inventory**: see root [`README.md`](../../README.md) and [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) — **86 rows** (81 Maven modules in root `pom.xml` + 5 Gradle reactor entries). Regenerate with `python scripts/generate_plugin_matrix.py`.
- **CI**: Gates 1–5 are green on **curated subsets**; do not assume a single job builds all 86.
- **Gradle**: `Galactifun` is OK in CI; four community addons remain **compile-blocked** (details in the matrix and `docs/en/pending-modules.md`).
- **Org board**: [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — keep aligned via [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md).

## CI/CD infrastructure

- Gate workflows (`ci-gate-1` … `ci-gate-5`) validate Maven and Gradle slices separately.
- Regenerate the README/module table after inventory-affecting changes: `python scripts/generate_plugin_matrix.py`.

## DrakesLab Manager

- Use `python scripts/manager.py audit` to inspect repository state before large edits.
- `python scripts/manager.py repair` can fix parent/version/XML identity issues (review diffs).

## Dependency stabilization notes (Phase 2 highlights)

- Several addons needed explicit `dough-core` / Slimefun coordinates even when versions come from the parent.
- Lombok and Paper API 1.21.1 migrations were applied in batches; always verify with targeted `-pl … -am` builds.

## Recommended next route

1. Promote **local-green** Maven modules into the right `ci-gate-*.yml` slice.
2. Triage the **Gradle-blocked** quartet using the matrix “Observaciones” column.
3. Smoke-test high-risk gameplay addons on a real Paper 1.21.1 server.

## Survival commands

- **Audit**: `python scripts/manager.py audit`
- **Maven**: `mvn -pl sources/community-addons/AddonName -am -DskipTests package`
- **Gradle**: `./gradlew :sources:batch-2-expansion:Galactifun:build`

## Links

- [Migration checklist](migration-checklist.md)
- [Release & CI strategy](release-and-ci-strategy.md)
- [Pending modules](pending-modules.md)

<!-- DRAKES-STATUS:BEGIN -->
> Sync cut: **2026-04-24**.
> Active baseline: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **Gates 1–5 green**.
> Note: incremental migration continues for the wider monorepo beyond the gate subsets.
<!-- DRAKES-STATUS:END -->
