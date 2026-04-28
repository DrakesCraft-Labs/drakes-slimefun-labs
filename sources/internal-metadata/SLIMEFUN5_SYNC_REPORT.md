# Sync Slimefun5 -> Drake (sin migrar runtime)

Generado: **2026-04-28 12:36 UTC**

Este reporte compara codigo `src/main` y `src/test` entre el ultimo upstream de Slimefun5 y nuestros modulos Drake.

| Repo | Modulo local | Commit upstream | Fecha | Archivos distintos (aprox) |
| --- | --- | --- | --- | --- |
| `DynaTech` | `sources/repos-to-port/DynaTech` | `fbb7213` | `2026-04-27` | `107` |
| `InfinityExpansion` | `sources/repos-to-port/InfinityExpansion` | `07fa226` | `2026-04-27` | `55` |
| `FluffyMachines` | `sources/repos-to-port/FluffyMachines` | `1957da4` | `2026-04-27` | `52` |
| `LiteXpansion` | `sources/batch-2-expansion/LiteXpansion` | `0bd665a` | `2026-04-27` | `42` |
| `MissileWarfare` | `sources/community-addons/MissileWarfare` | `205dae3` | `2026-04-27` | `22` |
| `ExoticGarden` | `sources/repos-to-port/ExoticGarden` | `cc21e1b` | `2026-04-27` | `38` |
| `ExtraGear` | `sources/repos-to-port/ExtraGear` | `6cc1fee` | `2026-04-27` | `3` |
| `SlimefunAdvancements` | `sources/community-addons/SlimefunAdvancements` | `8ef4b8f` | `2026-04-27` | `55` |
| `SlimeTinker` | `sources/batch-2-expansion/SlimeTinker` | `414d7d9` | `2026-04-27` | `72` |
| `ChestTerminal` | `sources/repos-to-port/ChestTerminal-sf5` | `10cee27` | `2026-04-27` | `0` |
| `LuckyBlocks` | `sources/repos-to-port/LuckyBlocks-sf5` | `0af5d44` | `2026-04-27` | `0` |
| `SensibleToolbox` | `sources/repos-to-port/SensibleToolbox-sf5` | `0696fa4` | `2026-04-27` | `0` |
| `Galactifun` | `sources/repos-to-port/Galactifun-sf5` | `46fd0a0` | `2026-04-27` | `0` |

## Notas
- Este flujo **no** migra al stack Slimefun5; solo absorbe cambios de codigo sobre la base Drake.
- Dependencias Drake (`slimefun-core`, `dough-core`, autoupdate) se mantienen en nuestros `pom.xml`.
- Para plugins con alta diferencia, aplicar integracion por lotes y compilar por modulo.
- Estado de build del lote 2026-04-28:
  - `DynaTech`: compilacion OK en reactor.
  - `ChestTerminal-sf5`: compilacion OK.
  - `SensibleToolbox-sf5`: compilacion OK con `-Dmaven.test.skip=true` (tests heredados usan MockBukkit legacy).
