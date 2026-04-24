# SlimyRepair
This Slimefun4 addon makes it easier to repair Slimefun tools and weapons!

To add a Slimefun weapon/tool to the repair list, simply follow the format provided in the example [repairs.yml](https://github.com/NCBPFluffyBear/SlimyRepair/blob/master/src/main/resources/repairs.yml) file. This file can be located locally at `\<YOUR_SERVER_LOCATION>\plugins\SlimyRepair\repairs.yml`

### Line-by-line description:
```yml
<Name of the Slimefun Item by ID>:
  material: <Material to repair the item. Accepts both vanilla (use the material name) and Slimefun (use the ID) items.>
  material-type: <Either VANILLA or SLIMEFUN>
  repair-amount: <The durability this item restores per repair>
```

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
