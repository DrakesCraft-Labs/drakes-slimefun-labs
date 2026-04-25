# Roadmap de Estabilización

## Objetivo

Esta página traduce el inventario del [`README.md`](../../README.md) y la [`PLUGIN_MATRIX.md`](PLUGIN_MATRIX.md) (generada) a una secuencia operativa. La meta no es solo "hacer que compile", sino cerrar el backlog real sin mezclar variantes viejas, sin inflar el reactor y sin dejar estados ambiguos.

## Estado base (auditable)

- Inventario unificado en el reactor: **86 entradas** (Maven + Gradle; ver matriz generada).
- **Listo (CI)**: subconjunto explicito en [`ci-monorepo-121.yml`](../../.github/workflows/ci-monorepo-121.yml) (no es el 100% del monorepo).
- **Listo (local)**: modulos con `mvn compile -am` verificado fuera de gate; pendiente promocion a CI.
- **En curso**: mayoria de modulos Maven con parches `port_paper_121` aplicados pero sin evidencia de build por modulo en CI.
- **Bloqueado (build)**: cuatro addons Gradle con fallos reproducibles (ver `pending-modules.md` y columnas de la matriz).
- Tablero org: [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) — mantener alineado con la matriz ([`PROJECT_BOARD_SYNC.md`](../PROJECT_BOARD_SYNC.md)).

## 🛡️ Fortificación y Seguridad (Especial 2026)

A partir de la intervención de abril 2026, el reactor ha elevado sus requisitos de seguridad:

1. **Shadow Patching**: Implementado para eliminar vulnerabilidades en librerías EOL (Commons Lang v2). ✅
2. **Estándares 2026**: Todas las dependencias críticas (Spring, Guava) deben estar en sus versiones de seguridad de 2026. ✅
3. **Manager Sentinel**: Auditoría activa obligatoria para detectar inyecciones de versiones obsoletas. ✅

## Orden Recomendado

### Fase 1: cerrar lo que ya está dentro del reactor

Esto tiene prioridad máxima porque cualquier fallo aquí afecta el build unificado.

1. `GeneticChickengineering-Reborn`
2. `PotionExpansion`

### Fase 2: incorporar quick wins fuera del reactor

Estos son los candidatos más razonables para seguir sumando cierres rápidos:

1. `MoreResearches`
2. `SfBetterChests`
3. `SlimeHUD`
4. `SmallSpace`
5. `Quaptics`

### Fase 3: candidatos intermedios

Requieren más revisión, pero siguen siendo mejores apuestas que los casos pesados o históricos.

1. `Geyser-Slimefun-Heads`
2. `Gastronomicon`
3. `RelicsOfCthonia`
4. `VillagerTrade`
5. `Wildernether`
6. `WorldEditSlimefun`
7. `CompressionCraft`
8. `EMCTech`
9. `SaneCrafting`
10. `SpiritsUnchained`
11. `Better-Nuclear-Generator`
12. `AdvancedTech`

### Fase 4: triage primero, port después

No conviene entrar aquí sin revisar antes estructura, build y dependencia externa.

- `Galactifun`
- `Bump`
- `CustomItemGenerators`
- `FastMachines`
- `SlimefunTranslation`
- `UltimateGenerators2`
- `Netheopoiesis`
- `SlimeFrame`
- `SlimefunAdvancements`

### Fase 5: variantes históricas o ambiguas

Estos casos no deben mezclarse con las variantes activas sin una decisión explícita.

- `Cultivation`
- `Networks`
- `EMC2`
- `SlimefunWarfare`

## Criterio de Trabajo por Addon

Antes de tocar código:

1. Confirmar si el problema es de `pom.xml`/dependencias o de API/código.
2. Revisar si ya usa `dev.drake.dough.*`.
3. Validar si hereda del parent del reactor.
4. **Ejecutar `python scripts/manager.py security`** para auditar vulnerabilidades. 🛡️
5. Compilar solo el módulo con `-pl` y `-am`.

Comando base:

```powershell
mvn -pl ruta/del/modulo -am -DskipTests package
```

## Criterio de "Listo"

Un addon puede marcarse como listo para `1.21.1` cuando:

- Compila dentro del reactor o en build aislado de forma consistente.
- Ya no depende de coordenadas viejas como `dev.drake:Slimefun:5.0-Drake-1.21.1`.
- **Supera la auditoría de seguridad del Módulo Sentinel**. ✅
- No deja errores activos de API conocidos.
- Su estado quedó reflejado en `README.md` y páginas de seguimiento.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** en verde (jobs curados).
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
