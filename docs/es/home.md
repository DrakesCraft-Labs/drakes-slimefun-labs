<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Inicio de documentación (Español)

**Rama de este árbol:** `26.X-ToTheStars` (porte **Paper / Minecraft 26.x**; ver [paper-26-base.md](../paper-26-base.md)). **Baseline estable y smoke:** Paper **1.21.x** · Java **21** · rama **`1.21-latin`**.

## Por dónde empezar

1. **[docs/README.md](../README.md)** — índice canónico del monorepo.
2. **[README.md](../../README.md)** — visión general y tabla de módulos generada (`python scripts/generate_plugin_matrix.py`).
3. **[PLUGIN_MATRIX.md](PLUGIN_MATRIX.md)** — filas auditadas por módulo (no editar a mano).

## Guías

| Tema | Archivo |
|------|---------|
| Paper 26.x (rama actual) | [paper-26-base.md](../paper-26-base.md) |
| Entorno local | [development-setup.md](development-setup.md) |
| Smoke en Paper | [smoke-test-guide.md](smoke-test-guide.md) |
| Checklist de migración | [migration-checklist.md](migration-checklist.md) |
| CI / releases | [release-and-ci-strategy.md](release-and-ci-strategy.md) |
| Referencia técnica | [technical-reference-paper-1.21.1.md](technical-reference-paper-1.21.1.md) |
| Actions, PRs, alertas | [github-maintenance.md](../github-maintenance.md) |

## Tablero de la organización

[DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — sincronización descrita en [PROJECT_BOARD_SYNC.md](../PROJECT_BOARD_SYNC.md).
