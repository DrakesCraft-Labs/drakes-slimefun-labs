# Fuentes canónicas y saneamiento del monorepo

Actualizado: 2026-08-30

## Regla operativa

Los repositorios individuales de DrakesCraft son la fuente canónica de los
plugins que ya fueron extraídos. Las copias ubicadas en
`sources/repos-to-port/` permanecen sólo como compatibilidad de compilación e
historial: no se deben usar para iniciar cambios nuevos ni para publicar JARs.

El reactor Maven todavía los enumera porque retirarlos sin migrar todas sus
dependencias internas haría que compilaciones históricas dejaran de resolver
artefactos. Esta política evita esa ruptura sin volver a crear dos fuentes de
verdad.

## Plugins extraídos: repositorio individual canónico

| Copia de compatibilidad en este monorepo | Repositorio canónico |
| --- | --- |
| `ChestTerminal-sf5` | `ChestTerminal-drake` |
| `ColoredEnderChests` | `ColoredEnderChests-drake` |
| `DynaTech` | `DynaTech-drake` |
| `ElectricSpawners` | `ElectricSpawners-drake` |
| `ExtraGear` | `ExtraGear-drake` |
| `ExtraHeads` | `ExtraHeads-drake` |
| `GlobalWarming` | `GlobalWarming` |
| `HardcoreSlimefun` | `HardcoreSlimefun` |
| `HotbarPets` | `HotbarPets-drake` |
| `KinematicCore` | `KinematicCore-drake` |
| `luckyblocks-sf` | `luckyblocks-sf-drake` |
| `MobCapturer` | `MobCapturer-drake` |
| `PrivateStorage` | `PrivateStorage` |
| `SensibleToolbox-sf5` | `SensibleToolbox-drake` |
| `SimpleUtils` | `SimpleUtils-drake` |
| `SlimeChem` | `SlimeChem-drake` |
| `SlimefunOreChunks` | `SlimefunOreChunks` |
| `SlimyRepair` | `SlimyRepair-drake` |
| `SlimyTreeTaps` | `SlimyTreeTaps-drake` |

Las diferencias de árbol entre cada copia y su repositorio canónico son
esperadas: incluyen migraciones de build, CI, documentación y parches ya
publicados. No se deben sincronizar mediante copias masivas.

## Plugins que siguen siendo canónicos del monorepo

| Módulo | Versión publicable declarada |
| --- | --- |
| `DyedBackpacks` | `1.20.6-Drake-SNAPSHOT` |
| `EcoPower` | `1.20.6-Drake-SNAPSHOT` |
| `ExoticGarden` | `1.3` |
| `ExtraUtils` | `1.20.6-Drake-SNAPSHOT` |
| `FluffyMachines` | `1.0.0` |
| `InfinityExpansion` | `1.20.6-Drake-SNAPSHOT` |
| `SFCalc` | `1.21-Drake-v1` |
| `SFMobDrops` | `1.20.6-Drake-SNAPSHOT` |
| `SoulJars` | `1.21-Drake-v1` |
| `SoundMuffler` | `11.0-Drake` |

## Revisión de cambios de Chagui

La serie reciente integrada en `main` modifica únicamente:

* `ExoticGarden`: commits `e4db9428` a `a7eece05`. La lógica fue revisada y
  corregida después en `91c13401`; su versión de publicación queda en `1.3`.
* `FluffyMachines`: commit `8b3e3703`. No posee repositorio individual local,
  por lo que el monorepo sigue siendo su fuente canónica.

No se hallaron modificaciones recientes de Chagui dentro de los 19 módulos
extraídos que deban trasladarse a un repositorio individual. Cualquier cambio
futuro en ellos se hace en el repositorio de la segunda columna y se integra al
monorepo sólo mediante una migración explícita y revisada.
