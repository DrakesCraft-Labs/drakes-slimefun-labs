# Guía de Smoke Test

## Objetivo

El smoke test sirve para comprobar que el stack base y los addons integrados no solo compilan, sino que también cargan de forma razonable en runtime.

## Cuándo usarlo

- después de varios cierres seguidos
- cuando se tocó el core o `dough-core`
- cuando un addon sensible compila pero conviene validar carga real
- antes de marcar un bloque grande del roadmap como estabilizado

## Script disponible

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\slimefun\smoke-test.ps1
```

## Qué debería validar como mínimo

- que Paper/Purpur `1.21.11` inicie sin romper el stack base
- que `Slimefun` cargue correctamente
- que los addons recién cerrados no tiren errores de enable
- que integraciones opcionales críticas no revienten al cargar

## Addons que conviene mirar con más atención

- `MapJammers`
- `MissileWarfare`
- `SoundMuffler`
- `Simple-Storage`
- `SlimeCustomizer`
- `RykenSlimeCustomizer-EN`
- `Element-Manipulation`
- `HeadLimiter` si se usa `Towny`

## Qué registrar si falla

Si el smoke test encuentra un problema, dejar documentado:

- addon afectado
- error exacto
- si el fallo es de carga, evento, receta o integración externa
- si sigue compilando pero no es seguro marcarlo como listo

## Relación con el README

El `README.md` puede marcar un addon como listo por build validado. Aun así, el smoke test es la capa que confirma si conviene dejar observaciones especiales de runtime.

## Enlaces Útiles

- [[Checklist de Migración]]
- [[Roadmap de Estabilización]]
- [[Tomorrow-Handoff]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
