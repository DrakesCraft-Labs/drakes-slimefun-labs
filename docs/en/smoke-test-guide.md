# Runtime Smoke Test Guide

Monorepo documentation index: [`docs/README.md`](../README.md).

## Goal

The runtime smoke test verifies that Drake jars do more than compile: they must load on a real Paper server. This is the minimum runtime gate before calling a major ecosystem cut stable.

## Available Base

The smoke base lives in `scripts/smoke/`:

- `smoke-profiles.json`: declares which modules are packaged and which log markers must appear.
- `build-smoke-artifacts.ps1`: packages the profile modules and copies jars into `.smoke/<profile>/artifacts/plugins`.
- `run-smoke-server.ps1`: downloads Paper `1.21.1`, prepares a temporary server, copies plugins, starts it, waits for `Done`, stops it with `stop`, and validates logs.
- `smoke_orchestrate.py`: from repo root, chains Maven (`full` / `mvn-package` / `mvn-package-pl`), artifact build, and server run.
- `README.md`: operational detail for the smoke folder.

## Profiles

- `foundation`: Paper `1.21.1` + Drake `Slimefun` core. This should always stay green.
- `core-addons`: `foundation` plus a small Maven addon set for broader runtime coverage once core is stable.
- `monorepo-all`: large addon set; slow and resource-heavy. Use after a full `mvn package` or with `smoke_orchestrate.py full --skip-mvn` when jars are already built.

## Local Usage (orchestrator)

From repository root:

```bash
python scripts/smoke/smoke_orchestrate.py full --profile foundation --clean --timeout 120
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
```

## Local Usage (PowerShell only)

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\run-smoke-server.ps1 -Profile foundation -Clean -TimeoutSeconds 120
```

To only prepare jars:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\build-smoke-artifacts.ps1 -Profile foundation -Clean
```

## GitHub Smoke

The manual workflow `Smoke Runtime 1.21` runs the same smoke runner in GitHub Actions. It is `workflow_dispatch` only to avoid noisy Actions history.

## Verification banner

`Slimefun` startup prints a green DrakesCraft banner. The smoke runner checks that the log contains the **verification strings** configured for your profile in `scripts/smoke/smoke-profiles.json` (Drake pack markers, org repository link, etc.).

If any expected token is missing, smoke fails — confirming the server loaded a current Drake jar rather than a stale or generic artifact.

## Success Criteria

- Paper reaches `Done`.
- Log must not match the fatal regex list in `run-smoke-server.ps1` (including `Error occurred while enabling`, `Could not load plugin`, `NoClassDefFoundError`, `[SEVERE]`, and related load/exception patterns).
- Banner markers are present (see **Verification Banner**).

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: local runtime smoke base and manual `Smoke Runtime 1.21` workflow are available.
<!-- DRAKES-STATUS:END -->
