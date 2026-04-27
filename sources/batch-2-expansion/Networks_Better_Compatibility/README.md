# 🌐 Networks - Slimefun 6 (Drake Framework Port)
### *The Ultimate Item Storage Solution for 1.21.11*

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.11-brightgreen.svg)](https://www.spigotmc.org/)
[![Slimefun](https://img.shields.io/badge/Slimefun-6.0--Drake-blue.svg)](https://github.com/Slimefun/Slimefun4)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com/)

---

## 🤝 Créditos y Autoría
- **Autor Original**: [Sefiraat](https://github.com/Sefiraat)
- **Mantenedor Base**: [Chagui68](https://github.com/Chagui68)
- **Port a 1.21.11 (fork Drake):** [DrakesCraft-Labs](https://github.com/DrakesCraft-Labs)

---

> [!IMPORTANT]
> Este es el port oficial del Drake Framework para **Slimefun 6**.
> Hemos unificado el trabajo de compatibilidad previo de Chagui68 con las optimizaciones de nivel core del motor de Drake.

---

Networks is a Slimefun4 addon that brings a simple yet powerful item storage and movement network that works along side cargo.

## 🚀 Key Fixes in this Version
- **Full 1.20.6 Support**: Resolved all compilation errors related to modern Minecraft enchantment and particle renames.
- **Robust Initialization**: Fixed the `NullPointerException` and `ExceptionInInitializerError` by refactoring the static initialization order.
- **Improved Stability**: Removed circular dependencies between core Slimefun items.

---

## 📋 Requirements

### Server
- **Minecraft**: 1.20.6
- **Server Software**: Spigot or Paper 1.20.6
- **Java**: 21 or higher
- **Slimefun4**: RC-37 or newer

---

## 📦 Installation

1. **Install Slimefun4**
   - Download from [Slimefun4 Builds](https://blob.build/project/Slimefun4)
   - Place in `/plugins/` folder

2. **Install Networks**
   - Download the latest `.jar` from this repository.
   - Place in `/plugins/` folder

3. **Restart Server**

---

## 📖 About Networks

You can find a fuller guide to Networks including all items and blocks in the original [Documentation Pages](https://sefiraat.dev)

![](https://github.com/Sefiraat/Networks/blob/master/images/wiki/setup.png?raw=true)

### Network Grid / Crafting Grid
It can access every single item in the network and display it to you on a single GUI. Items can be inserted directly through this grid and a special crafting grid can craft both vanilla AND slimefun items using ingredients directly from the network.

![](https://github.com/Sefiraat/Networks/blob/master/images/wiki/grid.png?raw=true)

### Network Cells & Quantum Storage
- **Network Cells**: Single block holding a double-chests worth of items.
- **Network Quantum Storage**: Massive storage for a single item type, upgradable from 4k to 2 billion items.

### Autocrafting
The Network Autocrafter can take recipes encoded by the Network Encoder and craft them periodically using items and power directly from the network.

---

## 🛠️ Development

### Building from Source
```bash
git clone https://github.com/Chagui68/Networks_Better_Compatibility.git
cd Networks_Better_Compatibility
mvn clean package
```

---

## 🙏 Credits & Original Authors
- **Sefiraat** - Original creator and lead developer.
- Special thanks to Boomer, Cai, and Lucky for testing.
- Shoutout to the community members who supported the original project.

---

## 📄 License
This project is licensed under the **GPL-3.0 License** - see the original project for full details.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
