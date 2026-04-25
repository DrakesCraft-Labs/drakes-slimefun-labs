<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

## Changes
<!-- Please list all the changes you have made. -->

## Related Issues
<!-- Please tag any Issues related to your Pull Request -->
<!-- Syntax: "Resolves #000" -->

## Checklist
<!-- Here is a little checklist you should follow. -->
<!-- You can click those check boxes after you posted your issue. -->
- [ ] I have fully tested the proposed changes and promise that they will not break world generation, mob spawning, and the like.
- [ ] I followed the existing code standards and didn't mess up the formatting.
- [ ] I did my best to add documentation to any public classes or methods I added.
- [ ] I have added `Nonnull` and `Nullable` annotations to my methods to indicate their behaviour for null values

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
