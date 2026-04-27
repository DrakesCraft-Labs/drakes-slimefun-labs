# Wiki del laboratorio (Drakes Slimefun Labs)

**Rama estable:** `1.21-latin` · **Paper 1.21.x** · **Java 21**.

Esta carpeta es el **índice operativo** del monorepo: enlaza guías ya existentes en `docs/` y añade notas que no encajan en un solo addon. Si el repositorio tiene **GitHub Wiki** activada, lo recomendable es dejarla como **portal** que apunte aquí (`docs/wiki/`, `docs/README.md`) para no mantener dos fuentes de verdad.

---

## Mapa rápido

| Necesitas… | Documento |
|------------|--------------|
| Índice general (ES/EN) | [docs/README.md](../README.md) |
| Tabla de módulos (generada; no editar a mano) | [docs/es/PLUGIN_MATRIX.md](../es/PLUGIN_MATRIX.md) |
| CI, Actions, borrado de runs, **releases JAR** | [docs/github-maintenance.md](../github-maintenance.md) |
| Política CI / releases / hitos | [docs/es/release-and-ci-strategy.md](../es/release-and-ci-strategy.md) · [docs/en/release-and-ci-strategy.md](../en/release-and-ci-strategy.md) |
| Scripts Python / smoke | [scripts/README.md](../../scripts/README.md) · [scripts/smoke/README.md](../../scripts/smoke/README.md) |
| Tablero org (Project 1) | [docs/PROJECT_BOARD_SYNC.md](../PROJECT_BOARD_SYNC.md) |
| Auto-updater + despliegue en servidor | [Runtime: DrakesLabs auto-updater](runtime-drakes-autoupdate.md) |
| Aircraft: YAML de vehículos y revisión en disco | [Runtime: Aircraft (YAML)](runtime-aircraft-yaml.md) |

---

## Guías por tema (ES / EN)

| Tema | ES | EN |
|------|----|----|
| Inicio | [es/home.md](../es/home.md) | [en/home.md](../en/home.md) |
| Entorno local | [es/development-setup.md](../es/development-setup.md) | [en/development-setup.md](../en/development-setup.md) |
| Smoke Paper | [es/smoke-test-guide.md](../es/smoke-test-guide.md) | [en/smoke-test-guide.md](../en/smoke-test-guide.md) |
| Colaboración / QA | [es/colaboracion-y-qa.md](../es/colaboracion-y-qa.md) | — |
| Guía monorepo | [es/guia-monorepo.md](../es/guia-monorepo.md) | [en/monorepo-work-guide.md](../en/monorepo-work-guide.md) |
| Contexto rápido (copiar/pegar) | [es/contexto-rapido-repo.md](../es/contexto-rapido-repo.md) | [en/repo-session-brief.md](../en/repo-session-brief.md) |

---

## Ramas y línea roja

Política del repo en GitHub: **solo dos ramas largas** en el remoto:

| Rama | Rol |
|------|-----|
| **`1.21-latin`** | Línea estable Paper **1.21.x** + Java 21 (rama por defecto / `HEAD`). |
| **`26.X-ToTheStars`** | Línea experimental Paper **API 26.x** (BOMs y supuestos distintos). |

- **No** fusionar ni rebase cruzado entre `1.21-latin` y `26.X-ToTheStars` (corrompe `pom.xml`, módulos y CI). README raíz y reglas de Cursor del laboratorio.

**Ramas de corta vida:** integración vía **PR**; tras fusionar, borrar la rama cabeza en GitHub para no acumular ruido. **Dependabot** abre ramas `dependabot/...` por cada PR de dependencias: al cerrar o fusionar el PR, GitHub puede borrar la rama; si reaparecen, es normal (nuevo bump). Para depender solo de las dos ramas largas, revisa y fusiona o cierra PRs de Dependabot con frecuencia, o ajusta `.github/dependabot.yml` (intervalo / límites) con criterio del equipo.

---

## Builds y calidad

1. **CI Monorepo 1.21** (`.github/workflows/ci-monorepo-121.yml`): fundación Maven, reactor completo, Gradle verde.
2. **Smoke** (opcional, manual): `scripts/smoke/`, `smoke_orchestrate.py`.
3. **Release monorepo JARs** (manual): un tag de GitHub Release con **un asset `.jar` por módulo** + `manifest.json`; alimenta el auto-updater embebido en muchos addons.

---

## English (short mirror)

**Stable branch:** `1.21-latin` · **Paper 1.21.x** · **Java 21**.

This folder is the **lab wiki hub**: it links canonical docs under `docs/` and adds runtime notes (see `runtime-drakes-autoupdate.md`, `runtime-aircraft-yaml.md`). Prefer a thin **GitHub Wiki** that points here instead of duplicating content.

| Need | Doc |
|------|-----|
| Canonical index | [docs/README.md](../README.md) |
| Generated module table | [docs/es/PLUGIN_MATRIX.md](../es/PLUGIN_MATRIX.md) |
| Actions, releases | [docs/github-maintenance.md](../github-maintenance.md) |
| CI / release policy | [docs/en/release-and-ci-strategy.md](../en/release-and-ci-strategy.md) |
| Scripts | [scripts/README.md](../../scripts/README.md) |
| Auto-updater + deploy | [Runtime: DrakesLabs auto-updater](runtime-drakes-autoupdate.md) |
| Aircraft vehicle YAML | [Runtime: Aircraft (YAML)](runtime-aircraft-yaml.md) |

**Do not cross-merge** `1.21-latin` ↔ `26.X-ToTheStars` (repo policy).
