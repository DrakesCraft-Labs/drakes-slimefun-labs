# Tomorrow Handoff

## Current State

- Main working branch: `1.21-latin`
- Target stack: `Paper 1.21.11`, `Java 21`, `Slimefun 6`, `dough-core 1.3.1-DRAKE`
- Active reactor: `59` modules
- Ready in reactor: `57`
- Active failures in reactor: `2`
- Outside the reactor: `30`
- Real operational backlog: `32 addons`

## Last Confirmed Status

The human source of truth is `README_EN.md`.

That file already documents:

- which addons are ready
- which ones are still failing inside the reactor
- which ones are still outside the reactor
- plugin-specific notes where they matter

## Active Blockers

### `GeneticChickengineering-Reborn`

- still integrated into the reactor, but not ready
- current main blocker: `pom.xml` and missing dependencies
- review `lombok` and `bstats` first

### `PotionExpansion`

- still integrated into the reactor, but not ready
- current main blocker: old API usage
- review `SlimefunItemStack.item()`
- review old `CustomItemStack.create(...)` calls

## Recommended Next Route

1. close `GeneticChickengineering-Reborn`
2. close `PotionExpansion`
3. go back to quick wins outside the reactor:
   - `MoreResearches`
   - `SfBetterChests`
   - `SlimeHUD`
   - `SmallSpace`
   - `Quaptics`

## Operational Reminders

- do not build the entire reactor unless strictly needed
- use isolated builds with `-pl` and `-am`
- if an addon changes state, sync `README.md`, `README_EN.md`, and the wiki
- do not count historical variants as ready if the active target is a different variant
- do not commit `build_status.log` unless explicitly requested

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
