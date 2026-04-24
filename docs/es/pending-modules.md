# Modulos pendientes

Este documento refleja el estado real despues de la estabilizacion CI actual.

## Estado actual

- Gates 1-5: estables en verde
- Pendiente principal: ampliar cobertura de modulos fuera del subset estable
- Trabajo requerido: reingreso por lotes + smoke tests por addon sensible

## Auditoria tecnica reciente (build por lotes)

Fecha del corte: `2026-04-24`.

### Lote Gradle auditado (reactor raiz)

- `sources:batch-2-expansion:Galactifun`: **BUILD SUCCESSFUL**
- `sources:community-addons:Bump`: **FAIL** (128 errores de compilacion; mezcla de APIs legacy y simbolos removidos en 1.21.1)
- `sources:community-addons:CustomItemGenerators`: **FAIL** (errores Kotlin, referencias no resueltas como `logger`)
- `sources:community-addons:FastMachines`: **FAIL** (errores Kotlin de tipos/API de Slimefun addon)
- `sources:community-addons:SlimefunTranslation`: **FAIL** (94 errores Java; incompatibilidades API/constructores/tipos)

### Mitigaciones aplicadas en este bloque

1. Timeout de wrapper Gradle aumentado para estabilizar descargas en red lenta (`networkTimeout=120000`).
2. Alineacion de coordenadas internas Gradle con el namespace canonico del monorepo (`com.github.drakescraft_labs`) para resolver artefactos locales.
3. Publicacion local de artefactos base (`dough-core`, `slimefun-core`, `SefiLib`, `InfinityLib`) para destrabar resolucion de dependencias sin credenciales de GitHub Packages.
4. Ajuste de imports legacy en `Cultivation_Updated` (`slimefun4.libraries.*` -> paquetes vigentes) con evidencia de compilacion Maven en verde para el modulo.

## Porteo automatizado (parches por lotes)

Herramienta: `scripts/port_paper_121.py` (reglas textuales conservadoras para Paper 1.21.1).

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

- compila dentro del gate correspondiente,
- no rompe el pipeline global,
- y tiene validacion minima de runtime si aplica.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24 (actualizado tras auditoria de build por lotes)**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes; ultimo lote Gradle validado con 1 modulo en verde y 4 modulos con fallos de migracion API/codigo.
<!-- DRAKES-STATUS:END -->
