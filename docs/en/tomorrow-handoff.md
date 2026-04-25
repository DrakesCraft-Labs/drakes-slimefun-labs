<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Handoff note

Long-lived “tomorrow” notes go out of date quickly. Use one of these instead:

- **[docs/README.md](../README.md)** — documentation hub.
- **GitHub Issues** — concrete next tasks with labels.
- **[docs/github-maintenance.md](../github-maintenance.md)** — Actions, PRs, Dependabot.

Snapshot commands (April 2026 baseline):

```bash
python scripts/manager.py audit
mvn -B -DskipTests compile -fae
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
```
