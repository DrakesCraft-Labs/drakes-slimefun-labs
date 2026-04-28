# Migracion Slimefun5 -> Drake Labs (1.21-latin)

Este documento registra el estado de ingestion de repos del org `Slimefun5` al monorepo Drake.

## Inventario Slimefun5 (repos de addons)

- `SlimeTinker`
- `Galactifun`
- `SensibleToolbox`
- `InfinityExpansion`
- `FluffyMachines`
- `LiteXpansion`
- `MissileWarfare`
- `ExoticGarden`
- `ChestTerminal`
- `LuckyBlocks`
- `ExtraGear`
- `SlimefunAdvancements`
- `DynaTech`

## Estado en Drake

- **Ya existentes y activos en monorepo**
  - `DynaTech` -> `sources/repos-to-port/DynaTech`
  - `InfinityExpansion` -> `sources/repos-to-port/InfinityExpansion`
  - `FluffyMachines` -> `sources/repos-to-port/FluffyMachines`
  - `LiteXpansion` -> `sources/batch-2-expansion/LiteXpansion`
  - `MissileWarfare` -> `sources/community-addons/MissileWarfare`
  - `ExoticGarden` -> `sources/repos-to-port/ExoticGarden`
  - `ExtraGear` -> `sources/repos-to-port/ExtraGear`
  - `SlimefunAdvancements` -> `sources/community-addons/SlimefunAdvancements`
  - `SlimeTinker` -> `sources/batch-2-expansion/SlimeTinker`
  - `LuckyBlocks` (legacy equivalente) -> `sources/repos-to-port/luckyblocks-sf`
  - `Galactifun` (lineas actuales) -> `sources/repos-to-port/Galactifun2` y `sources/batch-2-expansion/Galactifun`
- **Ingeridos hoy como base sf5 para port**
  - `sources/repos-to-port/ChestTerminal-sf5`
  - `sources/repos-to-port/SensibleToolbox-sf5`
  - `sources/repos-to-port/LuckyBlocks-sf5`
  - `sources/repos-to-port/Galactifun-sf5`

## Criterios "ponerle lo nuestro"

Para cada plugin importado desde sf5:

1. Pasar a parent del reactor Drake (`drakes-slimefun-labs`) o adaptar build equivalente.
2. Ajustar coordenadas:
  - `groupId` -> `com.github.drakescraft_labs`
  - `artifactId` -> `<Plugin>-drake`
3. Alinear dependencias a:
  - `paper-api` y version de `slimefun-core` de la rama `1.21-latin`.
4. Aplicar branding Drake en `plugin.yml` (author/website/descripcion cuando corresponda).
5. Integrar `drakes-labs-autoupdate` donde aplique.
6. Compilar y smoke-test de carga en servidor.

## Orden recomendado de port (riesgo bajo -> alto)

1. `LuckyBlocks-sf5` (comparar contra `luckyblocks-sf`)
2. `Galactifun-sf5` (coordinar con `Galactifun2`, no coexistir a ciegas)

## Avance aplicado en este lote (2026-04-28)

- `DynaTech`: migracion fuerte completada desde `upstream-sf5` con adaptacion Drake, compilacion OK.
- `ChestTerminal-sf5`: port de imports/API terminado, compilacion OK y modulo agregado al reactor root.
- `SensibleToolbox-sf5`: bridge/compat API terminado para stack Drake, compilacion OK (con `-Dmaven.test.skip=true`) y modulo agregado al reactor root.
- Verificacion reactor: build conjunto de `ChestTerminal-sf5` + `SensibleToolbox-sf5` en root OK (skip tests).

## Repos standalone creados en la organizacion (2026-04-28)

Se inicio la separacion gradual de addons inestables a repos individuales dentro de `DrakesCraft-Labs`:

- `https://github.com/DrakesCraft-Labs/SensibleToolbox-drake`
- `https://github.com/DrakesCraft-Labs/Galactifun2-drake`
- `https://github.com/DrakesCraft-Labs/DynaTech-drake`
- `https://github.com/DrakesCraft-Labs/Aircraft-drake`
- `https://github.com/DrakesCraft-Labs/SlimeTinker-drake`
- `https://github.com/DrakesCraft-Labs/ChestTerminal-drake`
- `https://github.com/DrakesCraft-Labs/SaneCrafting-drake`
- `https://github.com/DrakesCraft-Labs/MissileWarfare-drake`
- `https://github.com/DrakesCraft-Labs/CrystamaeHistoria-drake`

Cada repo fue bootstrappeado con rama `1.21-latin` y README explicando objetivo/valor del addon para Slimefun y el stack Drake.

## Nota de seguridad de ramas

No mezclar ni proponer merge entre lineas `1.21-latin` y `26.X-ToTheStars`.