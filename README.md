# Drakes Slimefun Labs

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.x](https://img.shields.io/badge/Paper-1.21.x-3b82f6?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![CI Monorepo 1.21](https://img.shields.io/badge/CI-Monorepo%201.21-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions/workflows/ci-monorepo-121.yml)
[![Monorepo](https://img.shields.io/badge/Monorepo-Slimefun%20Ecosystem-7c3aed?style=for-the-badge)](#inventario-completo-de-modulos-y-plugins)
[![GPLv3](https://img.shields.io/badge/License-GPLv3-ef4444?style=for-the-badge)](LICENSE)

---

## English · What this repository is

**Drakes Slimefun Labs** is an open **Maven + Gradle monorepo** (Java **21**) from [**DrakesCraft Labs**](https://github.com/DrakesCraft-Labs). It ships a **forked Slimefun 4** (“Drake” core), **Dough**, internal patches (e.g. Commons Lang), and **dozens of ported/community addons** aligned with **Paper 1.21.x**. Treat it as a **build and porting lab**: GitHub Actions compiles the full reactor; optional **smoke** runs Paper locally; **Releases** provide a **ZIP of plugin JARs** when maintainers run the workflow manually.

**Goals:** one dependency graph, reproducible CI, and a clear path from upstream-style addons → Paper 1.21 API → Slimefun shaded classpath (including **relocated Dough** types under `com.github.drakescraft_labs.slimefun4.libraries.dough.*`). **Non-goals:** promising “every recipe on every public server is QA’d”; gameplay polish still happens on real survival (see reference server below).

**Default branch:** `1.21-latin` (stable lab line). Experimental **Paper 26.x** work lives on **`26.X-ToTheStars`** — **do not merge** those two histories (see the red-line section later in this README).

### Using the reactor (commands)

| Goal | Command |
|------|---------|
| Regenerate the big module table + `docs/es/PLUGIN_MATRIX.md` | `python scripts/generate_plugin_matrix.py` |
| Compile **all** Maven modules (fail-at-end) | `mvn -B -DskipTests compile -fae` |
| Full `package` (same pressure as CI / release) | `mvn -B -DskipTests package -fae` |
| One addon + its Maven dependencies | `mvn -B -pl sources/community-addons/MyAddon -am -DskipTests package` |
| Install Drake APIs into `~/.m2` (for Gradle addons) | `mvn -B clean install -DskipTests -pl sources/dough-core,sources/slimefun-core/Slimefun4-src,sources/batch-2-expansion/SefiLib,sources/batch-2-expansion/InfinityLib,sources/repos-to-port/InfinityExpansion -am` |
| Gradle from repo root (example) | `./gradlew :sources:community-addons:SlimefunTranslation:shadowJar --no-daemon` |

**Python tooling:** full list and flags in [`scripts/README.md`](scripts/README.md) (`manager.py`, `port_paper_121.py`, `fix_dough_compilation_imports.py`, smoke orchestrator, release collector, etc.). **Smoke:** [`scripts/smoke/README.md`](scripts/smoke/README.md).

### Posting in the official Slimefun Discord (expectations)

**Technically:** CI is green for the full reactor on `1.21-latin`; releases ship a ZIP; smoke profiles exist for Paper **1.21.1** and **1.21.11**. **For the community:** present this repo as a **third-party fork / lab**, not “official Slimefun”. Link this README + **latest [GitHub Release](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/releases)**, state **Paper 1.21.x + Java 21**, and that **gameplay, balance, and cross-plugin quirks** are still validated on real servers. That avoids misleading casual server owners.

---

## Español · Qué es este repositorio

**Drakes Slimefun Labs** es un **monorepo Maven + Gradle** (Java **21**) de [**DrakesCraft Labs**](https://github.com/DrakesCraft-Labs): incluye **Slimefun 4 fork** (“Drake”), **Dough**, parches internos (p. ej. Commons Lang) y **muchos addons** portados o de comunidad, alineados a **Paper 1.21.x**. Es un **laboratorio de compilación y porte**: el **CI** valida el reactor; el **smoke** levanta Paper de prueba; las **Releases** publican un **ZIP de JARs** cuando un mantenedor dispara el workflow.

**Qué se persigue:** un solo grafo de dependencias, builds reproducibles y un camino claro desde addons estilo upstream → API Paper 1.21 → classpath de Slimefun sombreado (incluidos tipos **Dough relocados** bajo `com.github.drakescraft_labs.slimefun4.libraries.dough.*`). **Qué no prometemos:** que cada receta o interacción esté pulida en todo servidor público; eso sigue en **supervivencia real** (servidor de referencia más abajo).

**Rama por defecto:** `1.21-latin` (línea estable del laboratorio). El trabajo **Paper 26.x** va en **`26.X-ToTheStars`**; **no fusiones** esas dos historias (apartado “línea roja” más abajo).

### Cómo usar el reactor (comandos)

| Objetivo | Comando |
|----------|---------|
| Regenerar tabla larga del README + `docs/es/PLUGIN_MATRIX.md` | `python scripts/generate_plugin_matrix.py` |
| Compilar **todo** el reactor Maven (falla al final si algo rompe) | `mvn -B -DskipTests compile -fae` |
| `package` completo (misma presión que CI / release) | `mvn -B -DskipTests package -fae` |
| Un addon Maven y sus dependencias | `mvn -B -pl sources/community-addons/MiAddon -am -DskipTests package` |
| Publicar APIs Drake en `~/.m2` (addons Gradle) | `mvn -B clean install -DskipTests -pl sources/dough-core,sources/slimefun-core/Slimefun4-src,sources/batch-2-expansion/SefiLib,sources/batch-2-expansion/InfinityLib,sources/repos-to-port/InfinityExpansion -am` |
| Gradle desde la raíz (ejemplo) | `./gradlew :sources:community-addons:SlimefunTranslation:shadowJar --no-daemon` |

**Scripts Python:** catálogo y opciones en [`scripts/README.md`](scripts/README.md) (`manager.py`, `port_paper_121.py`, `fix_dough_compilation_imports.py`, orquestador de smoke, JARs de release, etc.). **Smoke:** [`scripts/smoke/README.md`](scripts/smoke/README.md).

### Scripts Python (resumen rápido)

| Script | Para qué sirve |
|--------|----------------|
| `generate_plugin_matrix.py` | Sincroniza inventario de módulos con CI y el README / matriz. |
| `manager.py` | Auditoría y reparaciones masivas de POMs / imports (usar con diff revisado). |
| `port_paper_121.py` | Parches repetibles de API Paper 1.21.1 (`--dry-run` primero). |
| `fix_dough_compilation_imports.py` | Alinea imports de `ProtectionManager` con el Slimefun sombreado. |
| `fix_graalvm_js_community_poms.py` / `ci_hygiene_fixes.py` | GraalVM “community” + pasada de higiene antes de `package`. |
| `smoke/smoke_orchestrate.py` | Orquesta descarga Paper, JARs y arranque de prueba. |
| `release/collect_monorepo_jars.py` | Usado por el workflow de release para armar el ZIP. |

Detalle, flags y ejemplos: **[`scripts/README.md`](scripts/README.md)**.

### ¿Conviene anunciarlo en el Discord de Slimefun?

**Sí, con matices.** A nivel **técnico** el repo está en buena forma para compartirlo: CI del monorepo, releases en GitHub, documentación y scripts. A nivel **comunidad**, conviene presentarlo como **fork / laboratorio de DrakesCraft**, no como “el Slimefun oficial”: aclara **Paper 1.21.x**, **Java 21**, enlace al **último release**, y que el **gameplay en survival** sigue depender de pruebas reales (p. ej. [DrakesCraft](https://drakescraft.cl)). Así no generas expectativas de producto cerrado en gente que solo busca un jar “plug and play” sin leer.

### Listo en el laboratorio (no bloquea seguir iterando)

| Pilar | Detalle breve |
|-------|----------------|
| **Build** | Todos los módulos del reactor cubiertos por [CI Monorepo 1.21](.github/workflows/ci-monorepo-121.yml) (`maven_full_reactor`, `foundation`, `gradle_green`). |
| **Smoke** | Servidor Paper **1.21.1** y **1.21.11** (perfiles `foundation`, `foundation-paper-12111`, `monorepo-all`, …); ver [`scripts/smoke/README.md`](scripts/smoke/README.md). |
| **Distribución** | [Release monorepo JARs](.github/workflows/release-monorepo-jars.yml): un **ZIP** con los plugins del reactor (manual en Actions); guía en [`docs/github-maintenance.md`](docs/github-maintenance.md). |

### Qué queda a propósito “para después”

No es deuda del reactor: es **vida real** en el juego.

- **Pasta fina addon por addon** (lore, recetas raras, choques con otros plugins): lo van encontrando **jugadores**, **Chagui** y quien cura el survival.
- **Bugs de gameplay** → [Issues](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/issues) y el tablero de la org.
- **Servidor de referencia (Chile):** **[DrakesCraft](https://drakescraft.cl)** — survival donde vive buena parte del stack; conexión típica **`play.drakescraft.cl`** / **`mc.drakescraft.cl`** (según lo que muestre el cliente al unirse). Ahí se valida el pack con carga real.

### Rama experimental **26.x** (`26.X-ToTheStars`)

**Qué es:** una línea de trabajo paralela para el salto a **Minecraft / Paper API 26.x** (artefactos tipo `26.1.x.build.*-alpha` en el repo de Paper). En esa rama el `pom.xml` raíz sigue con **`paper.version` 1.21.x por defecto** y se añade el perfil Maven **`paper-26-preview`** para compilar contra la API 26 cuando toque (`mvn -Ppaper-26-preview`). La documentación allí incluye avisos en los `.md` y la guía [`paper-26-base.md`](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/blob/26.X-ToTheStars/docs/paper-26-base.md) (solo en esa rama).

**Línea roja (no negociable):** **`1.21-latin`** (rama estable por defecto del repo; equivale a la “main” operativa del laboratorio) y **`26.X-ToTheStars`** son **líneas de historia separadas a propósito**. **Jamás las unas** con `git merge`, PR de fusión bidireccional ni rebase que mezcle ambas bases: los BOMs, `paper.version`, perfiles Maven, módulos y supuestos de API **no son intercambiables** y mezclarlos tiene **alto riesgo de romper el monorepo** (resolución de dependencias imposible, CI rojo masivo, pérdida de tiempo hasta revertir o reconstruir a mano). Si un cambio debe vivir en las dos líneas, se **replica o reescribe** de forma acotada (cherry-pick manual o parche equivalente), nunca fusionando las ramas enteras.

**Qué no es:** un reemplazo de `1.21-latin` de la noche a la mañana. Los **releases**, el **smoke** pensado para operadores y el **pack estable** siguen saliendo de **`1.21-latin`**.

**Calendencia humana:** el porte agresivo a **1.21.x** fue extremadamente exigente (“casi nos mata”). Se contempla una ventana aproximada de **un mes** antes de retomar el porte **26.x** con el mismo ritmo de sprint; en ese intervalo el foco sigue siendo **mantener verde** `1.21-latin`, Issues, smoke cuando haga falta y el survival **[DrakesCraft](https://drakescraft.cl)**. El trabajo 26.x avanza **sin presión** en `26.X-ToTheStars` hasta que el equipo vuelva a tener cabeza para API breaking.

#### ¿Por qué GitHub muestra “más de dos” ramas?

Además de **`1.21-latin`** y **`26.X-ToTheStars`**, GitHub lista ramas **`dependabot/...`**: son **ramas de PRs automáticos** (Dependabot); no son líneas de producto paralelas y se cierran al fusionar o descartar el PR. La cadencia y el agrupamiento están acotados en [`.github/dependabot.yml`](.github/dependabot.yml) (mensual, pocos PRs abiertos) para no inundar el repo; detalle en [`docs/github-maintenance.md`](docs/github-maintenance.md).

---

## Enlaces rapidos

| Recurso | URL |
|---|---|
| **Indice de documentacion** | [`docs/README.md`](docs/README.md) |
| **Smoke (perfiles, Paper, logs)** | [`scripts/smoke/README.md`](scripts/smoke/README.md) |
| **Release ZIP monorepo (Actions)** | [`.github/workflows/release-monorepo-jars.yml`](.github/workflows/release-monorepo-jars.yml) |
| **Mantenimiento GitHub** (runs, PRs, alertas) | [`docs/github-maintenance.md`](docs/github-maintenance.md) |
| **GitHub Project (estado org)** | [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) |
| **Actions (CI)** | [Workflow runs](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions) |
| **Issues** | [Issues](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/issues) |
| **Matriz detallada (generada)** | [`docs/es/PLUGIN_MATRIX.md`](docs/es/PLUGIN_MATRIX.md) |
| **Como sincronizar el tablero** | [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md) |
| **Docs ES (indice)** | [`docs/es/home.md`](docs/es/home.md) |
| **Docs EN (indice)** | [`docs/en/home.md`](docs/en/home.md) |
| **Prompt corto para IA** | [`docs/es/ai-start-prompt.md`](docs/es/ai-start-prompt.md) · [`docs/en/ai-start-prompt.md`](docs/en/ai-start-prompt.md) |
| **Servidor de referencia (Chile)** | [drakescraft.cl](https://drakescraft.cl) |

> La matriz y la tabla larga de módulos de este README se generan con `python scripts/generate_plugin_matrix.py` para evitar desalineación manual.

---

## Tablero GitHub Projects

El estado de alto nivel del porteo se gestiona en el [**Project 1 de la organizacion**](https://github.com/orgs/DrakesCraft-Labs/projects/1).

Para actualizarlo desde tu equipo con la CLI, autoriza scopes de Projects:

```bash
gh auth refresh -h github.com -s read:project,project
```

Luego alinea cada tarjeta con la columna **Estado** y las **Observaciones** de la tabla inferior (o del archivo `docs/es/PLUGIN_MATRIX.md`). Guia paso a paso: [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md).

---

## Resumen de estado (auditable)

> Corte generado automáticamente a partir de `ci-monorepo-121.yml`, `pom.xml`, `settings.gradle.kts` y el script de matriz. Los números miden **cobertura de build en CI**, no “porcentaje de perfección” del juego en survival.

| Estado | Cantidad | Significado |
|---:|---:|---|
| **Listo (CI)** | **86** | Aparece en `ci-monorepo-121.yml` (`maven_full_reactor`, `foundation` o `gradle_green`) y compila alli. |
| **Listo (local)** | **0** | `mvn compile -fae` o `gradlew <proyecto>:compileJava` verde en revision auditada; **pendiente** promover a un job de `ci-monorepo-121.yml`. |
| **En curso** | **0** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **0** | Fallo reproducible de compilacion en el reactor local. |
| **Total modulos** | **86** | Maven + Gradle en reactor; ver conteo exacto en esta fila. |

### Barra de proporcion (CI vs resto)

```text
Listo CI:     86/86  (100.0%)
Listo local:  0/86
En curso:     0/86
Bloqueado:    0/86
```

---

## Metodologia (criterios)

1. **Listo (CI)**: modulo cubierto por un job de [`ci-monorepo-121.yml`](.github/workflows/ci-monorepo-121.yml) (`foundation`, `maven_full_reactor`, `gradle_green`).
2. **Listo (local)**: compilacion Maven/Gradle exitosa en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build local documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`, bridges locales de compatibilidad para addons Gradle (`MenuBlock`, `TickingMenuBlock`, `DrakeItemBuilderCompat`) y bridges BusyBiscuit en Slimefun core (`io.github.thebusybiscuit.slimefun4.*`).

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |
|---|---|:---:|---|---|---|
| Cultivation_Updated | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/Cultivation_Updated` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| InfinityLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/InfinityLib` | Stack base Paper 1.21.1 + Java 21. |
| LiteXpansion | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/LiteXpansion` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Networks_Better_Compatibility | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/Networks_Better_Compatibility` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SMG | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/SMG` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SefiLib | libreria | Listo (CI) | CI Monorepo · foundation | `sources/batch-2-expansion/SefiLib` | Stack base Paper 1.21.1 + Java 21. |
| SlimeTinker | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/SlimeTinker` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Supreme | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/Supreme` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| TranscEndence | addon / expansion | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/batch-2-expansion/TranscEndence` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| AdvancedTech | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/AdvancedTech` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| AlchimiaVitae | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/AlchimiaVitae` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Better-Nuclear-Generator | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Better-Nuclear-Generator` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| CompressionCraft | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/CompressionCraft` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| CrystamaeHistoria | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/CrystamaeHistoria` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DankTech2 | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/DankTech2` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DyeBench | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/DyeBench` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| EMCTech | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/EMCTech` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Element-Manipulation | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Element-Manipulation` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraTools | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/ExtraTools` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FN-FAL-s-Amplifications | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/FN-FAL-s-Amplifications` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FlowerPower | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/FlowerPower` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FoxyMachines | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/FoxyMachines` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Gastronomicon | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Gastronomicon` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| GeneticChickengineering-Reborn | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/GeneticChickengineering-Reborn` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Geyser-Slimefun-Heads | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Geyser-Slimefun-Heads` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| HeadLimiter | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/HeadLimiter` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Liquid | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Liquid` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Magic-8-Ball | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Magic-8-Ball` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MapJammers | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MapJammers` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MiniBlocks | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MiniBlocks` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MissileWarfare | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MissileWarfare` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MoreResearches | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/MoreResearches` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Netheopoiesis | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Netheopoiesis` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| PotionExpansion | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/PotionExpansion` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Quaptics | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Quaptics` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| RelicsOfCthonia | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/RelicsOfCthonia` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| RykenSlimeCustomizer-EN | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/RykenSlimeCustomizer-EN` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SaneCrafting | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SaneCrafting` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SfBetterChests | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SfBetterChests` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SfChunkInfo | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SfChunkInfo` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Simple-Storage | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Simple-Storage` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeCustomizer | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimeCustomizer` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeFrame | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimeFrame` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeHUD | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimeHUD` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimefunAdvancements | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SlimefunAdvancements` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SmallSpace | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SmallSpace` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SpiritsUnchained | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/SpiritsUnchained` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| UltimateGenerators2 | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/UltimateGenerators2` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| VillagerTrade | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/VillagerTrade` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| VillagerUtil | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/VillagerUtil` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Wildernether | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/Wildernether` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| WorldEditSlimefun | addon | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/community-addons/WorldEditSlimefun` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| dough-core | libreria | Listo (CI) | CI Monorepo · foundation | `sources/dough-core` | Stack base Paper 1.21.1 + Java 21. |
| commons-lang-drake-patched | interno | Listo (CI) | CI Monorepo · foundation | `sources/internal-metadata/patches/commons-lang-drake-patched` | Stack base Paper 1.21.1 + Java 21. |
| ColoredEnderChests | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ColoredEnderChests` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DyedBackpacks | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/DyedBackpacks` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| DynaTech | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/DynaTech` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| EcoPower | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/EcoPower` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ElectricSpawners | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ElectricSpawners` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExoticGarden | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExoticGarden` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraGear | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExtraGear` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraHeads | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExtraHeads` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| ExtraUtils | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/ExtraUtils` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| FluffyMachines | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/FluffyMachines` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| GlobalWarming | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/GlobalWarming` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| HardcoreSlimefun | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/HardcoreSlimefun` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| HotbarPets | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/HotbarPets` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| InfinityExpansion | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/InfinityExpansion` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| MobCapturer | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/MobCapturer` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| PrivateStorage | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/PrivateStorage` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SFCalc | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SFCalc` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SFMobDrops | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SFMobDrops` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SimpleUtils | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SimpleUtils` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimeChem | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimeChem` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimefunOreChunks | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimefunOreChunks` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimyRepair | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimyRepair` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SlimyTreeTaps | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SlimyTreeTaps` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SoulJars | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SoulJars` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| SoundMuffler | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/SoundMuffler` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| luckyblocks-sf | addon (port) | Listo (CI) | CI Monorepo · maven_full_reactor | `sources/repos-to-port/luckyblocks-sf` | `mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles. |
| Slimefun4-src | core | Listo (CI) | CI Monorepo · foundation | `sources/slimefun-core/Slimefun4-src` | Stack base Paper 1.21.1 + Java 21. |
| Galactifun | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/batch-2-expansion/Galactifun` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| Bump | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/Bump` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| CustomItemGenerators | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/CustomItemGenerators` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| FastMachines | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/FastMachines` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |
| SlimefunTranslation | addon (Gradle) | Listo (CI) | CI Monorepo · gradle_green | `sources/community-addons/SlimefunTranslation` | Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow. |

---

## Arbol resumido del monorepo

```text
drakes-slimefun-labs/
├─ .github/workflows/     # ci-monorepo-121.yml (CI unificado)
├─ docs/                  # guias ES/EN + PROJECT_BOARD_SYNC + PLUGIN_MATRIX
├─ scripts/               # generate_plugin_matrix.py, port_paper_121.py, manager.py
├─ sources/
│  ├─ slimefun-core/Slimefun4-src
│  ├─ dough-core/
│  ├─ batch-2-expansion/
│  ├─ community-addons/
│  ├─ repos-to-port/
│  └─ internal-metadata/
├─ pom.xml
└─ settings.gradle.kts
```

---

## Comandos útiles (copiar y pegar)

Las tablas **English / Español** arriba cubren el día a día. Aquí van recetas largas que suele pegar el equipo:

```bash
# Parche masivo Paper 1.21.1 (dry-run primero)
python scripts/port_paper_121.py --dry-run --path sources/community-addons/MiAddon

# Smoke completo local (Paper + JARs; tarda; subir timeout si hace falta)
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 420

# Verificación local alineada con el job Gradle del CI (tras install del núcleo Maven)
mvn -B -DskipTests compile -fae
./gradlew :sources:batch-2-expansion:Galactifun:compileJava :sources:community-addons:Bump:compileJava :sources:community-addons:CustomItemGenerators:compileJava :sources:community-addons:FastMachines:compileJava :sources:community-addons:SlimefunTranslation:compileJava --no-daemon

# Dependencia local necesaria para FastMachines cuando Gradle resuelve InfinityExpansion-drake
mvn -B -DskipTests install -pl sources/repos-to-port/InfinityExpansion -am
```

**Release ZIP:** GitHub → *Actions* → **Release monorepo JARs** → *Run workflow* (tag único cada vez). Requiere que el reactor `package` sea verde (incluye paso Gradle de SlimefunTranslation, ver workflow).

---

## Licencia

Proyecto bajo **GPLv3**. Ver [`LICENSE`](LICENSE).
