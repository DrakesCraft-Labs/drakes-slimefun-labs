# Drakes Slimefun Labs

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper Baseline](https://img.shields.io/badge/Paper-1.21.1-blue?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![Slimefun Core](https://img.shields.io/badge/Slimefun-11.0--Drake--1.21.1-green?style=for-the-badge)](https://github.com/Slimefun/Slimefun4)
[![License](https://img.shields.io/badge/License-GPL%20v3-red?style=for-the-badge)](LICENSE)

Repositorio de integracion y migracion del ecosistema Slimefun para el stack Drake.

## Estado real actual

- Rama operativa: `1.21-latin`
- Baseline tecnico: `Paper 1.21.1` + `Java 21`
- Identidad unificada: `com.github.drakescraft_labs`
- CI principal (Gates 1-5): en verde
- Dependency Submission Maven: en verde
- Cobertura total del monorepo: en migracion progresiva (no 100 por ciento funcional en runtime para todos los addons)

> Importante: este repo ya esta estabilizado a nivel CI para la rama principal de trabajo, pero todavia conserva modulos legacy que requieren hardening o smoke test adicional.

## Documentacion principal

- Portada ES: `docs/es/home.md`
- Portada EN: `docs/en/home.md`
- Checklist ES: `docs/es/migration-checklist.md`
- Pendientes ES: `docs/es/pending-modules.md`
- Estrategia CI/Release EN: `docs/en/release-and-ci-strategy.md`
- Referencia tecnica 1.21.1 ES: `docs/es/technical-reference-paper-1.21.1.md`

## Notas operativas

- El objetivo visible de la rama actual es estabilidad incremental en CI + consolidacion de dependencias.
- No asumir que cualquier addon fuera de gates activos este listo para produccion sin smoke test.
- Si quieres cobertura total, usa una estrategia de reingreso por lotes pequenos (batch hardening).

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
