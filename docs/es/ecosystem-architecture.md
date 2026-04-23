# 🏛️ Arquitectura del Ecosistema DrakesLab

## Qué es este laboratorio
`drakes-slimefun-labs` es un monorepo de consolidación técnica diseñado para portar, estabilizar y documentar el ecosistema Slimefun sobre **Paper 1.21.1** y **Java 21**.

## 🧬 Identidad y Namespace
A partir de la v15.0, el ecosistema utiliza un namespace unificado para evitar colisiones y asegurar la resolución de dependencias en GitHub Packages:
- **Namespace Maestro**: `com.github.drakescraft-labs`
- **Prefijo de Versión**: `Drake-1.21.1`

## 🏗️ Reactor Híbrido (Hybrid Build System)
El laboratorio utiliza un sistema de construcción dual para soportar el 100% de los addons del ecosistema:

### 1. Reactor Maven (80 Módulos)
Gestiona el núcleo y la gran mayoría de addons mediante el `pom.xml` raíz. Centraliza:
- Propiedades de versión comunes (`${slimefun.drake.version}`, `${dough.version}`).
- Gestión de dependencias relocalizadas (`dough-core`).
- Alineación del Parent POM para addons quirúrgicos.

### 2. Reactor Gradle Maestro (9 Módulos) 🐘
Gestiona los addons basados en Gradle (como Galactifun o SlimefunTranslation) mediante el `settings.gradle.kts` raíz.
- Fuerza el estándar **Java 21** en todos los subproyectos.
- Conecta los addons a las librerías de DrakesLab publicadas en Maven.

## 🐍 DrakesLab Manager
La integridad del ecosistema se mantiene mediante `scripts/manager.py`. Sus funciones incluyen:
- **Auditoría**: Seguimiento del estado de los 89 addons.
- **Hot-Repair**: Sincronización de identidades XML y reparación de puentes de dependencias internas.

## 🚀 CI/CD: Unified Engine
Contamos con un único workflow maestro (`unified-engine.yml`) que:
- Ejecuta auditorías de ecosistema.
- Compila los reactores Maven y Gradle en paralelo.
- Despliega automáticamente los módulos core (`Dough`, `Slimefun`, `SefiLib`, `InfinityLib`) si el build es exitoso.

## 📂 Organización del Workspace
- `sources/dough-core`: Librería base unificada (`com.github.drakescraft-labs`).
- `sources/slimefun-core`: Núcleo Slimefun adaptado.
- `sources/repos-to-port`: Batch prioritario estabilizado.
- `sources/batch-2-expansion`: Librerías y expansiones activas.
- `sources/community-addons`: Archivo comunitario en proceso de integración.

---
**Navegación Relacionada**:
- [Checklist de Migración](migration-checklist.md)
- [Guía de Porteo 1.21.1](README-PORT-1.21.1.md)
- [Referencia Técnica](technical-reference-paper-1.21.1.md)
