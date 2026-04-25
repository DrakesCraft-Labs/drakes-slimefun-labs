# AI instructions

## Base context (read before editing)

- **What this repo is:** a **Maven + Gradle** monorepo (**Java 21**), **one source tree**, not a single plugin. It contains **Slimefun 4 (Drake fork)**, **dough-core**, internal patches, and **dozens of addons** under `sources/` (Maven) plus **Gradle** projects (Galactifun, Bump, …) from `settings.gradle.kts`.
- **Stable 1.21.x branch:** **`1.21-latin`**. Compile baseline: **Paper 1.21.1-R0.1-SNAPSHOT** in the root `pom.xml`; smoke targets **Paper 1.21.1** and **1.21.11** depending on profile (`scripts/smoke/smoke-profiles.json`).
- **Experimental 26.x branch:** **`26.X-ToTheStars`** — work toward **Paper API 26.x** (Maven profile `paper-26-preview`). Do not land large 26.x refactors on `1.21-latin` PRs without agreement. **Cadence:** maintainers expect roughly **one month** before resuming 26.x at sprint pace (the 1.21.x push was extremely heavy); meanwhile priority is **keep `1.21-latin` green**, Issues, and smoke when needed.
- **Real-world validation:** **[DrakesCraft](https://drakescraft.cl)** (Chile; typical joins `play.drakescraft.cl` / `mc.drakescraft.cl`). Per-addon polish: **community**, **Chagui**, GitHub **Issues**.

## Working rules

1. Read root **`README.md`** and **`docs/es/PLUGIN_MATRIX.md`** (regenerate: `python scripts/generate_plugin_matrix.py` if modules or CI mapping changed).
2. Prefer **scoped builds**: `mvn -B -pl path/to/module -am -DskipTests package`. Avoid full reactor unless the change requires it or you are doing smoke/release.
3. Separate **POM / resolution failures** from **Paper / Slimefun API** failures before editing.
4. **Shaded Slimefun:** after Slimefun `package`, Dough lives under `com.github.drakescraft_labs.slimefun4.libraries.dough.protection.*`. Addons using `Slimefun.getProtectionManager()` must align imports with the **relocated** types on the addon classpath. Helper: `python scripts/fix_dough_compilation_imports.py` (excludes core modules).
5. **Smoke:** `scripts/smoke/` (`run-smoke-server.ps1`, `smoke_orchestrate.py`); **ProtocolLib** via Python script + PowerShell fallback. Profiles include `*-paper-12111` for Paper 1.21.11.
6. **Releases:** manual workflow **Release monorepo JARs** produces a **ZIP** of jars — see `docs/github-maintenance.md`.
7. If module **status** vs the org board changes, refresh the matrix and align [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) per `docs/PROJECT_BOARD_SYNC.md`.

## Reference command

```bash
mvn -B -pl sources/community-addons/Example -am -DskipTests package
```

## Investigation hints

- If coordinates look Drake-correct but build fails: check **parent POM**, **`slimefun-core`** dependency, and whether the failure is **`compile` only** vs full **`package`** order in the reactor.
- **Gradle addons:** you may need `mvn install` of Drake jars into `mavenLocal()` (e.g. InfinityExpansion) before `gradlew compileJava`; see `docs/en/pending-modules.md`.

## Definition of done

No half-merged PRs. If something does not compile or fails smoke, document the **exact** error and minimal repro.

## Related links

- [AI Start Prompt](ai-start-prompt.md)
- [Migration checklist](migration-checklist.md)
- [Stabilization roadmap](stabilization-roadmap.md)
- [Pending modules](pending-modules.md)
- [Root README](../../README.md)

<!-- DRAKES-STATUS:BEGIN -->
> **2026-04-25** — Expanded AI instructions (smoke, shaded Dough, 26.x branch, DrakesCraft, ~1 month cadence).
> Baseline: **Paper 1.21.x + Java 21** on **`1.21-latin`**. CI: **Monorepo 1.21**.
> **26.x:** branch `26.X-ToTheStars`; heavy sprint deferred ~1 month after 1.21 burn-in.
<!-- DRAKES-STATUS:END -->
