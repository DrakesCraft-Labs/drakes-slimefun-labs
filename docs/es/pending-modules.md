# Modulos pendientes

Este documento refleja el estado real despues de la estabilizacion CI actual.

## Inventario unico (fuente de verdad)

- Tabla **por modulo** con estado (Listo CI / Listo local / En curso / Bloqueado) y observaciones: [`PLUGIN_MATRIX.md`](PLUGIN_MATRIX.md) (generada; ejecutar `python scripts/generate_plugin_matrix.py`).
- Misma tabla incrustada en el [`README.md`](../../README.md) raiz.
- Tablero de organizacion: [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — ver [`PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md) para alinear tarjetas con la matriz.

## Estado actual

- **CI Monorepo 1.21** (`ci-monorepo-121.yml`): estable en verde en `1.21-latin` para los subconjuntos Maven/Gradle curados
- Pendiente principal: ampliar cobertura de modulos fuera del subset estable
- Trabajo requerido: reingreso por lotes + smoke tests por addon sensible

## Auditoria tecnica reciente (build por lotes)

Fecha del corte: `2026-04-24`.

### Resumen de conteos (evidencia local en esta fecha)

| Herramienta | OK | FAIL | Notas |
|-------------|---:|-----:|-------|
| Maven (`-pl` LiteXpansion, Supreme, TranscEndence `-am compile`) | 3 modulos objetivo + reactor | 0 | `BUILD SUCCESS` con `mvn` portable o sistema |
| Gradle (`gradlew build -x test`, reactor raiz) | Bump + Galactifun + metaproyectos | Otros modulos Gradle fallan | Bump y Galactifun compilan; el `build` raiz sigue fallando en CustomItemGenerators / FastMachines / SlimefunTranslation segun el orden de tareas |

### Lote Maven (expansion batch-2)

- `LiteXpansion-drake`, `Supreme-drake`, `TranscEndence-drake` (y dependencias `-am`): **BUILD SUCCESS** tras regla `libraries-paperlib` en `port_paper_121.py` y metricas bStats en `LiteXpansion.java`.

### Lote Gradle auditado (reactor raiz)

- `sources:batch-2-expansion:Galactifun`: **BUILD SUCCESSFUL**
- `sources:community-addons:Bump`: **BUILD SUCCESS** en `compileJava` (abril 2026). Port principal: clase principal `JavaPlugin` + `SlimefunAddon` Drake (sin Guizhan `AbstractAddon` BusyBiscuit), `SimpleMenuBlock` nativo sobre `SlimefunItem` + `BlockMenuPreset`, `LocalizationService` sobre `MinecraftLocalization`, encantamientos/flags 1.21 (`POWER`, `SHARPNESS`, `UNBREAKING`, `HIDE_ADDITIONAL_TOOLTIP`), `GuizhanBuildsUpdater.start(...)`. El `build` raiz del monorepo puede seguir fallando en otros modulos Gradle (CIG/FastMachines/SlimefunTranslation).
- `sources:community-addons:CustomItemGenerators`: **FAIL** en Kotlin/SlimefunAddon (ver matriz); ejecutar `./gradlew :sources:community-addons:CustomItemGenerators:compileKotlin` para log actual.
- `sources:community-addons:FastMachines`: **FAIL** Kotlin (tipos BusyBiscuit vs Drake; dependencias InfinityExpansion); ver matriz.
- `sources:community-addons:SlimefunTranslation`: **FAIL** Java API amplia vs Slimefun Drake; ver matriz.

### Mitigaciones aplicadas en este bloque

1. Timeout de wrapper Gradle aumentado para estabilizar descargas en red lenta (`networkTimeout=120000`).
2. Alineacion de coordenadas internas Gradle con el namespace canonico del monorepo (`com.github.drakescraft_labs`) para resolver artefactos locales.
3. Publicacion local de artefactos base (`dough-core`, `slimefun-core`, `SefiLib`, `InfinityLib`) para destrabar resolucion de dependencias sin credenciales de GitHub Packages.
4. Ajuste de imports legacy en `Cultivation_Updated` (`slimefun4.libraries.*` -> paquetes vigentes) con evidencia de compilacion Maven en verde para el modulo.

## Porteo automatizado (parches por lotes)

Herramienta: `scripts/port_paper_121.py` (reglas textuales conservadoras para Paper 1.21.1). Incluye entre otras la regla `libraries-paperlib` (`com.github.drakescraft_labs.slimefun4.libraries.paperlib` -> `io.papermc.lib`).

```bash
python scripts/port_paper_121.py --list-rules
python scripts/port_paper_121.py --dry-run --path sources/community-addons/MiAddon
python scripts/port_paper_121.py --apply --path sources/community-addons/MiAddon --rules libraries-dough,libraries-commons
```

Siempre ejecutar antes `--dry-run`, revisar diff, y luego `--apply`. Con `--backup` se escribe un `.portbak` por archivo (no versionar esos backups).

## Bloques de trabajo sugeridos

1. Reintroducir modulos al CI en lotes pequenos.
2. Resolver incompatibilidades de dependencias legacy (repos externos, snapshots, coordinates antiguas).
3. Ejecutar smoke tests de runtime en addons con mecanicas complejas.
4. Consolidar documentacion de cierre por lote.

## Criterio de cierre

Un modulo se considera cerrado cuando:

- compila en el job correspondiente de [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml),
- no rompe el pipeline global,
- y tiene validacion minima de runtime si aplica.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24 (actualizado tras auditoria de build por lotes)**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** en verde (jobs curados).
> Nota: el monorepo completo sigue en migracion incremental por lotes; ultimo corte: Maven lote LiteXpansion/Supreme/TranscEndence en verde; Gradle Bump + Galactifun compilan; CustomItemGenerators / FastMachines / SlimefunTranslation siguen bloqueados en el reactor Gradle.
<!-- DRAKES-STATUS:END -->
