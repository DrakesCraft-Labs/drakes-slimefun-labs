# Modulos pendientes

Este documento refleja el estado real despues de la estabilizacion CI actual.

## Estado actual

- Gates 1-5: estables en verde
- Pendiente principal: ampliar cobertura de modulos fuera del subset estable
- Trabajo requerido: reingreso por lotes + smoke tests por addon sensible

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
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
