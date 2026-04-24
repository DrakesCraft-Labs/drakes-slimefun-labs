# Auditoría General de Addons de Slimefun

Este reporte clasifica todos los addons actuales mencionados en la [Wiki Oficial de Slimefun](https://github.com/Slimefun/Slimefun4/wiki/Addons) en relación a su estado operativo en `DrakesVanillaSlimefun+`.

## 🟩 Listos para Subir al Servidor (Plugins Confirmados)
Estos `jars` ya están en el stack inicial (`deploy/purpur-1.20.6/plugins-confirmed/...`) o parcheados localmente, garantizados para `Purpur 1.20.6`.

**Oficiales:**
- ColoredEnderChests
- DyedBackpacks
- EcoPower
- ElectricSpawners
- ExtraGear
- ExtraHeads
- HardcoreSlimefun
- HotbarPets
- LiteXpansion
- MobCapturer
- PrivateStorage
- SFMobDrops
- SlimefunOreChunks
- SoulJars

**Comunitarios:**
- AdvancedTech
- Cultivation
- DynaTech
- Galactifun
- InfinityExpansion _(Parche local para dupe issue #126)_
- Networks

---

## 🟨 Pendientes / En Construcción (En la fila de espera)
Los repositorios de estos addons se encuentran descargados en `sources/plugins-in-construction/slimefun-addons/repos-to-port` y aguardan un fix manual, parcheo o verificación de compatibilidad.

**Oficiales:**
- ExoticGarden
- luckyblocks-sf
- SlimyTreeTaps

**Comunitarios:**
- FluffyMachines
- GlobalWarming
- SFCalc
- SimpleUtils
- SlimeChem
- SlimyRepair
- SoundMuffler

---

## ⬛ Lista Negra (Descartados / Uso Peligroso)
Estos repositorios se encuentran formalmente descartados por inestabilidad, bypass de protecciones, dupes conocidos o rendimiento perjudicial. **No irán a producción.**

- **SlimefunWarfare:** Riesgo permanente de bypass de claims y abusos con explosivos, armas y daño colateral.
- **EMCTech:** Ingresado a la lista negra (sugerido por la política operativa y técnica).

_Nota: Estos están documentados a fondo en `addons-rechazados.md`._

---

## ⬜ Faltan / No están en nuestro radar
Esta es la lista restante de addons comunitarios que existen en la Wiki pero no están priorizados todavía. Se recomienda auditarlos uno por uno antes de tomar la decisión de portarlos, ya que muchos pueden estar sin mantenimiento.

**Comunitarios (Requieren auditoría si se van a usar):**
- AlchimiaVitae
- Better-Nuclear-Generator
- Bump
- CompressionCraft
- CrystamaeHistoria
- CustomItemGenerators
- DankTech2
- Dracfun
- DyeBench
- Element-Manipulation
- ExtraTools
- FastMachines
- FlowerPower
- FoxyMachines
- FN-FAL's Amplifications
- Gastronomicon
- GeneticChickengineering-Reborn
- HeadLimiter
- IDreamOfEasy
- Liquid
- Magic-8-Ball
- MapJammers
- MiniBlocks
- MissileWarfare
- MoreResearches
- Netheopoiesis
- PotionExpansion
- Quaptics
- RelicsOfCthonia
- RykenSlimeCustomizer-EN
- SaneCrafting
- SfBetterChests
- SfChunkInfo
- Simple-Storage
- SlimeCustomizer
- SlimeFrame
- SlimeHUD
- SlimeTinker
- SlimeVision
- SlimefunAdvancements
- SlimefunTranslation
- SmallSpace
- SMG
- SpiritsUnchained
- Supreme
- TranscEndence
- UltimateGenerators2
- VillagerTrade
- VillagerUtil
- Wildernether
- WorldEditSlimefun

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
