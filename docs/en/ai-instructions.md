# AI Instructions

## Base Context

This repository is a migration lab for Slimefun 4 (Drake fork), `Paper 1.21.1`, `Java 21`, and the shared Dough/Slimefun stack pinned in the root POM.

It is not a single plugin.

## Working Rules

- read root `README.md` and generated `docs/es/PLUGIN_MATRIX.md` first (refresh with `python scripts/generate_plugin_matrix.py` if gates changed)
- use isolated builds with `-pl` and `-am`
- do not build the full reactor unless strictly necessary
- determine whether a failure is Maven-related or API-related before editing code
- synchronize documentation if the board changes

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
