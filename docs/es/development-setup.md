# Dev-Setup

## Objetivo

Este proyecto no es un plugin suelto, sino un laboratorio Maven unificado. El entorno debe prepararse para trabajar por módulo y no a base de compilar todo el reactor.

## Requisitos

- `Java 21`
- `Maven 3.9+`
- `Git`
- entorno Paper o Purpur `1.21.11` para smoke tests

## Rama de Trabajo

La rama operativa de referencia es:

```powershell
git checkout 1.21-latin
git pull origin 1.21-latin
```

## Verificar que estás en la raíz correcta

Antes de compilar, conviene confirmar que el comando corre desde la raíz del repo:

```powershell
git rev-parse --show-toplevel
```

Si Maven corre desde una subcarpeta, puede dejar fuera módulos del reactor y dar errores engañosos.

## Patrón de Build Recomendado

No compilar el reactor entero salvo necesidad estricta.

Usar:

```powershell
mvn -pl ruta/del/modulo -am -DskipTests package
```

Ejemplos:

```powershell
mvn -pl sources/repos-to-port/SimpleUtils -am -DskipTests package
mvn -pl sources/repos-to-port/DynaTech -am -DskipTests package
mvn -pl sources/community-addons/MapJammers -am -DskipTests compile
```

## Qué significan las flags

- `-pl`: elige el módulo que quieres trabajar
- `-am`: compila también sus dependencias dentro del reactor
- `-DskipTests`: acelera ciclos de validación

## Reglas Técnicas del Workspace

- `dough-core` usa el grupo `dev.drake.dough`
- el core usa `dev.drake:Slimefun:6.0-Drake-1.21.11`
- el `pom.xml` raíz es el punto de verdad para módulos y versiones
- si un addon ya usa `dev.drake.dough.*` pero falla por imports, revisar primero su `pom.xml`

## Qué revisar cuando algo no compila

1. si hereda del parent del reactor
2. si la dependencia `Slimefun` apunta a coordenadas viejas
3. si falta dependencia local como `dough-core` o `InfinityLib`
4. si el error real es de API y no de Maven

## Runtime

Para una validación rápida de servidor:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\slimefun\smoke-test.ps1
```

## Enlaces Relacionados

- [[Guía de Smoke Test]]
- [[Checklist de Migración]]
- [[Roadmap de Estabilización]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
