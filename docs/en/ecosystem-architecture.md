# 🏛️ DrakesLab Ecosystem Architecture

## What is this Lab?
`drakes-slimefun-labs` is a technical consolidation monorepo designed to port, stabilize, and document the Slimefun ecosystem for **Paper 1.21.1** and **Java 21**.

## 🧬 Unified Identity and Namespace
As of v15.0, the ecosystem uses a unified namespace to prevent collisions and ensure reliable dependency resolution on GitHub Packages:
- **Master Namespace**: `com.github.drakescraft-labs`
- **Version Prefix**: `Drake-1.21.1`

## 🏗️ Hybrid Build System
The lab uses a dual build system to support 100% of the ecosystem addons:

### 1. Maven Reactor (80 Modules)
Manages the core and most addons via the root `pom.xml`. It centralizes:
- Common version properties (`${slimefun.drake.version}`, `${dough.version}`).
- Relocated dependency management (`dough-core`).
- Parent POM alignment for surgical addons.

### 2. Master Gradle Reactor (9 Modules) 🐘
Manages Gradle-based addons (such as Galactifun or SlimefunTranslation) via the root `settings.gradle.kts`.
- Forces **Java 21** standards across all subprojects.
- Connects addons to DrakesLab libraries published on Maven.

## 🐍 DrakesLab Manager
Ecosystem integrity is maintained via `scripts/manager.py`. Its functions include:
- **Audit**: Real-time tracking of all 89 addons.
- **Hot-Repair**: XML identity synchronization and internal dependency bridge repair.

## 🚀 CI/CD: Unified Engine
A single master workflow (`unified-engine.yml`) handles:
- Ecosystem-wide audits.
- Parallel Maven and Gradle reactor builds.
- Automated deployment of core modules (`Dough`, `Slimefun`, `SefiLib`, `InfinityLib`) upon successful builds.

## 📂 Workspace Layout
- `sources/dough-core`: Unified base library (`com.github.drakescraft-labs`).
- `sources/slimefun-core`: Adapted Slimefun core.
- `sources/repos-to-port`: Stabilized priority batch.
- `sources/batch-2-expansion`: Active libraries and expansions.
- `sources/community-addons`: Community archive under integration.

---
**Related Navigation**:
- [Migration Checklist](migration-checklist.md)
- [Porting Guide 1.21.1](../docs/README-PORT-1.21.1.md)
- [Technical Reference](technical-reference-paper-1.21.1.md)
