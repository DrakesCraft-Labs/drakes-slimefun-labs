# Stabilization Roadmap

## Goal

This page translates the `README_EN.md` inventory into an operational closure sequence. The goal is not only to "make it compile", but to close the real backlog without mixing old variants, bloating the reactor, or leaving ambiguous states behind.

## Base State

- Workspace universe: `87 addons + 2 base modules`
- Active modules in reactor: `59`
- Ready modules in reactor: `57`
- Active modules with confirmed failure: `2`
- Addons outside the reactor: `30`
- Real operational backlog: `32 addons`

## Recommended Order

### Phase 1: close what is already inside the reactor

This is highest priority because any failure here affects the unified build.

1. `GeneticChickengineering-Reborn`
2. `PotionExpansion`

### Phase 2: integrate quick wins outside the reactor

These are the most reasonable targets for continued fast closures:

1. `MoreResearches`
2. `SfBetterChests`
3. `SlimeHUD`
4. `SmallSpace`
5. `Quaptics`

### Phase 3: mid-tier candidates

These require more review, but still make more sense than the heavy or historical cases.

1. `Geyser-Slimefun-Heads`
2. `Gastronomicon`
3. `RelicsOfCthonia`
4. `VillagerTrade`
5. `Wildernether`
6. `WorldEditSlimefun`
7. `CompressionCraft`
8. `EMCTech`
9. `SaneCrafting`
10. `SpiritsUnchained`
11. `Better-Nuclear-Generator`
12. `AdvancedTech`

### Phase 4: triage first, port second

Do not enter these blindly without reviewing structure, build system, and external dependencies first.

- `Galactifun`
- `Bump`
- `CustomItemGenerators`
- `FastMachines`
- `SlimefunTranslation`
- `UltimateGenerators2`
- `Netheopoiesis`
- `SlimeFrame`
- `SlimefunAdvancements`

### Phase 5: historical or ambiguous variants

These should not be mixed with active variants without an explicit decision.

- `Cultivation`
- `Networks`
- `EMC2`
- `SlimefunWarfare`

## Per-Addon Working Criteria

Before touching code:

1. confirm whether the problem is `pom.xml`/dependency-related or API/code-related
2. check whether it already uses `dev.drake.dough.*`
3. confirm whether it inherits from the reactor parent
4. compile only that module with `-pl` and `-am`

Base command:

```powershell
mvn -pl path/to/module -am -DskipTests package
```

## Ready Criteria

An addon can be marked as ready for `1.21.11` when:

- it builds consistently either inside the reactor or through isolated module build
- it no longer depends on old coordinates such as `dev.drake:Slimefun:5.0-Drake-1.21.11`
- it no longer has known active API failures
- its status is reflected in `README_EN.md` and tracking pages

## Final Project Closure

The project is not considered closed only because it reaches `BUILD SUCCESS`.

At the end of the backlog, the team should still do:

1. smoke testing on Paper/Purpur `1.21.11`
2. basic runtime validation for sensitive addons
3. cleanup of temporary untracked logs
4. final review of the `README` and wiki inventory

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
