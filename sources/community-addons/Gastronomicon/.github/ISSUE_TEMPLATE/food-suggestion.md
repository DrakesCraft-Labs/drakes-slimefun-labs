<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

---
name: Food Suggestion
about: Suggest a new food for Gastronomicon
title: ''
labels: suggestion
assignees: ''

---

## Details
<!-- Stuff like name, effects, etc... -->
<!-- Only the name is required -->

## Recipe (optional)
<!-- 
Some tips for the recipe:
- Don't focus too much on making it realistic, just the list of ingredients and tools is usually good enough
- On the other hand, don't be afraid to also suggest an intermediate product (like tapioca pearls for the boba tea recipe) or a completely new plant/resource if it seems appropriate
-->

## Any other comments (optional)

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
