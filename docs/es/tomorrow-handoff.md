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
