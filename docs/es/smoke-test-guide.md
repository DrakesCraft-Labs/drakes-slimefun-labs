# Guia de Smoke Test Runtime

Indice de documentacion: [`docs/README.md`](../README.md).

## Objetivo

El smoke test runtime valida que los jars Drake no solo compilan, sino que cargan en un servidor Paper real. Es la prueba minima antes de declarar estable un corte grande del ecosistema.

## Base disponible

La base vive en `scripts/smoke/`:

- `smoke-profiles.json`: define que modulos se empaquetan y que marcadores de log deben aparecer.
- `build-smoke-artifacts.ps1`: compila/empaca los modulos del perfil y copia jars a `.smoke/<perfil>/artifacts/plugins`.
- `run-smoke-server.ps1`: descarga Paper `1.21.1`, prepara un servidor temporal, copia plugins, arranca, espera `Done`, apaga con `stop` y valida logs.
- `smoke_orchestrate.py`: desde la raiz del repo encadena Maven (`full`, `mvn-package`, `mvn-package-pl`), build de artifacts y ejecucion del servidor.
- `README.md`: detalle operativo de la carpeta smoke.

## Perfiles

- `foundation`: Paper `1.21.1` + `Slimefun` core Drake. Debe estar verde siempre.
- `core-addons`: `foundation` + addons Maven pequenos para ampliar cobertura runtime cuando el core ya esta estable.
- `monorepo-all`: conjunto amplio; lento y exigente en RAM/disco. Tras un `mvn package` completo o con `full --skip-mvn` si los jars ya estan construidos.

## Uso local (orquestador)

Desde la raiz del repositorio:

```bash
python scripts/smoke/smoke_orchestrate.py full --profile foundation --clean --timeout 120
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
```

## Uso local (solo PowerShell)

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\run-smoke-server.ps1 -Profile foundation -Clean -TimeoutSeconds 120
```

Para solo preparar jars:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke\build-smoke-artifacts.ps1 -Profile foundation -Clean
```

## Smoke en GitHub

El workflow manual `Smoke Runtime 1.21` ejecuta el mismo runner en GitHub Actions. Se dispara desde `workflow_dispatch` para evitar ruido en cada push.

## Banner de verificación

El arranque de `Slimefun` imprime un banner de DrakesCraft Labs. El smoke comprueba que el log contenga las **cadenas de verificación** de `scripts/smoke/smoke-profiles.json` (marcador de smoke, línea del laboratorio y ruta del repo en GitHub).

Si falta alguna cadena esperada, el smoke falla: así se confirma que el servidor cargó un JAR Drake actual y no un artefacto antiguo o genérico.

## Criterio de exito

- Paper llega a `Done`.
- El log no debe coincidir con la lista de patrones fatales de `run-smoke-server.ps1` (incluye `Error occurred while enabling`, `Could not load plugin`, `NoClassDefFoundError`, `[SEVERE]` y otros de carga/excepcion).
- Aparecen los marcadores del banner.

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: existe base runtime smoke local y workflow manual `Smoke Runtime 1.21`.
<!-- DRAKES-STATUS:END -->
