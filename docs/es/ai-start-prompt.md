# AI-Start-Prompt

Usar este resumen como punto de partida cuando se abre una sesión nueva y hace falta recuperar contexto rápido sin releer todo el repo.

## Contexto corto

- rama: `1.21-latin`
- stack: `Paper 1.21.1`, `Java 21`, ecosistema Slimefun (ver `pom.xml` raiz)
- inventario auditado: **86** entradas en reactor (ver `README.md` + `docs/es/PLUGIN_MATRIX.md`)
- CI: **CI Monorepo 1.21** cubre los 86 modulos: reactor Maven completo + 5 Gradle en `ci-monorepo-121.yml`

## Reglas clave

- usar el CI completo como gate principal; compilar todo localmente solo antes de cambios grandes
- usar `mvn -pl <ruta> -am -DskipTests package`
- antes de tocar código, distinguir fallo de `pom.xml` contra fallo de API
- si se actualiza un estado, ejecutar `python scripts/generate_plugin_matrix.py` y alinear el [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) (`docs/PROJECT_BOARD_SYNC.md`)

## Próximo enfoque recomendado

1. mantener `maven_full_reactor` y `gradle_green` verdes
2. ejecutar smoke tests de runtime en addons sensibles
3. reducir deuda de bridges/compatibilidad donde se repita entre addons

## Referencias

- [[Checklist de Migración]]
- [[Módulos Pendientes]]
- [[Tomorrow-Handoff]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
