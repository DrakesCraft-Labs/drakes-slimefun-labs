# Inicio - Drakes Slimefun Labs

> Estado sincronizado al cierre operativo actual de la rama `1.21-latin`.

## Resumen ejecutivo

- Baseline tecnico vigente: `Paper 1.21.1`, `Java 21`
- CI principal (`CI Monorepo 1.21` en Actions): en verde
- Dependency Submission Maven: en verde
- Enfoque del momento: estabilidad CI + migracion gradual de modulos legacy

## Que significa esto

- El pipeline principal esta sano y permite iterar con seguridad.
- Aun existen addons que necesitan validacion de runtime y hardening.
- El estado "verde" no equivale a "todo el monorepo listo para produccion".

## Navegacion rapida

- **README raiz** (tabla de todos los modulos generada): [`README.md`](../../README.md)
- **Matriz auditada** (86 filas, no editar a mano): [`docs/es/PLUGIN_MATRIX.md`](PLUGIN_MATRIX.md)
- **Tablero GitHub org**: [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1)
- **Sincronizar tablero con la matriz**: [`docs/PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md)
- Checklist: `docs/es/migration-checklist.md`
- Pendientes: `docs/es/pending-modules.md`
- Roadmap: `docs/es/stabilization-roadmap.md`
- Estrategia CI/Release: `docs/es/release-and-ci-strategy.md`
- Referencia tecnica: `docs/es/technical-reference-paper-1.21.1.md`

Regenerar README + matriz: `python scripts/generate_plugin_matrix.py`

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** en verde (subconjuntos en `ci-monorepo-121.yml`).
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
