# Home - Drakes Slimefun Labs

> Synced to the current operational state of branch `1.21-latin`.

## Executive snapshot

- Active baseline: `Paper 1.21.1`, `Java 21`
- Main CI (`CI Monorepo 1.21` in Actions): green
- Maven Dependency Submission: green
- Current focus: CI stability plus gradual migration of legacy modules

## What this means

- The primary pipeline is healthy and safe for iteration.
- Some addons still require runtime validation and hardening.
- A green pipeline does not mean every module is production ready.

## Quick navigation

- **Root README** (generated full module table): [`README.md`](../../README.md)
- **Audited plugin matrix** (86 rows; do not hand-edit): [`docs/es/PLUGIN_MATRIX.md`](../es/PLUGIN_MATRIX.md) (Spanish column headers; canonical data for the monorepo)
- **Org GitHub Project board**: [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1)
- **How to sync the board with the matrix**: [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md)
- Checklist: `docs/en/migration-checklist.md`
- Pending modules: `docs/en/pending-modules.md`
- Roadmap: `docs/en/stabilization-roadmap.md`
- CI/Release strategy: `docs/en/release-and-ci-strategy.md`
- Technical reference: `docs/en/technical-reference-paper-1.21.1.md`

Refresh README + matrix: `python scripts/generate_plugin_matrix.py`

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> Main CI on `1.21-latin`: **CI Monorepo 1.21** green (curated jobs in `ci-monorepo-121.yml`).
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
