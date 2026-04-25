# AI Start Prompt (copy/paste for a new session)

Use this block as **short memory** for assistants.

```text
Repo: DrakesCraft-Labs / drakes-slimefun-labs
Stable branch: 1.21-latin (Paper 1.21.x API, Java 21). NOT a single plugin: Maven+Gradle monorepo (~86 modules).
Experimental 26.x: 26.X-ToTheStars (Maven profile paper-26-preview). Do not mix into 1.21 without agreement.
Human note: the 1.21.x sprint was extremely heavy; expect ~1 month before 26.x is pushed at full sprint pace; priority = keep 1.21-latin green + Issues + DrakesCraft.

Before coding:
- Root README.md + docs/es/PLUGIN_MATRIX.md
- Maven failure vs Paper/Slimefun API failure
- Slimefun shaded jar relocates Dough → use com.github.drakescraft_labs.slimefun4.libraries.dough.protection.* for ProtectionManager paths when compiling addons (fix_dough_compilation_imports.py)

Typical build: mvn -B -pl <module-path> -am -DskipTests package

Smoke: scripts/smoke/ + smoke_orchestrate.py (profiles include Paper 1.21.11 variants).
Release ZIP: release-monorepo-jars.yml (manual Actions).
Reference survival server (Chile): https://drakescraft.cl — gameplay polish: community + Chagui + Issues.
```

## More detail (if one paragraph is not enough)

| Topic | Where |
|-------|--------|
| Lab vs gameplay backlog | `docs/en/pending-modules.md`, root `README.md` |
| Paper smoke + ProtocolLib | `scripts/smoke/README.md` |
| ZIP releases | `docs/github-maintenance.md` |
| Long AI rules | `docs/en/ai-instructions.md` |
| What 26.x is | branch `26.X-ToTheStars` → `docs/paper-26-base.md` on GitHub |

## Suggested focus order

1. Keep **CI Monorepo 1.21** green.
2. Run **smoke** after large addon changes.
3. Triage **Issues** from **DrakesCraft** / players.
4. Keep **26.x** work on its branch without forcing timeline during the ~1 month recovery window.

## References

- [AI instructions](ai-instructions.md)
- [Migration checklist](migration-checklist.md)
- [Pending modules](pending-modules.md)
- [Tomorrow handoff](tomorrow-handoff.md)

<!-- DRAKES-STATUS:BEGIN -->
> **2026-04-25** — Expanded prompt (26.x cadence, DrakesCraft, smoke, release).
<!-- DRAKES-STATUS:END -->
