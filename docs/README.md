# Documentación del monorepo

Fuente de verdad para **Drakes Slimefun Labs**: Paper **1.21.1**, Java **21**, reactor Maven + proyectos Gradle declarados en la raíz.

## Dónde empezar

| Objetivo | Documento |
|----------|-------------|
| Visión general del repo y tabla de módulos | [README raíz](../README.md) (regenerar tabla con `python scripts/generate_plugin_matrix.py`) |
| Matriz auditada por módulo (no editar a mano) | [es/PLUGIN_MATRIX.md](es/PLUGIN_MATRIX.md) |
| Arranque local y convenciones | [en/development-setup.md](en/development-setup.md) · [es/development-setup.md](es/development-setup.md) |
| Smoke test (servidor real) | [en/smoke-test-guide.md](en/smoke-test-guide.md) · [es/smoke-test-guide.md](es/smoke-test-guide.md) |
| Scripts Python y PowerShell | [../scripts/README.md](../scripts/README.md) |
| Tablero GitHub Projects (org) | [PROJECT_BOARD_SYNC.md](PROJECT_BOARD_SYNC.md) |
| Limpieza de Actions, PRs y alertas | [github-maintenance.md](github-maintenance.md) |

## Idiomas

- **Inglés**: índice [en/home.md](en/home.md); guías bajo `docs/en/`.
- **Español**: índice [es/home.md](es/home.md); guías bajo `docs/es/`.

Los dos idiomas cubren los mismos temas; si hay divergencia puntual, prioriza el texto que coincida con el estado actual del workflow en `.github/workflows/`.

## Qué queda fuera de esta carpeta

- `sources/**/README.md`: documentación heredada de cada addon o upstream; no forma parte del manual del laboratorio salvo referencia local.
- Wiki de GitHub (si existe): debe alinearse con `docs/en/`; este árbol es la referencia editable en git.

## Compilación (addons y Slimefun sombreado)

En el reactor, el `package` de **Slimefun** sombrea Dough bajo `com.github.drakescraft_labs.slimefun4.libraries.dough.protection`. Los módulos que dependen de Slimefun compilan **después** de ese paso, así que las llamadas a `Slimefun.getProtectionManager()` deben usar esos tipos relocados, no `dev.drake.dough.protection` (salvo en `slimefun-core` y `dough-core`). Un `mvn compile` aislado puede dar una imagen distinta del classpath; la verificación alineada con CI es `mvn package`. Ajuste masivo: `python scripts/fix_dough_compilation_imports.py` (ver [scripts/README.md](../scripts/README.md)).

## Estado operativo (referencia rápida)

- Rama de trabajo habitual: `1.21-latin`.
- CI principal: **CI Monorepo 1.21** (`.github/workflows/ci-monorepo-121.yml`).
- Smoke opcional: **Smoke Runtime 1.21** (manual, `workflow_dispatch`).

Para fechas y cortes históricos, usa Issues o el Project de la org; evita duplicar “handoffs” largos en markdown sueltos.
