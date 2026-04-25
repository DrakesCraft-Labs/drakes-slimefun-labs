<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Nota de handoff

Las notas “para mañana” en markdown envejecen mal. Mejor:

- **[docs/README.md](../README.md)** — centro de documentación.
- **Issues en GitHub** — tareas concretas con etiquetas.
- **[docs/github-maintenance.md](../github-maintenance.md)** — Actions, PRs, Dependabot.

Comandos de comprobación (baseline abril 2026):

```bash
python scripts/manager.py audit
mvn -B -DskipTests compile -fae
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
```
