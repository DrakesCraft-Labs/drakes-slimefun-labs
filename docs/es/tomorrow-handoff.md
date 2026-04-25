# 🏛️ Tomorrow-Handoff (v16.0)

## Estado operativo (abril 2026)

- **Rama**: `1.21-latin` | **Identidad**: `com.github.drakescraft_labs`
- **Inventario auditado**: ver [`README.md`](../../README.md) y [`PLUGIN_MATRIX.md`](PLUGIN_MATRIX.md) — **86 filas** (81 Maven en `pom.xml` + 5 entradas Gradle del reactor; regenerar con `python scripts/generate_plugin_matrix.py`).
- **CI**: **CI Monorepo 1.21** verde sobre **subconjuntos** curados (`ci-monorepo-121.yml`); no asumir build global de los 86.
- **Gradle**: `Galactifun` y `Bump` OK en CI (`gradle_green`); tres addons comunitarios siguen **bloqueados** de compilacion en el reactor completo (detalle en la matriz y en `pending-modules.md`).
- **Tablero org**: [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — alinear con [`PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md).

## 🛰️ Infraestructura CI/CD
- **Workflow unificado**: [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml) valida lotes Maven y el subconjunto Gradle en verde (`gradle_green`).
- **GitHub Packages**: Registro purgado de basura antigua. Solo queda el stack premium.

## 🐍 DrakesLab Manager
- Usar siempre `python scripts/manager.py audit` para ver el estado real antes de empezar.
- Las metricas de modulo/estado del README se regeneran con `python scripts/generate_plugin_matrix.py` (tabla + `PLUGIN_MATRIX.md`).
- **REPARACIÓN MAESTRA**: `python scripts/manager.py repair` ahora fuerza la versión `11-SNAPSHOT` del padre y reconstruye bloques dañados.
- El comando `python scripts/manager.py` (sin flags) repara identidades XML y versiones de padre.

## 🧬 Estabilización de Dependencias (Fase 2)
- **Dough-Core**: Se detectó que varios addons (`ExtraUtils`, `LiteXpansion`) usaban paquetes de Drake pero no tenían la dependencia explícita. Corregido.
- **Lombok**: `InfinityExpansion` fallaba por falta de anotaciones. Se inyectó Lombok v1.18.34.
- **Paper-API**: Migración masiva de 17 módulos de Spigot-API a Paper-API 1.21.1.
- **Rebranding de Sombras**: Sincronización de 19 módulos para usar el namespace `com.github.drakescraft_labs` en sus dependencias sombreadas (relocations).
- **Lección Aprendida**: Aunque las versiones se definan en el `parent`, los addons DEBEN declarar la dependencia explícitamente para que Maven las incluya en el classpath de compilación.

## 🎯 Siguiente Ruta Recomendada (Fase Quirúrgica)
1.  **Rebranding Masivo**: Aplicar el estándar `-drake` a los 20 addons que aún están en estado `STABILIZED`.
2.  **Smoke Testing**: Iniciar pruebas de carga en servidores Paper 1.21.1 reales con el stack completo.
3.  **Modernización Gradle**: Continuar con la limpieza de los addons de Gradle siguiendo el patrón de Galactifun.

## 🛠️ Comandos de Supervivencia
- **Audit**: `python scripts/manager.py audit`
- **Build Maven**: `mvn -pl sources/community-addons/AddonName -am -DskipTests package`
- **Build Gradle**: `./gradlew :sources:batch-2-expansion:Galactifun:build`

## 🔗 Enlaces Estratégicos
- [Checklist de Migración](migration-checklist.md)
- [Guía de Porteo 1.21.1](../docs/README-PORT-1.21.1.md)
- [Estrategia de Releases y CI](release-and-ci-strategy.md)

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** en verde (jobs curados).
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
