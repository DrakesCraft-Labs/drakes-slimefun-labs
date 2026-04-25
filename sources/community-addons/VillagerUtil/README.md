<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# VillagerUtil

---

**A small Slimefun4 addon that adds a few items that makes using Minecraft villagers easier**

Requires Minecraft 1.18 and [Slimefun](https://github.com/Slimefun/Slimefun4)

---

### List of items:

**Villager Token**

A consumable that is used when using the various Villager Wands

**Villager Cure Wand**

A tool that cures zombie villagers

**Villager De-nitwit-ifier**

A tool that allows nitwit-type villagers to get jobs

**Villager Trade Wand**

A tool that can cycle the trades of villagers

**Villager Transport Wand**

A tool that lets you move villagers easily

**Villager Transport Charm**

An item given to you by the Villager Transport wand which is associated with a villager

Right click on the ground somewhere with the Transport Charm to teleport the villager there

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
