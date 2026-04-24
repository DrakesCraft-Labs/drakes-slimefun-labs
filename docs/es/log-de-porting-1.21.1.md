# 🏛️ Drake Slimefun 1.21.1 Porting Guide

Este repositorio contiene el ecosistema unificado de Slimefun preparado para **Paper 1.21.1** y **Java 21**.

## 🧬 Identidad Unificada
A partir de la v15.0, todos los módulos deben usar la identidad oficial de DrakesLab:
- **GroupId**: `com.github.drakescraft_labs`
- **Parent**: `drakes-slimefun-labs`
- **Estandarización**: Todos los addons heredan versiones y dependencias del reactor central.

## 🛠️ Herramientas de Gestión (DrakesLab Manager)
Hemos reemplazado los antiguos scripts de PowerShell por una herramienta profesional en Python: `scripts/manager.py`.

### Comandos Principales:
1. **Auditoría**: `python scripts/manager.py audit`
   - Muestra el progreso real de los 89 addons (Surgical, Stabilized, Gradle).
2. **Sincronización**: `python scripts/manager.py`
   - Repara automáticamente las identidades de Maven y las dependencias internas rotas.

## 🐘 Reactor Híbrido (Maven + Gradle)
- **Maven**: Gestiona el core y 80 addons. Usa el `pom.xml` raíz.
- **Gradle**: Gestiona los 9 addons "rebeldes" (Galactifun, Bump, etc.) mediante el `settings.gradle.kts` raíz.

## 🚀 Requisitos de Compilación
- **Java 21** (Obligatorio)
- **Maven 3.9+**
- **Python 3.12** (Para el Manager)

## 🏁 Flujo de Trabajo
Para añadir o actualizar un addon:
1. Colócalo en `sources/community-addons`.
2. Ejecuta el **Manager** para unificar su identidad.
3. Agrégalo al reactor correspondiente (`pom.xml` o `settings.gradle.kts`).
4. El **Unified Engine** de GitHub Actions se encargará del resto.

## 📜 Bitácora de Estabilización (Abril 2026)

### 🛰️ Sesión 24.04 (Día de la Victoria) - Estado: 97.6% Global
- **Unificación 100% Addons**: Todos los addons (Maven y Gradle) han sido rebrandeados a `com.github.drakescraft_labs`.
- **Soporte Kotlin**: El Manager (v4.13) ahora procesa archivos `.kt` y estructuras `src/main/kotlin`.
- **Asimilación de Autores**: Se completó el rebranding de autores rebeldes (`me.vaan`, `io.github.slimefunguguproject`, `io.github.addoncommunity`).
- **Blindaje de Dependencias**:
    - Reparada la paradoja del Parent POM (`groupId` unificado).
    - Reglas de reversión para librerías externas (`ErrorReporter`, `GuizhanLib`).
    - Sincronización automática de dependencias internas (`InfinityLib`, `SefiLib`).
- **Higiene del Reactor**: Purgadas carpetas huérfanas (`Cultivation`, `EMC2`, `Networks`, `SlimefunWarfare`).

### 🛡️ Próximos Objetivos
1.  **Validación de Runtime**: Pruebas de inyección de dependencias en servidor 1.21.1.
2.  **Modernización de los Núcleos**: Iniciar cirugía sobre `dough-core` y `slimefun-core`.
