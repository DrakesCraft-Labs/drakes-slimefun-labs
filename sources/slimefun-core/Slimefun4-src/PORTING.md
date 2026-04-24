# Slimefun 1.21.11 Port Notes

This repository is an unofficial packaging and publication of a `Slimefun4` code line suitable for `Paper/Purpur 1.21.11`.

## What This Is

- A GitHub-hosted port for `Minecraft 1.21.11`
- Built for `Paper` and `Purpur`
- Intended for `Java 21`
- Published with a clearly marked local version: `5.0-Drake-1.21.11`
- Designed to stay as close as possible to upstream behavior and API shape

## What This Is Not

- Not the official `Slimefun` upstream repository
- Not an attempt to replace or supersede upstream development
- Not a blanket compatibility promise for every addon ever made
- Not a frozen archival `1.20.6` branch

## Technical Base

This port is the active Drakes core line for the mono-repo migration to `1.21.11`.

Current design choices:

- Paper API target standardized to `1.21.11-R0.1-SNAPSHOT`
- local artifact version standardized to `5.0-Drake-1.21.11`
- integration with `dev.drake.dough:dough-core`
- selective adoption of direct Paper APIs in places where older abstractions create friction

## Local Changes Made Here

The local publication is now an actively maintained `1.21.11` core line.

- Maven version changed to `5.0-Drake-1.21.11`
- parent alignment with `drakes-slimefun-labs`
- direct Paper profile-based head handling in key compatibility hotspots
- updated health attribute usages for the Paper 1.21.11 API
- test suite kept intact instead of being stripped to force a build

## Compatibility

Expected to be compatible with:

- `Paper 1.21.11`
- `Purpur 1.21.11`
- addons migrated against the Drakes reactor and `dough-core`

Potential incompatibilities still exist for:

- addons that rely on newer upstream changes after this base commit
- addons that ship their own fragile NMS hooks
- addons that assume exact upstream build metadata or updater behavior
- plugins expecting official upstream support policy

## Build Requirements

- `Java 21`
- `Maven 3.9+`

Typical build:

```powershell
mvn clean package "-Dmaven.test.skip=true"
```

## Release Artifact

The published release jar is:

- `Slimefun v5.0-Drake-1.21.11.jar`

## Support Expectations

This repository should be treated as the active Drakes compatibility-focused core for `1.21.11`, not as an official upstream support channel.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
