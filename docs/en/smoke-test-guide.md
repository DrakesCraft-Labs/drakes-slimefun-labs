# Runtime Smoke Test Guide

## Goal

The runtime smoke test verifies that Drake jars do more than compile: they must load on a real Paper server. This is the minimum runtime gate before calling a major ecosystem cut stable.

## Available Base

The smoke base lives in `scripts/smoke/`:

- `smoke-profiles.json`: declares which modules are packaged and which log markers must appear.
- `build-smoke-artifacts.ps1`: packages the profile modules and copies jars into `.smoke/<profile>/artifacts/plugins`.
- `run-smoke-server.ps1`: downloads Paper `1.21.1`, prepares a temporary server, copies plugins, starts it, waits for `Done`, stops it with `stop`, and validates logs.
- `README.md`: quick local and GitHub Actions reference.

## Profiles

- `foundation`: Paper `1.21.1` + Drake `Slimefun` core. This should always stay green.
- `core-addons`: `foundation` plus a small Maven addon set for broader runtime coverage once core is stable.

## Local Usage

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\run-smoke-server.ps1 -Profile foundation -Clean -TimeoutSeconds 120
```

To only prepare jars:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\build-smoke-artifacts.ps1 -Profile foundation -Clean
```

## GitHub Smoke

The manual workflow `Smoke Runtime 1.21` runs the same smoke runner in GitHub Actions. It is `workflow_dispatch` only to avoid noisy Actions history.

## Verification Banner

`Slimefun` startup prints a green DrakesCraft banner with:

- `JACKSTAR`
- `DRAKESCRAFT`
- `CHAGUI68`
- repository link
- JackStar profile link

The smoke fails if these markers are missing. This confirms the server loaded a current Drake artifact rather than an old jar.

## Success Criteria

- Paper reaches `Done`.
- No jar load errors.
- No `Error occurred while enabling`.
- No `NoClassDefFoundError` or `ClassNotFoundException`.
- Banner markers are present.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: local runtime smoke base and manual `Smoke Runtime 1.21` workflow are available.
<!-- DRAKES-STATUS:END -->
