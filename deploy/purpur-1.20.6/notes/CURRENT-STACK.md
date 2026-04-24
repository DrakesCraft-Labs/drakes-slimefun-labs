# Current Stack

Estado operativo actual del paquete `Purpur 1.20.6`.

## Server Core

- `purpur-1.20.6-2233.jar`

## Plugins Confirmados Base

Ubicacion: `deploy/purpur-1.20.6/plugins-confirmed/server-base`

- `LuckPerms`
- `Vault`
- `EssentialsX`
- `PlaceholderAPI`
- `TAB`
- `DeluxeMenus`
- `ProtectionStones`
- `WorldGuard`
- `CoreProtect`
- `FastAsyncWorldEdit`
- `Slimefun v4.9-Drake-1.20.6`

## Slimefun Addons Confirmados

### Upstream Ready

Ubicacion: `deploy/purpur-1.20.6/plugins-confirmed/slimefun-upstream-ready`

- `AdvancedTech`
- `Cultivation`
- `ExtraHeads`
- `Galactifun`
- `HardcoreSlimefun`
- `LiteXpansion`
- `MobCapturer`
- `Networks`

### Ported

Ubicacion: `deploy/purpur-1.20.6/plugins-confirmed/slimefun-ported`

- `ColoredEnderChests`
- `DyedBackpacks`
- `DynaTech`
- `EcoPower`
- `ElectricSpawners`
- `ExtraGear`
- `ExtraUtils`
- `HotbarPets`
- `InfinityExpansion`
- `PrivateStorage`
- `SFMobDrops`
- `SlimefunOreChunks`
- `SoulJars`

## Plugins en Pruebas (Test Environment)

Ubicacion: `deploy/purpur-1.20.6/plugins-testing`

Aquí se colocan los plugins u addons que no están garantizados para el survival principal y están bajo evaluación o debug en el build lab.

## Nota de Seguridad y Operacion

- Todo jar debe seguir la `NAMING-CONVENTION.md`.
- `InfinityExpansion` incluye parche local para el dupe de storages del issue `#126`.
- `SlimefunWarfare` y `EMCTech` quedan estrictamente prohibidos y no deben colocarse ni siquiera en la carpeta confirmed. Ver `addons-rechazados.md`.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
