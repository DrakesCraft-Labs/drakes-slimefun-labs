<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# AI Start Prompt

Use this summary as a starting point when opening a new session and you need quick context without rereading the whole repository.

## Short context

- branch: `1.21-latin`
- stack: `Paper 1.21.1`, `Java 21`, Slimefun ecosystem (see root `pom.xml`)
- audited inventory: **86** reactor entries (see `README.md` + `docs/es/PLUGIN_MATRIX.md`)
- CI: **CI Monorepo 1.21** covers all 86 modules: full Maven reactor + 5 Gradle projects in `ci-monorepo-121.yml`

## Key rules

- use the full CI as the main gate; compile the whole reactor locally only before large changes
- use `mvn -pl <path> -am -DskipTests package`
- before editing code, distinguish `pom.xml` failures from API failures
- if a state changes, run `python scripts/generate_plugin_matrix.py` and align the org Project board (`docs/PROJECT_BOARD_SYNC.md`)

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **CI Monorepo 1.21** covers the full Maven reactor + 5 Gradle projects.
> Note: runtime smoke tests and release strategy remain; there are no compile blockers in the current cut.
<!-- DRAKES-STATUS:END -->
