# Drakes Slimefun Labs

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper Baseline](https://img.shields.io/badge/Paper-1.21.1-blue?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![Slimefun Core](https://img.shields.io/badge/Slimefun-11.0--Drake--1.21.1-green?style=for-the-badge)](https://github.com/Slimefun/Slimefun4)
[![License](https://img.shields.io/badge/License-GPL%20v3-red?style=for-the-badge)](LICENSE)

Integration and migration workspace for the Drake Slimefun ecosystem.

## Current real status

- Main working branch: `1.21-latin`
- Technical baseline: `Paper 1.21.1` + `Java 21`
- Unified identity: `com.github.drakescraft_labs`
- Main CI gates (1-5): green
- Maven Dependency Submission: green
- Full monorepo runtime readiness: still incremental (not every addon is production ready)

> Important: CI is now stable for the active branch, but legacy modules still require additional hardening and runtime smoke validation.

## Main docs

- Spanish home: `docs/es/home.md`
- English home: `docs/en/home.md`
- Spanish checklist: `docs/es/migration-checklist.md`
- Spanish pending modules: `docs/es/pending-modules.md`
- CI/release strategy: `docs/en/release-and-ci-strategy.md`
- Technical reference 1.21.1: `docs/en/technical-reference-paper-1.21.1.md`

## Operational notes

- The branch goal is incremental CI stability plus dependency consolidation.
- Do not assume modules outside active gates are production ready without smoke tests.
- For full coverage, use small-batch reintroduction and hardening.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
