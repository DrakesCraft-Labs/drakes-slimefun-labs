<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# 🐉 Slimefun 6 Core (Drake Framework)
### *Natively Updated for Minecraft 1.21.11 & Java 21*

Este es el núcleo de **Slimefun 6**, una evolución tecnológica diseñada para la independencia técnica y el alto rendimiento en la versión **1.21.11**.

---

## 🚀 Slimefun 6: ¿Qué ha cambiado?
Esta versión marca un hito en el desarrollo de Slimefun bajo el **Drake Framework**:

- **Soporte Paper 1.21.11 Nativo**: Eliminación de hacks NMS rotos en favor de APIs modernas de Paper.
- **Dough-Core Integration**: Uso de la librería unificada de Drake para mayor estabilidad.
- **Java 21**: Explotando las últimas características de la JVM.
- **Unificación de Addons**: Diseñado como el motor central para el port masivo de más de 60 addons.

---

## 🤝 Créditos y Reconocimientos
Este proyecto es un derivado del trabajo monumental de la comunidad de Slimefun:

- **Proyecto Original**: [`Slimefun/Slimefun4`](https://github.com/Slimefun/Slimefun4)
- **Autor Original**: **[TheBusyBiscuit](https://github.com/TheBusyBiscuit)**
- **Equipo de Slimefun**: Walshy y más de 200 colaboradores.
- **Slimefun 6 / Drake Port**: **[Pablo Elías](https://github.com/JackStar6677-1)** (DrakesCraft-Labs)

---

## 🔧 Detalles Técnicos
El jar publicado sigue la nomenclatura:
- `Slimefun v6.0-Drake-1.21.11.jar`

### Cambios Clave:
- Portado al reactor Maven de Drake.
- Migración de hotspots de compatibilidad a APIs directas de Paper (Profiles, Heads, NBT).
- Limpieza de dependencias obsoletas.

---

## 📄 Licencia
Este proyecto continúa bajo la licencia **GPL-3.0**, respetando el trabajo original de todos los contribuidores de Slimefun4.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
