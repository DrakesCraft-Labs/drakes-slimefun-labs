<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

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
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
