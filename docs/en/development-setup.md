# Development Setup

## Goal

This project is not a standalone plugin. It is a unified Maven lab. The environment should be prepared for per-module work rather than full-reactor builds.

## Requirements

- `Java 21`
- `Maven 3.9+`
- `Git`
- Paper or Purpur `1.21.11` environment for smoke tests

## Working Branch

The reference working branch is:

```powershell
git checkout 1.21-latin
git pull origin 1.21-latin
```

## Make sure you are in the correct repo root

Before compiling, it is worth confirming the command runs from the repository root:

```powershell
git rev-parse --show-toplevel
```

If Maven is run from a subfolder, it may silently drop reactor modules and produce misleading errors.

## Recommended Build Pattern

Do not build the whole reactor unless it is strictly necessary.

Use:

```powershell
mvn -pl path/to/module -am -DskipTests package
```

Examples:

```powershell
mvn -pl sources/repos-to-port/SimpleUtils -am -DskipTests package
mvn -pl sources/repos-to-port/DynaTech -am -DskipTests package
mvn -pl sources/community-addons/MapJammers -am -DskipTests compile
```

## What the flags mean

- `-pl`: selects the module you want to work on
- `-am`: also builds its reactor dependencies
- `-DskipTests`: speeds up validation loops

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
