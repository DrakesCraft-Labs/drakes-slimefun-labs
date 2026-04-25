<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

## Description
<!-- Please explain why you are making this pull request. -->
<!-- Start writing below this line -->

## Proposed changes
<!-- Please explain what changes you have made to the code. -->
<!-- Start writing below this line -->

## Related Issues (if applicable)
<!-- Please tag any Issues related to your Pull Request -->
<!-- Syntax: "Resolves #000" -->
<!-- Start writing below this line -->

## Checklist
<!-- Here is a little checklist you can follow. -->
<!-- Click on these checkboxes after you created the pull request. -->
<!-- Don't worry, these are not requirements. They only serve as guidance. -->
- [ ] I have fully tested the proposed changes and promise that they will not break everything into chaos.
- [ ] I have also tested the proposed changes in combination with various popular addons and can confirm my changes do not break them.
- [ ] I followed the existing code standards and didn't mess up the formatting.
- [ ] I did my best to add documentation to any public classes or methods I added.
- [ ] I have added `Nonnull` and `Nullable` annotations to my methods to indicate their behaviour for null values
- [ ] I added sufficient Unit Tests to cover my code.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
