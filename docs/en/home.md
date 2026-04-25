<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Documentation home (English)

**This branch:** `26.X-ToTheStars` (Paper / Minecraft **26.x** port; see [paper-26-base.md](../paper-26-base.md)). **Stable baseline & smoke:** Paper **1.21.x** · Java **21** · branch **`1.21-latin`**.

## Start here

1. **[docs/README.md](../README.md)** — canonical index (bilingual pointers + tables).
2. **[README.md](../../README.md)** — repository overview and generated module table (`python scripts/generate_plugin_matrix.py`).
3. **[PLUGIN_MATRIX.md](../es/PLUGIN_MATRIX.md)** — audited per-module rows (Spanish headers; do not hand-edit).

## Guides

| Topic | File |
|-------|------|
| Paper 26.x (this branch) | [paper-26-base.md](../paper-26-base.md) |
| Local setup | [development-setup.md](development-setup.md) |
| Smoke test (Paper) | [smoke-test-guide.md](smoke-test-guide.md) |
| Migration checklist | [migration-checklist.md](migration-checklist.md) |
| CI / release notes | [release-and-ci-strategy.md](release-and-ci-strategy.md) |
| Technical baseline | [technical-reference-paper-1.21.1.md](technical-reference-paper-1.21.1.md) |
| GitHub hygiene (runs, PRs, alerts) | [github-maintenance.md](../github-maintenance.md) |

## Org board

[DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — align with the matrix using [PROJECT_BOARD_SYNC.md](../PROJECT_BOARD_SYNC.md).
