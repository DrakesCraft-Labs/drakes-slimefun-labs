# Log de Porting - Minecraft 1.21.1

## Estado Actual
- **Versión de Minecraft**: 1.21.1
- **Versión de Java**: 21
- **Progreso**: 98.8% del Reactor Maven Estabilizado

## Hitos Recientes

### [2026-04-24] - La Gran Purificación de Namespaces
- **Unificación MASTER_MAPPING**: Se ha implementado un sistema de mapeo global en el `manager.py` para sincronizar todos los componentes del laboratorio.
- **Transición a `dev.drake`**: El código fuente de 262 archivos ha sido migrado de namespaces largos e inconsistentes a la nueva identidad corporativa:
  - `io.github.bakedlibs.dough` -> `dev.drake.dough`
  - `io.github.mooy1.infinitylib` -> `dev.drake.infinitylib`
  - `dev.sefiraat.sefilib` -> `dev.drake.sefilib`
- **Sincronización de Sombras (Shades)**: Corregidas las reglas de reubicación en 18 módulos para coincidir con los nuevos namespaces del código fuente.
- **Reparación de Cultivation**: Se resolvieron los fallos de compilación masivos mediante la corrección de ArtifactIds de Dough y el rebranding de imports.

## Bloqueadores
- **Node.js 20 Deprecation**: GitHub Actions muestra advertencias. Planificada migración a Node.js 24 mediante `FORCE_JAVASCRIPT_ACTIONS_TO_NODE24=true`.
- **Gradle Validation**: Pendiente de validar los 5 módulos Gradle tras la estabilización de Maven.

## Próximos Pasos
1. Monitorizar el build `b462a6bd`.
2. Verificar el sombreado final en los JARs resultantes.
3. Iniciar fase de pruebas en entorno real (Server 1.21.1).

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
