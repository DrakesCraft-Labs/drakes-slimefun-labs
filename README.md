# Drakes Slimefun Labs

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper Baseline](https://img.shields.io/badge/Paper-1.21.1-blue?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![Slimefun Core](https://img.shields.io/badge/Slimefun-11.0--Drake--1.21.1-green?style=for-the-badge)](https://github.com/Slimefun/Slimefun4)
[![License](https://img.shields.io/badge/License-GPL%20v3-red?style=for-the-badge)](LICENSE)

Monorepo de ingenieria para portar, estabilizar y documentar addons de Slimefun sobre una base comun moderna: **Paper 1.21.1 + Java 21**.

## Vision del proyecto

`drakes-slimefun-labs` no es un repositorio de un solo plugin. Es un laboratorio de consolidacion tecnica para un ecosistema completo de addons, librerias y variantes de compatibilidad.

Objetivos:

- Unificar baseline de compilacion y runtime en todo el ecosistema.
- Reducir deuda tecnica heredada (dependencias antiguas, versiones mezcladas, configuraciones divergentes).
- Mantener una migracion por lotes con trazabilidad real en CI.
- Exponer estado real sin claims inflados de "100% produccion".

## Arquitectura del monorepo

Estructura principal:

- `sources/slimefun-core/Slimefun4-src`: core de Slimefun adaptado a la linea Drake.
- `sources/dough-core`: libreria base compartida para utilidades y compatibilidad.
- `sources/batch-2-expansion`: expansiones/librerias en fase activa de hardening.
- `sources/community-addons`: addons comunitarios integrados al flujo comun.
- `sources/repos-to-port`: repositorios en proceso de normalizacion tecnica.
- `sources/internal-metadata`: patches internos, auditorias y metadatos.
- `.github/workflows`: estrategia CI por gates.

Este diseno permite aislar riesgos por grupo de modulos y avanzar por fases.

## Baseline tecnico

Baseline vigente del repositorio:

- **Paper:** `1.21.1`
- **Java:** `21`
- **Stack base:** Slimefun 6 (linea Drake) + dough-core unificado
- **Identidad Maven:** `com.github.drakescraft_labs`

Notas:

- El baseline define objetivo de build e interoperabilidad minima.
- Build exitoso no equivale automaticamente a compatibilidad total en runtime.
- Todo addon critico debe pasar smoke test en servidor real.

## Estado real de migracion

Estado actual:

- Se alinearon descriptores de build a `Paper 1.21.1` y Java 21 en addons auditados.
- Se eliminaron referencias activas de baseline `1.21.11` en descriptors principales.
- Se corrigieron valores legacy de `source/target` 16/17/1.8 hacia 21.
- Persisten modulos con deuda funcional o dependencias externas no resolubles en entorno local.

Interpretacion:

- **Si:** hay avance fuerte de consistencia tecnica.
- **No:** no todos los addons estan listos para produccion sin validacion runtime.

## Estrategia CI por gates

El CI se divide en compuertas para aislar fallos y mantener velocidad:

- **Gate 1 - Foundation:** validacion de base comun y dependencias criticas.
- **Gate 2 - Stable:** modulos con mayor madurez.
- **Gate 3 - Community:** addons de comunidad integrados.
- **Gate 4 - Complex:** modulos de mayor acoplamiento o riesgo historico.
- **Gate 5 - Gradle:** subproyectos Gradle fuera del flujo Maven puro.

Beneficios:

- Diagnostico mas rapido.
- Menor blast radius ante regresiones.
- Migracion incremental sostenible.

## Instalacion y uso

Requisitos:

- JDK 21 en `JAVA_HOME`
- Maven 3.8+
- Gradle (cuando aplique)
- Git

Clonado:

```bash
git clone https://github.com/DrakesCraft-Labs/drakes-slimefun-labs.git
cd drakes-slimefun-labs
```

Build rapido del reactor:

```bash
mvn -T 1C -DskipTests package
```

Build focalizado:

```bash
mvn -pl sources/community-addons/Gastronomicon -am -DskipTests compile
```

Modulo Gradle (ejemplo):

```bash
cd sources/community-addons/FastMachines
./gradlew build
```

## Contribucion

Buenas practicas para PRs:

- Cambios pequenos y con alcance claro.
- No mezclar migracion de baseline con refactors no relacionados.
- Adjuntar evidencia de build/check de los modulos tocados.
- Documentar explicitamente: migrado, pendiente y riesgos residuales.

## Roadmap

1. Cerrar brechas restantes de baseline fuera de CI estable.
2. Reducir dependencias snapshots/forks no reproducibles.
3. Mejorar cobertura de pruebas por addon (unit + smoke).
4. Consolidar guias de port para contribucion externa.
5. Aumentar predictibilidad de empaquetado en entorno de laboratorio.

## Riesgos conocidos

- Addons sensibles a integraciones externas (protecciones, placeholders, economia).
- Dependencias no publicadas o privadas que bloquean builds locales.
- Diferencias de calidad entre modulos por origen y antiguedad.
- "CI verde" no significa automaticamente "runtime listo para produccion".

## FAQ

### El repositorio ya esta 100% migrado?

No. El baseline tecnico esta mucho mas consistente, pero quedan addons con deuda funcional y pruebas de runtime pendientes.

### Por que usar gates en CI?

Porque el monorepo es grande y heterogeneo. Los gates permiten aislar fallos y seguir migrando sin bloquear todo el ecosistema.

### Paper 1.21.1 y Java 21 son obligatorios?

Si para el baseline actual del repositorio. Modulos historicos deben converger a este objetivo.

### Dónde esta la documentacion extendida?

- `docs/es/home.md`
- `docs/es/migration-checklist.md`
- `docs/es/pending-modules.md`
- `docs/es/technical-reference-paper-1.21.1.md`
- `docs/en/home.md`

## Licencia

Este proyecto se distribuye bajo GPL v3. Ver `LICENSE`.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
