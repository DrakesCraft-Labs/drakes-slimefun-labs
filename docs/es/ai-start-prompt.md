# AI-Start-Prompt

Usar este resumen como punto de partida cuando se abre una sesión nueva y hace falta recuperar contexto rápido sin releer todo el repo.

## Contexto corto

- rama: `1.21-latin`
- stack: `Paper 1.21.11`, `Java 21`, `Slimefun 6`, `dough-core 1.3.1-DRAKE`
- reactor activo: `59` módulos
- listos en reactor: `57`
- fallos activos: `2`
- fuera del reactor: `30`

## Reglas clave

- no compilar todo el reactor salvo necesidad estricta
- usar `mvn -pl <ruta> -am -DskipTests package`
- antes de tocar código, distinguir fallo de `pom.xml` contra fallo de API
- si se actualiza un estado, sincronizar `README.md` y wiki

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
