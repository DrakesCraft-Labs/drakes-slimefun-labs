# 🏛️ Tomorrow-Handoff (v16.0)

## Estado operativo (abril 2026)

- **Rama**: `1.21-latin` | **Identidad**: `com.github.drakescraft_labs`
- **Inventario auditado**: ver [`README.md`](../../README.md) y [`PLUGIN_MATRIX.md`](PLUGIN_MATRIX.md) — **86 filas** (81 Maven en `pom.xml` + 5 entradas Gradle del reactor; regenerar con `python scripts/generate_plugin_matrix.py`).
- **CI**: **CI Monorepo 1.21** cubre los **86**: reactor Maven completo (`maven_full_reactor`) + 5 Gradle (`gradle_green`).
- **Build local completo**: 81 modulos Maven + 5 proyectos Gradle compilan en el corte `2026-04-24`.
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
- **Bridges de compatibilidad**: Slimefun core expone shims `io.github.thebusybiscuit.slimefun4.*`; `FastMachines` y `UltimateGenerators2` usan bridges locales `MenuBlock`/`TickingMenuBlock`; `FastMachines` agrega `DrakeItemBuilderCompat`.
- **Lección Aprendida**: Aunque las versiones se definan en el `parent`, los addons DEBEN declarar la dependencia explícitamente para que Maven las incluya en el classpath de compilación.

## 🎯 Siguiente Ruta Recomendada (Fase Quirúrgica)
1.  **Smoke Testing**: iniciar pruebas de carga en servidores Paper 1.21.1 reales con el stack completo.
2.  **Vigilar CI completo**: mantener `maven_full_reactor` y `gradle_green` verdes antes de nuevos cierres.
3.  **Reducir deuda de bridges**: mover a utilidades compartidas los bridges que se repitan entre addons.

## 🛠️ Comandos de Supervivencia
- **Audit**: `python scripts/manager.py audit`
- **Build Maven completo**: `mvn -B -DskipTests compile -fae`
- **Build Maven puntual**: `mvn -pl sources/community-addons/AddonName -am -DskipTests package`
- **Build Gradle completo del corte**: `./gradlew :sources:batch-2-expansion:Galactifun:compileJava :sources:community-addons:Bump:compileJava :sources:community-addons:CustomItemGenerators:compileJava :sources:community-addons:FastMachines:compileJava :sources:community-addons:SlimefunTranslation:compileJava --no-daemon`
- **Artefacto local FastMachines**: `mvn -B -DskipTests install -pl sources/repos-to-port/InfinityExpansion -am`

## 🔗 Enlaces Estratégicos
- [Checklist de Migración](migration-checklist.md)
- [Guía de Porteo 1.21.1](../docs/README-PORT-1.21.1.md)
- [Estrategia de Releases y CI](release-and-ci-strategy.md)

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
