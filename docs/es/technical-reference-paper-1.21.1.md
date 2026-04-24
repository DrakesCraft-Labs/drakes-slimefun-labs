# Referencia Técnica (Paper 1.21.1)

## Nota de alcance

El branch objetivo trabaja hoy sobre `Paper 1.21.11`, pero muchas de las roturas técnicas detectadas durante el port empezaron con el salto a la línea `1.21.x`. Esta página resume ese tipo de cambios.

## Patrones de rotura ya observados

### Dependencias Maven desalineadas

Casos típicos:

- módulos que no heredaban del parent del reactor
- dependencias a `dev.drake:Slimefun:5.0-Drake-1.21.11`
- ausencia de `dev.drake.dough:dough-core`
- dependencia remota a librerías que ya existen dentro del reactor

### APIs legacy de Slimefun o Dough

Casos ya vistos:

- imports antiguos que debían moverse a `dev.drake.dough.*`
- utilidades legacy de addons que ya no existen igual en el stack actual
- firmas viejas que seguían compilando solo en branches históricos

### Cambios de Bukkit/Paper

Casos ya detectados:

- partículas obsoletas como `Particle.REDSTONE` en usos que ya requieren `Particle.DUST`
- warnings o ajustes por API deprecated de Bukkit
- cambios en atributos, serializers y helpers visuales

## Casos Reales del Repo

- `SimpleUtils`: se cerró al alinear `InfinityLib` al reactor actual
- `EcoPower`: el código ya apuntaba al stack nuevo, pero su `pom.xml` no heredaba del parent ni declaraba `dough-core`
- `SlimeTinker`: seguía hardcodeando `Slimefun 5.0-Drake-1.21.11`
- `MissileWarfare`: requirió corregir partículas obsoletas
- `PotionExpansion`: sigue roto por uso de API vieja en código, no solo por Maven

## Orden de diagnóstico recomendado

1. revisar `pom.xml`
2. confirmar parent del reactor
3. confirmar coordenadas de `Slimefun` y `dough-core`
4. recién después revisar errores de API/código

## Regla práctica

Si un addon ya usa `dev.drake.dough.*` pero sigue fallando por imports no resueltos, primero revisar si el problema real es que su `pom.xml` sigue mal alineado.

## Enlaces Relacionados

- [[Dev-Setup]]
- [[Checklist de Migración]]
- [[Roadmap de Estabilización]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
