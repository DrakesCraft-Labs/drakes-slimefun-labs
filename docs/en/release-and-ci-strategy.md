# 🚀 Release and CI Strategy: DrakesLab Standard

## Goal
Define a clear policy for artifact publication and automation in `drakes-slimefun-labs`, leveraging the **Unified Engine** to maintain high-quality standards across the 89-addon ecosystem.

## 🏗️ Unified CI Architecture
As of v15.0, we have consolidated all automation into a single, high-performance engine: **DrakesLab Unified Engine** (`unified-engine.yml`).

### Core Capabilities:
- **Parallel Processing**: Builds both Maven and Gradle reactors simultaneously.
- **Ecosystem Audit**: Automatically runs `manager.py audit` to track migration progress.
- **Strict Quality**: Forces **Java 21** and **Paper 1.21.1** standards.
- **Smart Caching**: Aggressive dependency caching for sub-3-minute full builds.

## 📦 Deployment Policy

### Automated Deployment (Continuous Delivery)
The **Unified Engine** automatically deploys the following critical modules to **GitHub Packages** upon every successful push to `1.21-latin`:
- `com.github.drakescraft-labs:dough-core`
- `com.github.drakescraft-labs:slimefun-core`
- `com.github.drakescraft-labs:sefilib-core`
- `com.github.drakescraft-labs:infinitylib-core`

### Manual Release Candidates
Individual addons are not automatically released as stable versions. Instead:
1. **Workflow Artifacts**: Every build generates downloadable `.jar` files for all 89 addons.
2. **Smoke Testing**: Developers must manually validate artifacts in a live environment.
3. **Draft Releases**: Stable batches or high-priority addons (like `InfinityExpansion` or `DynaTech`) are published as manual releases after runtime confirmation.

## 🛠️ Recommended Developer Workflow
1. **Local Validation**: Run `python scripts/manager.py` to sync identities.
2. **Isolated Build**: Use `mvn -pl <path> -am clean install` for fast local testing.
3. **CI Validation**: Push to `1.21-latin` and monitor the **Unified Engine** for regressions.

## ✅ Quality Gate Standards
A module is considered "Surgical Ready" and eligible for stable distribution when:
- It successfully passes the **Unified Engine** build.
- It uses the `com.github.drakescraft-labs` unified identity.
- No legacy API warnings or dependency conflicts are present.
- It has been smoke-tested in a real Minecraft server environment.

---
**Maintained by DrakesLab Core Team.**
