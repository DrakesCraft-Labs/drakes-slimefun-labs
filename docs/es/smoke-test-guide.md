# Guia de Smoke Test Runtime

## Objetivo

El smoke test runtime valida que los jars Drake no solo compilan, sino que cargan en un servidor Paper real. Es la prueba minima antes de declarar estable un corte grande del ecosistema.

## Base disponible

La base vive en `scripts/smoke/`:

- `smoke-profiles.json`: define que modulos se empaquetan y que marcadores de log deben aparecer.
- `build-smoke-artifacts.ps1`: compila/empaca los modulos del perfil y copia jars a `.smoke/<perfil>/artifacts/plugins`.
- `run-smoke-server.ps1`: descarga Paper `1.21.1`, prepara un servidor temporal, copia plugins, arranca, espera `Done`, apaga con `stop` y valida logs.
- `README.md`: referencia rapida de uso local y GitHub Actions.

## Perfiles

- `foundation`: Paper `1.21.1` + `Slimefun` core Drake. Debe estar verde siempre.
- `core-addons`: `foundation` + addons Maven pequenos para ampliar cobertura runtime cuando el core ya esta estable.

## Uso local

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\run-smoke-server.ps1 -Profile foundation -Clean -TimeoutSeconds 120
```

Para solo preparar jars:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\build-smoke-artifacts.ps1 -Profile foundation -Clean
```

## Smoke en GitHub

El workflow manual `Smoke Runtime 1.21` ejecuta el mismo runner en GitHub Actions. Se dispara desde `workflow_dispatch` para evitar ruido en cada push.

## Banner de verificacion

El arranque de `Slimefun` imprime un banner verde de DrakesCraft con:

- `JACKSTAR`
- `DRAKESCRAFT`
- `CHAGUI68`
- enlace al repo
- enlace al perfil de JackStar

El smoke falla si esos marcadores no aparecen. Esto confirma que el servidor cargo un jar Drake actual y no un artifact viejo.

## Criterio de exito

- Paper llega a `Done`.
- No hay errores de carga de jars.
- No hay `Error occurred while enabling`.
- No hay `NoClassDefFoundError` ni `ClassNotFoundException`.
- Aparecen los marcadores del banner.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: existe base runtime smoke local y workflow manual `Smoke Runtime 1.21`.
<!-- DRAKES-STATUS:END -->
