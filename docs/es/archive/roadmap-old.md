# Roadmap

## Objetivo

Completar una libreria util de plugins para `Purpur 1.20.6` que permita montar un servidor nuevo sin volver a investigar desde cero.

## Ya Cubierto

- `Purpur 1.20.6`
- `Slimefun 1.20.6` portado
- base de addons `Slimefun` clasificada en:
  - listos sin modificar
  - portados localmente
  - en construccion
- base general actual con:
  - `LuckPerms`, `Vault`, `EssentialsX`, `PlaceholderAPI`, `TAB`, `DeluxeMenus`, `ProtectionStones`, `WorldGuard`, `CoreProtect`, `FastAsyncWorldEdit`
- Directorios para configuraciones (`configs-base`)
- Test Environment (`plugins-testing`)
- Documentacion de addons prohibidos (`addons-rechazados.md`) y convención de nombres (`NAMING-CONVENTION.md`)

## Falta Consolidar

- decidir la lista exacta de plugins base que quedaran como stack oficial de `Drakes`
- completar la libreria fuera de `Slimefun`
- clasificar plugins por:
  - seguros para publico
  - utiles pero delicados
- preparar packs de configuracion base:
  - tablist
  - scoreboard
  - menas y drops
  - protecciones
  - menus
  - permisos

## Pendientes Tecnicos

- seguir portando addons medianos y pesados de `Slimefun`
- revisar exploits adicionales en addons complejos
- probar en runtime real los jars portados mas sensibles

## Pendientes Operativos

- probar la estabilidad de las configuraciones base en runtime

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** en verde (jobs curados).
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
