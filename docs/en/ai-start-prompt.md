# AI Start Prompt

Use this summary as a starting point when opening a new session and you need quick context without rereading the whole repository.

## Short context

- branch: `1.21-latin`
- stack: `Paper 1.21.1`, `Java 21`, Slimefun ecosystem (see root `pom.xml`)
- audited inventory: **86** reactor entries (see `README.md` + `docs/es/PLUGIN_MATRIX.md`)
- CI: Gates 1–5 green on curated subsets (not full monorepo per-module proof)

## Key rules

- do not compile the whole reactor unless strictly necessary
- use `mvn -pl <path> -am -DskipTests package`
- before editing code, distinguish `pom.xml` failures from API failures
- if a state changes, run `python scripts/generate_plugin_matrix.py` and align the org Project board (`docs/PROJECT_BOARD_SYNC.md`)

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
