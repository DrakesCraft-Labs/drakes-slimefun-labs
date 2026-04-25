<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Walkthrough

Este archivo existía como informe narrativo largo y quedaba desalineado con el CI en días sucesivos.

**Usa en su lugar:**

- [docs/README.md](docs/README.md) — índice de documentación del laboratorio.
- [README.md](README.md) — visión del monorepo y tabla de módulos (regenerar con `python scripts/generate_plugin_matrix.py`).
- [docs/github-maintenance.md](docs/github-maintenance.md) — Actions, PRs y alertas de seguridad.

Los README bajo `sources/` son documentación por-addon o heredada; no intentan describir el monorepo completo. En **`26.X-ToTheStars`** suelen incluir además el aviso de porte **Paper 26.x** al inicio del archivo.
