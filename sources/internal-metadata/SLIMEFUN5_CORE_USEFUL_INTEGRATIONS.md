# Slimefun5 core -> Drake: integraciones utiles detectadas

Fecha de auditoria: 2026-04-28

## Cambios utiles encontrados en core Slimefun5 (stable vs experimental)

- `default-addons.txt` (nuevo en core): lista oficial de addons recomendados por defecto.
- Ajustes de CI/workflows y docs (`release.yml`, `README`, `CONTRIBUTING`, `ADR storage-layer`).
- Sin cambios directos de runtime Java en `src/main` en ese diff puntual de ramas.

## Lista oficial de default addons (Slimefun5)

- `Slimefun5/ExtraGear`
- `Slimefun5/LuckyBlocks`
- `Slimefun5/ChestTerminal`
- `Slimefun5/SlimefunAdvancements`
- `Slimefun5/ExoticGarden`
- `Slimefun5/MissileWarfare`
- `Slimefun5/LiteXpansion`
- `Slimefun5/FluffyMachines`
- `Slimefun5/InfinityExpansion`
- `Slimefun5/DynaTech`
- `Slimefun5/SensibleToolbox`
- `Slimefun5/Galactifun`
- `Slimefun5/SlimeTinker`

## Mapeo rapido a Drake (estado)

- Integrados en Drake: ExtraGear, LuckyBlocks (linea `luckyblocks-sf`), SlimefunAdvancements, ExoticGarden, MissileWarfare, LiteXpansion, FluffyMachines, InfinityExpansion, DynaTech, SlimeTinker.
- Integrados al reactor Drake en este lote: ChestTerminal-sf5, SensibleToolbox-sf5.
- Caso especial: Galactifun (Drake usa lineas propias `Galactifun2` + batch interno).
