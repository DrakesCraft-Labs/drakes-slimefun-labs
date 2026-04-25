# Tomorrow handoff

## Operational snapshot (April 2026)

- **Branch**: `1.21-latin` | **Identity**: `com.github.drakescraft_labs`
- **Audited inventory**: see root [`README.md`](../../README.md) and [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) — **86 rows** (81 Maven modules in root `pom.xml` + 5 Gradle reactor entries). Regenerate with `python scripts/generate_plugin_matrix.py`.
- **CI**: **CI Monorepo 1.21** is green on **curated subsets**; a full local pass now exists, but CI does not cover all 86 yet.
- **Full local build**: 81 Maven modules + 5 Gradle projects compile on the `2026-04-24` cut.
- **Org board**: [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — keep aligned via [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md).

## CI/CD infrastructure

- Unified workflow [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml) validates Maven slices and the green Gradle subset.
- Regenerate the README/module table after inventory-affecting changes: `python scripts/generate_plugin_matrix.py`.

## DrakesLab Manager

- Use `python scripts/manager.py audit` to inspect repository state before large edits.
- `python scripts/manager.py repair` can fix parent/version/XML identity issues (review diffs).

## Dependency stabilization notes (Phase 2 highlights)

- Several addons needed explicit `dough-core` / Slimefun coordinates even when versions come from the parent.
- Lombok and Paper API 1.21.1 migrations were applied in batches; always verify with targeted `-pl … -am` builds.
- Compatibility bridges now exist for upstream BusyBiscuit packages in Slimefun core (`io.github.thebusybiscuit.slimefun4.*`), plus local Kotlin bridges for `FastMachines` / `UltimateGenerators2` (`MenuBlock`, `TickingMenuBlock`, `DrakeItemBuilderCompat`).

## Recommended next route

1. Promote the full local-green Maven/Gradle coverage into the right slices or jobs in [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml).
2. Smoke-test high-risk gameplay addons on a real Paper 1.21.1 server.
3. Reduce bridge debt where repeated local bridges can become shared API.

## Survival commands

- **Audit**: `python scripts/manager.py audit`
- **Full Maven**: `mvn -B -DskipTests compile -fae`
- **Targeted Maven**: `mvn -pl sources/community-addons/AddonName -am -DskipTests package`
- **Gradle cut**: `./gradlew :sources:batch-2-expansion:Galactifun:compileJava :sources:community-addons:Bump:compileJava :sources:community-addons:CustomItemGenerators:compileJava :sources:community-addons:FastMachines:compileJava :sources:community-addons:SlimefunTranslation:compileJava --no-daemon`
- **FastMachines local artifact**: `mvn -B -DskipTests install -pl sources/repos-to-port/InfinityExpansion -am`

## Links

- [Migration checklist](migration-checklist.md)
- [Release & CI strategy](release-and-ci-strategy.md)
- [Pending modules](pending-modules.md)

<!-- DRAKES-STATUS:BEGIN -->
> Sync cut: **2026-04-24**.
> Active baseline: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **CI Monorepo 1.21** green (curated jobs).
> Note: full local build is green: 81 Maven + 5 Gradle. Remaining gap is wider CI coverage and smoke testing.
<!-- DRAKES-STATUS:END -->
