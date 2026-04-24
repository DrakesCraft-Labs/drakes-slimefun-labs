# AI-Start-Prompt

Usar este resumen como punto de partida cuando se abre una sesión nueva y hace falta recuperar contexto rápido sin releer todo el repo.

## Contexto corto

- rama: `1.21-latin`
- stack: `Paper 1.21.1`, `Java 21`, ecosistema Slimefun (ver `pom.xml` raiz)
- inventario auditado: **86** entradas en reactor (ver `README.md` + `docs/es/PLUGIN_MATRIX.md`)
- CI: Gates 1–5 en verde sobre subconjuntos curados (no prueba de build de los 86 en un solo job)

## Reglas clave

- no compilar todo el reactor salvo necesidad estricta
- usar `mvn -pl <ruta> -am -DskipTests package`
- antes de tocar código, distinguir fallo de `pom.xml` contra fallo de API
- si se actualiza un estado, ejecutar `python scripts/generate_plugin_matrix.py` y alinear el [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) (`docs/PROJECT_BOARD_SYNC.md`)

## Próximo enfoque recomendado

1. cerrar los fallos activos del reactor
2. continuar con quick wins fuera del reactor
3. dejar variantes históricas y casos pesados para triage dedicado

## Referencias

- [[Checklist de Migración]]
- [[Módulos Pendientes]]
- [[Tomorrow-Handoff]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
