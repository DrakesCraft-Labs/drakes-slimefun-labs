# Drakes Slimefun Labs

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper Version](https://img.shields.io/badge/Paper-1.21.11-blue?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![Slimefun Version](https://img.shields.io/badge/Slimefun-6.0--Drake-green?style=for-the-badge)](https://github.com/Slimefun/Slimefun4)
[![License](https://img.shields.io/badge/License-GPL%20v3-red?style=for-the-badge)](LICENSE)

Repositorio de trabajo para la migración del ecosistema Slimefun hacia `Paper 1.21.11`, `Java 21`, `Slimefun 6` y el stack `Drake Framework`.

Este repo no representa un único plugin. Es un laboratorio de consolidación, porting, validación y documentación para un conjunto amplio de addons, librerías base y variantes de compatibilidad. El objetivo no es solo que “compile”, sino dejar una base que el equipo pueda entender, continuar y validar en runtime sin ambigüedades.

## Idioma / Language

- README principal en español: [README.md](README.md)
- README en inglés: [README_EN.md](README_EN.md)
- Portada de docs en inglés: [Home-EN](docs/en/home.md)

## Acceso Rápido

| Recurso | Mirror Local (`.md`) | GitHub Wiki | Uso |
| :--- | :--- | :--- | :--- |
| Portada de documentación | [Home](docs/es/home.md) | [Home](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki) | Punto de entrada general. |
| Checklist operativo | [Checklist de Migración](docs/es/migration-checklist.md) | [Checklist de Migración](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Checklist-de-Migraci%C3%B3n) | Estado resumido y prioridades. |
| Módulos faltantes | [Módulos Pendientes](docs/es/pending-modules.md) | [Módulos Pendientes](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/M%C3%B3dulos-Pendientes) | Qué sigue fuera del reactor o con bloqueo activo. |
| Project backlog | [Slimefun 1.21.11 Migration Backlog](https://github.com/orgs/DrakesCraft-Labs/projects/1/views/1) | Tablero operativo público para priorizar integración, port técnico y smoke tests. |
| Roadmap | [Roadmap de Estabilización](docs/es/stabilization-roadmap.md) | [Roadmap de Estabilización](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Roadmap-de-Estabilizaci%C3%B3n) | Orden sugerido de cierre. |
| Handoff diario | [Tomorrow-Handoff](docs/es/tomorrow-handoff.md) | [Tomorrow-Handoff](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Tomorrow-Handoff) | Continuidad entre sesiones. |
| Arquitectura | [Arquitectura del Ecosistema](docs/es/ecosystem-architecture.md) | [Arquitectura del Ecosistema](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Arquitectura-del-Ecosistema) | Explica el stack Drake y la organización del workspace. |
| CI Modular | [CI Architecture](CI_ARCHITECTURE.md) | [CI Architecture](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/CI-Architecture) | Funcionamiento de los 4 Gates paralelos y v7. |
| Referencia técnica 1.21.1 | [Referencia Técnica (Paper 1.21.1)](docs/es/technical-reference-paper-1.21.1.md) | [Referencia Técnica (Paper 1.21.1)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Referencia-T%C3%A9cnica-(Paper-1.21.1)) | Hallazgos de API, compatibilidad y criterios de port. |
| Smoke test | [Guía de Smoke Test](docs/es/smoke-test-guide.md) | [Guía de Smoke Test](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Gu%C3%ADa-de-Smoke-Test) | Validación de runtime básica. |
| Dev setup | [Dev-Setup](docs/es/development-setup.md) | [Dev-Setup](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Dev-Setup) | Preparación del entorno de trabajo. |
| Estándares de desarrollo | [Estándares de Desarrollo](docs/es/development-standards.md) | [Estándares de Desarrollo](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Est%C3%A1ndares-de-Desarrollo) | Convenciones y criterios de calidad. |
| Guía para IA | [Instrucciones para la IA](docs/es/ai-instructions.md) | [Instrucciones para la IA](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Instrucciones-para-la-IA) | Contexto y reglas para sesiones asistidas. |
| Plantilla de addon | [New-Addon-Template](docs/es/new-addon-template.md) | [New-Addon-Template](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/New-Addon-Template) | Explicación de la plantilla estándar. |
| Estrategia de releases/CI | [Estrategia de Releases y CI](docs/es/release-and-ci-strategy.md) | [Estrategia de Releases y CI](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Estrategia-de-Releases-y-CI) | Política de publicación y automatización. |
| Template real | [Template README](templates/slimefun-addon/README.md) | No aplica | Base local para crear nuevos addons del stack. |

## Estado Ejecutivo (La Gran Obra)

> [!IMPORTANT]
> Rama de trabajo principal: `1.21-latin` | Identidad Unificada: `com.github.drakescraft_labs`

El laboratorio cubre actualmente un universo de **89 addons** más los módulos base. Hemos logrado una arquitectura híbrida de alto rendimiento.

| Métrica | Valor | Estado |
| :--- | :--- | :--- |
| **REACTOR MAVEN** | `80` | 🛡️ Unificados bajo `com.github.drakescraft_labs` |
| **REACTOR GRADLE** | `8` | 🐘 Modernizados con Java 21 |
| **Progreso Estructural** | **100%** | **89/89 Addons Integrados** |
| **Progreso Quirúrgico** | **67.4%** | **60/89 Addons con Rebrand** |
| **Base Unificada** | `com.github.drakescraft_labs:dough-core` | `1.3.1-DRAKE-v11-SNAPSHOT` |
| **Core Slimefun** | `com.github.drakescraft_labs:slimefun-core` | `11.0-Drake-1.21.1-SNAPSHOT` |
| **Unified Engine** | `Actions: DrakesLab Unified Engine` | 🚀 Full CI Híbrido |

### Cómo interpretar este estado

- **REACTOR MAVEN**: Los 80 módulos Maven ya tienen sus identidades sincronizadas y dependen del reactor central.
- **REACTOR GRADLE**: Los 9 rebeldes ya operan bajo el mando del Reactor Maestro de Gradle.
- **SURGICAL**: 60 addons ya han pasado por la cirugía final de rebranding `-drake`.
- **DrakesLab Manager**: La herramienta `scripts/manager.py` es ahora el estándar para gestionar este universo de 89 addons.

## Qué Es Este Repo y Qué No Es

Este repositorio:
- centraliza versiones, dependencias y módulos Maven
- contiene el port del core y de `dough-core`
- sirve como laboratorio de migración para addons de procedencias distintas
- documenta el estado real del ecosistema en una sola rama operativa

Este repositorio no:
- representa un único plugin listo para distribución pública
- garantiza por sí solo que todos los addons “listos” ya estén smoke-testeados en runtime real
- implica que todos los addons presentes en `sources/*` formen parte del reactor actual

## Estructura del Workspace

| Ruta | Propósito |
| :--- | :--- |
| `sources/dough-core` | Librería base unificada y relocalizada a `dev.drake.dough`. |
| `sources/slimefun-core/Slimefun4-src` | Core Slimefun adaptado al stack actual. |
| `sources/repos-to-port` | Batch 1: addons históricamente prioritarios para portar. |
| `sources/batch-2-expansion` | Librerías, expansiones y variantes de compatibilidad. |
| `sources/community-addons` | Archivo comunitario integrado progresivamente al reactor. |
| `templates/slimefun-addon` | Plantilla base para nuevos addons del ecosistema. |
| `wiki_temp` | Espejo local editable de la wiki pública del repo. |

## Flujo de Trabajo Recomendado

### Compilación aislada

No se debe compilar el reactor completo salvo necesidad estricta. El patrón recomendado es:

```powershell
mvn -pl ruta/del/modulo -am -DskipTests package
```

Ejemplos:

```powershell
mvn -pl sources/repos-to-port/SimpleUtils -am -DskipTests package
mvn -pl sources/repos-to-port/DynaTech -am -DskipTests package
mvn -pl sources/community-addons/MapJammers -am -DskipTests compile
```

### Significado de las flags

- `-pl`: selecciona el módulo objetivo
- `-am`: compila también sus dependencias dentro del reactor
- `-DskipTests`: evita tests para acelerar ciclos de validación

### Smoke test de servidor

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\slimefun\smoke-test.ps1
```

### DrakesLab Manager (Ecosystem Control) 🐍

La gestión de este universo de 89 módulos se centraliza en `scripts/manager.py`. Esta herramienta en Python 3.12 es el corazón operativo del laboratorio.

- **Auditoría de la Gran Obra**: `python scripts/manager.py audit`
  - Genera el reporte de estado en tiempo real (Surgical, Stabilized, Gradle).
- **Reparación de Identidades**: `python scripts/manager.py`
  - Sincroniza automáticamente los GroupIDs, corrige dependencias internas y repara puentes de librerías rotos.

## Arquitectura del Stack 🏛️

El ecosistema usa el **Drake Framework** como base de consolidación técnica:

- **Blindaje Nuclear**: Integración de `DrakesLab Manager v3.4` para validación XML, backups y auditoría de sombras.
- **Estandarización Total**: Sincronización de dependencias globales y migración completa a **Paper-API 1.21.1**.
- **Identidad Profunda**: Rebranding masivo de namespaces en artefactos y patrones de sombreado (relocations).
- **Auditoría Automatizada**: Métricas en tiempo real sincronizadas cada vez que el reactor procesa un cambio.

Para contexto profundo:
- [Arquitectura del Ecosistema](docs/es/ecosystem-architecture.md)
- [Guía de Porteo 1.21.1](sources/docs/README-PORT-1.21.1.md)
- [Referencia Técnica (Paper 1.21.1)](docs/es/technical-reference-paper-1.21.1.md)

## Inventario Explícito 1.21.11

Esta sección es la fuente de verdad humana del repositorio. Si un conteo viejo o un comentario histórico contradice esta lista, manda esta lista.

### Base/Core listo para 1.21.11

| Componente | Estado | Observación |
| :--- | :--- | :--- |
| `sources/dough-core` | `Listo` | Núcleo unificado `1.3.1-DRAKE`, usado por el stack completo. |
| `sources/slimefun-core/Slimefun4-src` | `Listo` | Core estabilizado con puentes de compatibilidad para addons legacy. |

### Batch 1 `sources/repos-to-port` listo para 1.21.11

Los `26/26` módulos de esta carpeta se consideran listos dentro de esta rama.

| Addon | Estado | Observación |
| :--- | :--- | :--- |
| `ColoredEnderChests` | `Listo` | Sin observación especial actual. |
| `DyedBackpacks` | `Listo` | Sin observación especial actual. |
| `DynaTech` | `Listo` | Alineado al reactor Drake y dependencias actuales. |
| `EcoPower` | `Listo` | Corregida herencia del parent y dependencia a `dough-core`. |
| `ElectricSpawners` | `Listo` | Sin observación especial actual. |
| `ExoticGarden` | `Listo` | Ya trabajado para este stack; revisar runtime si se usan integraciones externas no incluidas. |
| `ExtraGear` | `Listo` | Sin observación especial actual. |
| `ExtraHeads` | `Listo` | Convertido e integrado al reactor Maven unificado. |
| `ExtraUtils` | `Listo` | Referencia útil para dependencias comunitarias antiguas. |
| `FluffyMachines` | `Listo` | Sin observación especial actual. |
| `GlobalWarming` | `Listo` | Sin observación especial actual. |
| `HardcoreSlimefun` | `Listo` | Sin observación especial actual. |
| `HotbarPets` | `Listo` | Corregidos errores de API en el port a 1.21.11. |
| `InfinityExpansion` | `Listo` | Depende del `InfinityLib` local del reactor. |
| `luckyblocks-sf` | `Listo` | Sin observación especial actual. |
| `MobCapturer` | `Listo` | Convertido e integrado al reactor Maven unificado. |
| `PrivateStorage` | `Listo` | Corregidos errores de API durante la migración. |
| `SFCalc` | `Listo` | Validado como quick win. |
| `SFMobDrops` | `Listo` | Sin observación especial actual. |
| `SimpleUtils` | `Listo` | Su cierre dependió de alinear `InfinityLib` al reactor actual. |
| `SlimeChem` | `Listo` | Corregidos errores de API durante el port. |
| `SlimefunOreChunks` | `Listo` | Sin observación especial actual. |
| `SlimyRepair` | `Listo` | Validado como quick win. |
| `SlimyTreeTaps` | `Listo` | Compila en esta rama; revisar runtime si intervienen plugins de protección. |
| `SoulJars` | `Listo` | Sin observación especial actual. |
| `SoundMuffler` | `Listo` | Corregidos errores de API legacy. Conviene smoke test por tratarse de sonidos/eventos. |

### Batch 2 activo listo para 1.21.11

Estas son las librerías y variantes activas dentro del stack actual.

| Componente | Estado | Observación |
| :--- | :--- | :--- |
| `SefiLib` | `Listo` | Librería de soporte para expansiones. |
| `InfinityLib` | `Listo` | Alineado al parent y utilizado por varios addons del ecosistema. |
| `Cultivation_Updated` | `Listo` | Variante activa adoptada. No confundir con `Cultivation` original. |
| `LiteXpansion` | `Listo` | Sin observación especial actual. |
| `Networks_Better_Compatibility` | `Listo` | Variante activa adoptada. No confundir con `Networks` original. |
| `SlimeTinker` | `Listo` | Alineado al parent del reactor y a `Slimefun 6.0-Drake-1.21.11`. |
| `SMG` | `Listo` | En el reactor figura con el nombre `SimpleMaterialGenerators`. |
| `Supreme` | `Listo` | Sin observación especial actual. |
| `TranscEndence` | `Listo` | Sin observación especial actual. |

### Community addons activos listos para 1.21.11

Estos módulos ya están integrados al reactor y hoy compilan en esta rama.

| Addon | Estado | Observación |
| :--- | :--- | :--- |
| `AlchimiaVitae` | `Listo` | Sin observación especial actual. |
| `CrystamaeHistoria` | `Listo` | Sin observación especial actual. |
| `DankTech2` | `Listo` | Sin observación especial actual. |
| `DyeBench` | `Listo` | Integrado al reactor y migrado a `dev.drake.dough.*`. |
| `Element-Manipulation` | `Listo` | Tuvo port real de API y serializers; conviene smoke test en runtime. |
| `ExtraTools` | `Listo` | Verificado en build aislado. |
| `FlowerPower` | `Listo` | Ajustado a API moderna de atributos de Bukkit/Paper. |
| `FN-FAL-s-Amplifications` | `Listo` | Verificado en build aislado. |
| `FoxyMachines` | `Listo` | Limpiado de usos legacy de utilidades viejas. |
| `HeadLimiter` | `Listo` | Integrado al reactor. `Towny` es opcional y debe validarse en runtime si se usa. |
| `GeneticChickengineering-Reborn` | `Listo` | Integrado al reactor. Corregidas dependencias de Lombok/bStats y migrado a Java 21. |
| `Liquid` | `Listo` | Verificado en build aislado. |
| `Magic-8-Ball` | `Listo` | Validado como quick win. |
| `MapJammers` | `Listo` | Requiere `squaremap` o `dynmap` en runtime para ser funcional. |
| `MiniBlocks` | `Listo` | Integrado con `InfinityLib` local. Mantiene warnings por API deprecated de Bukkit, pero compila en Java 21. |
| `MissileWarfare` | `Listo` | Corregidas partículas obsoletas; recomendable smoke test por tratarse de combate/efectos. |
| `PotionExpansion` | `Listo` | Migrado a API de pociones de 1.21.1 y corregidas llamadas legacy de SlimefunItemStack. |
| `RykenSlimeCustomizer-EN` | `Listo` | Verificado en build aislado. |
| `SfChunkInfo` | `Listo` | Validado como quick win. |
| `Simple-Storage` | `Listo` | Verificado en build aislado. Conviene prueba de runtime por inventarios/red. |
| `SlimeCustomizer` | `Listo` | Verificado en build aislado. Conviene prueba de runtime por ser muy configurable. |
| `UltimateGenerators2` | `Listo` | Convertido a Maven, alineado a `dev.drake.dough.*` e integrado al reactor unificado. |
| `VillagerUtil` | `Listo` | Verificado en build aislado. |

### Activos en reactor pero todavía NO listos

Estos módulos ya están dentro del build unificado, pero todavía presentan fallo confirmado o requieren port técnico adicional.

| Addon | Estado | Observación |
| :--- | :--- | :--- |
| - | - | Todos los módulos activos en el reactor están actualmente estables. |

### Integración Global de Addons (89/89)

Todos los addons presentes en este laboratorio han sido integrados en sus respectivos reactores.

| Addon | Reactor | Estado | Observación |
| :--- | :---: | :--- | :--- |
| `Galactifun` | 🐘 Gradle | `STABILIZED` | Reactor Maestro de Gradle. |
| `Bump` | 🐘 Gradle | `STABILIZED` | Reactor Maestro de Gradle. |
| `SlimefunTranslation` | 🐘 Gradle | `STABILIZED` | Reactor Maestro de Gradle. |
| `AdvancedTech` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `Better-Nuclear-Generator` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `CompressionCraft` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `EMCTech` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `Gastronomicon` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `Quaptics` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `SlimeFrame` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `WorldEditSlimefun` | 🛡️ Maven | `STABILIZED` | Integrado en el Reactor Maestro de Maven. |
| `... (otros 78 módulos)` | 🛡️ Maven | `STABILIZED` | Todos sincronizados con `com.github.drakescraft-labs`. |

## Observaciones Operativas Importantes

- `MapJammers`: compila, pero su funcionalidad real depende de `squaremap` o `dynmap`.
- `HeadLimiter`: compila y carga su lógica propia; si se usa `Towny`, conviene validar wilderness/claims en runtime.
- `MiniBlocks`: compila en Java 21, pero mantiene warnings por API deprecated de Bukkit.
- `MissileWarfare`: compila después del ajuste de partículas obsoletas; por ser addon visual/combate, requiere smoke test real.
- `Element-Manipulation`: ya quedó alineado a serializers y utilidades modernas; conviene validación runtime por efectos y mecánicas.
- `Simple-Storage`: compila, pero al tocar inventarios y red conviene prueba con servidor real.
- `SlimeCustomizer` y `RykenSlimeCustomizer-EN`: compilan, pero por ser altamente configurables conviene validación con configs reales.
- `Cultivation_Updated` y `Networks_Better_Compatibility`: son las variantes activas. No deben mezclarse con sus variantes originales al hablar de “listo”.
- `GeneticChickengineering-Reborn`: ya compila en el reactor; conviene smoke test por rendimiento, entidades y comportamiento de máquinas.
- `PotionExpansion`: ya compila en el reactor; conviene smoke test por efectos, pociones y compatibilidad gameplay.
- `UltimateGenerators2`: ya compila en el reactor tras su conversión a Maven; conviene prueba de runtime por multibloques, generación y componentes Kotlin/GuizhanLib.

## Arquitectura del Stack

El ecosistema usa el **Drake Framework** como base de consolidación técnica:

- un `pom.xml` raíz que controla versiones, módulos y dependencias
- `dough-core` relocalizado a `dev.drake.dough`
- un core Slimefun adaptado para `1.21.11`
- variantes activas de compatibilidad en los casos donde el original histórico no es la opción vigente

Para contexto más profundo:

- [Arquitectura del Ecosistema](docs/es/ecosystem-architecture.md)
- [Arquitectura del Ecosistema en la Wiki](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Arquitectura-del-Ecosistema)
- [Referencia Técnica (Paper 1.21.1)](docs/es/technical-reference-paper-1.21.1.md)
- [Referencia Técnica (Paper 1.21.1) en la Wiki](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Referencia-T%C3%A9cnica-(Paper-1.21.1))

## Documentación Complementaria

| Tema | Mirror Local | GitHub Wiki |
| :--- | :--- | :--- |
| Estado y prioridades | [Checklist de Migración](docs/es/migration-checklist.md) | [Checklist de Migración](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Checklist-de-Migraci%C3%B3n) |
| Backlog detallado | [Módulos Pendientes](docs/es/pending-modules.md) | [Módulos Pendientes](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/M%C3%B3dulos-Pendientes) |
| Roadmap operativo | [Roadmap de Estabilización](docs/es/stabilization-roadmap.md) | [Roadmap de Estabilización](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Roadmap-de-Estabilizaci%C3%B3n) |
| Smoke test | [Guía de Smoke Test](docs/es/smoke-test-guide.md) | [Guía de Smoke Test](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Gu%C3%ADa-de-Smoke-Test) |
| Handoff | [Tomorrow-Handoff](docs/es/tomorrow-handoff.md) | [Tomorrow-Handoff](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Tomorrow-Handoff) |
| Setup local | [Dev-Setup](docs/es/development-setup.md) | [Dev-Setup](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Dev-Setup) |
| Estándares | [Estándares de Desarrollo](docs/es/development-standards.md) | [Estándares de Desarrollo](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Est%C3%A1ndares-de-Desarrollo) |
| Guía IA | [Instrucciones para la IA](docs/es/ai-instructions.md) | [Instrucciones para la IA](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Instrucciones-para-la-IA) |
| Prompt inicial IA | [AI-Start-Prompt](docs/es/ai-start-prompt.md) | [AI-Start-Prompt](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/AI-Start-Prompt) |
| Plantilla de addon | [New-Addon-Template](docs/es/new-addon-template.md) | [New-Addon-Template](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/New-Addon-Template) |
| Estrategia de releases/CI | [Estrategia de Releases y CI](docs/es/release-and-ci-strategy.md) | [Estrategia de Releases y CI](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/Estrategia-de-Releases-y-CI) |

## Template de Nuevo Addon

Para crear nuevos módulos alineados al stack actual:

- explicación: [New-Addon-Template](docs/es/new-addon-template.md)
- wiki pública: [New-Addon-Template](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/wiki/New-Addon-Template)
- base local real: [templates/slimefun-addon/README.md](templates/slimefun-addon/README.md)

## Créditos

Este proyecto existe sobre el trabajo acumulado de varios desarrolladores del ecosistema Slimefun.

| Dev | Contribución |
| :--- | :--- |
| **TheBusyBiscuit** | Autor original de Slimefun 4. |
| **Sefiraat** | Mentor de librerías de expansión y addons técnicos. |
| **Mooy1** | Creador de InfinityExpansion y de parte importante de la arquitectura modular asociada. |
| **Chagui68** | Variantes de compatibilidad y lógica de transición. |
| **[Pablo Elías](https://github.com/JackStar6677-1)** | Liderazgo del port `1.21.11` y consolidación del Drake Framework. |

Drakes Slimefun Labs es un proyecto independiente. Todas las marcas registradas pertenecen a sus respectivos dueños.
