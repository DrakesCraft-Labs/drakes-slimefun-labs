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
