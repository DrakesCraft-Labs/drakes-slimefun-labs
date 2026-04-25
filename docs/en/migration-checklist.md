# 🏁 DrakesLab Migration Checklist

## 📊 Fleet Summary (v15.6)
- **Total Universe**: 89 Addons + Base Modules.
- **Structural Integration**: **100% (89/89)**.
- **Maven Reactor**: 80 Active modules.
- **Gradle Reactor**: 9 Active modules.
- **Identity com.github.drakescraft_labs**: Implemented in core and 60 addons.

## 🧬 Status by Layer

### 1. Core and Libraries (COMPLETED ✅)
- [x] **Dough-Core**: Relocated and stable (`1.3.1-DRAKE`).
- [x] **Slimefun-Core**: Stabilized for 1.21.1 (`11.0-Drake`).
- [x] **InfinityLib / SefiLib**: Aligned with the master reactor.

### 2. Maven Reactor (IN PROGRESS ⚙️)
- [x] **Integration**: All 80 modules are now in the root `pom.xml`.
- [x] **Synchronization**: GroupIDs fixed via `manager.py`.
- [/] **Rebranding**: 60/80 addons are already using the `-drake` suffix. 20 remaining (`STABILIZED`).

### 3. Gradle Reactor (STABILIZED 💎)
- [x] **Master Reactor**: `settings.gradle.kts` configured for 9 addons.
- [x] **Java 21**: Forced across all Gradle projects.
- [x] **Galactifun / SlimefunTranslation**: Operational and stable.

## 🛠️ Operational Tools
- **DrakesLab Manager**: Use `python scripts/manager.py audit` for tracking.
- **Unified Engine**: Check build status in the `Actions` tab.

## 🎯 Next Objectives
1.  **Massive Rebranding**: Move the 20 `STABILIZED` addons to `SURGICAL`.
2.  **Smoke Tests**: Begin load testing on real Paper 1.21.1 servers.
3.  **Public Wiki**: Synchronize this local documentation with the GitHub Wiki.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
