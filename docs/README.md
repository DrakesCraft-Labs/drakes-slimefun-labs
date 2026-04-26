# Documentación del monorepo

Fuente de verdad para **Drakes Slimefun Labs** en la rama **`1.21-latin`**: **Paper 1.21.x**, **Java 21**, reactor Maven + proyectos Gradle en la raíz.

## Qué está hecho vs qué sigue

| Hecho en el repo (laboratorio) | Sigue en el mundo real |
|-------------------------------|-------------------------|
| Compilación del reactor en **CI Monorepo 1.21** | Bugs de gameplay, balance, interacciones con otros plugins |
| **Smoke** Paper 1.21.1 / 1.21.11 (perfiles en `scripts/smoke/`) | Pruebas largas en survival, reportes de jugadores |
| **Release** opcional: ZIP de JARs ([workflow](../.github/workflows/release-monorepo-jars.yml)) | Pasta fina **addon por addon** (Chagui, comunidad, staff) |
| Matriz y tablas generadas | **[DrakesCraft](https://drakescraft.cl)** (Chile) como servidor de referencia del pack |

La línea **Paper 26.x** se trabaja en la rama **[`26.X-ToTheStars`](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/tree/26.X-ToTheStars)**; no sustituye a `1.21-latin` hasta que ese porte esté listo.

## Dónde empezar

| Objetivo | Documento |
|----------|-------------|
| Visión general del repo y tabla de módulos | [README raíz](../README.md) (`python scripts/generate_plugin_matrix.py`) |
| Matriz auditada por módulo (no editar a mano) | [es/PLUGIN_MATRIX.md](es/PLUGIN_MATRIX.md) |
| Qué queda a nivel build / historial técnico | [es/pending-modules.md](es/pending-modules.md) |
| Arranque local y convenciones | [en/development-setup.md](en/development-setup.md) · [es/development-setup.md](es/development-setup.md) |
| Smoke test (servidor real) | [en/smoke-test-guide.md](en/smoke-test-guide.md) · [es/smoke-test-guide.md](es/smoke-test-guide.md) |
| Roles (QA / beta), acuerdos de equipo y “campo de pruebas” | [es/colaboracion-y-qa.md](es/colaboracion-y-qa.md) |
| Scripts Python y PowerShell | [../scripts/README.md](../scripts/README.md) |
| Tablero GitHub Projects (org) | [PROJECT_BOARD_SYNC.md](PROJECT_BOARD_SYNC.md) |
| Actions, releases ZIP, PRs, alertas | [github-maintenance.md](github-maintenance.md) |
| Prompt / instrucciones para IA | [es/ai-start-prompt.md](es/ai-start-prompt.md) · [en/ai-start-prompt.md](en/ai-start-prompt.md) y [es/ai-instructions.md](es/ai-instructions.md) · [en/ai-instructions.md](en/ai-instructions.md) |

## Idiomas

- **Inglés**: índice [en/home.md](en/home.md); guías bajo `docs/en/`.
- **Español**: índice [es/home.md](es/home.md); guías bajo `docs/es/`.

Si un texto discrepa del workflow en `.github/workflows/`, manda el **workflow** como referencia.

## Qué queda fuera de esta carpeta

- `sources/**/README.md`: documentación por addon o upstream; no es el manual central del laboratorio.
- Wiki de GitHub (si existe): alinear con `docs/` cuando haga falta.

## Compilación (addons y Slimefun sombreado)

En el reactor, el `package` de **Slimefun** sombrea Dough bajo `com.github.drakescraft_labs.slimefun4.libraries.dough.protection`. Los módulos que dependen de Slimefun compilan **después** de ese paso, así que las llamadas a `Slimefun.getProtectionManager()` deben usar esos tipos relocados, no `dev.drake.dough.protection` (salvo en `slimefun-core` y `dough-core`). La verificación alineada con CI es `mvn package`. Ajuste masivo: `python scripts/fix_dough_compilation_imports.py` (ver [scripts/README.md](../scripts/README.md)).

## Estado operativo (referencia rápida)

- Rama estable **1.21.x**: `1.21-latin`.
- CI principal: **CI Monorepo 1.21** (`.github/workflows/ci-monorepo-121.yml`).
- Smoke opcional: **Smoke Runtime 1.21** (`workflow_dispatch`).
- Servidor de referencia del pack: **[drakescraft.cl](https://drakescraft.cl)**.

Para fechas y cortes históricos, usa Issues o el Project de la org.
