<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

---
name: Bug Report
about: Create a report to help us improve
title: "[Bug]: "
labels: bug
assignees: ''

---

### Problem Description

*Describe it here, please.*

### Environment

*Copy info about plugin from the command*
```shell
/patpat info
```
*Also, if the problem is related to the client, add info about client*
```shell
/patpat-client info
```

### Attachments
1) Attach your latest game logs from `<SERVER_FOLDER>/logs/latest.log` ← *IMPORTANT*
2) Attach some images/video with the problem ← *OPTIONAL*

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
