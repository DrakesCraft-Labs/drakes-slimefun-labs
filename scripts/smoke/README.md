# Smoke runtime (Paper 1.21.x)

Objetivo: comprobar que los JAR no solo compilan, sino que **cargan** en un servidor **Paper 1.21.1 / 1.21.11** real con el stack configurado. Complementa al CI: el “mundo real” largo sigue en **[DrakesCraft](https://drakescraft.cl)** (Chile), con la comunidad y **Chagui**.

## Archivos

| Archivo | Rol |
|---------|-----|
| `smoke-profiles.json` | Lista de módulos y plugins por perfil (`foundation`, `core-addons`, `monorepo-all`, etc.). |
| `build-smoke-artifacts.ps1` | Empaqueta con Maven los módulos del perfil y copia JAR a `.smoke/<perfil>/artifacts/plugins`. |
| `run-smoke-server.ps1` | Descarga Paper si falta, arranca servidor temporal, valida el log y apaga con `stop`. Opcional **`-ServerJarPath`** (Purpur/Paper local) evita la descarga. |
| `fetch_smoke_optional_deps.py` | Descarga dependencias opcionales (p. ej. ProtocolLib) para addons que las declaran duras. |
| `smoke_orchestrate.py` | Orquesta Maven + build de artifacts + run desde la raíz del repo. |

## Orquestador Python (recomendado)

Desde la raíz del repositorio:

```bash
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
```

Opciones útiles:

- `--skip-mvn` — no ejecutar `mvn package` completo del reactor (útil si ya compilaste).
- `--skip-build-artifacts` — reusar `.smoke/<perfil>/artifacts` existentes.
- `mvn-package-pl --pl sources/community-addons/MiAddon,sources/community-addons/Otro` — compilar solo módulos listados.

## PowerShell directo

```powershell
pwsh -NoProfile -File .\scripts\smoke\build-smoke-artifacts.ps1 -Profile foundation -Clean
pwsh -NoProfile -File .\scripts\smoke\run-smoke-server.ps1 -Profile foundation -Clean -TimeoutSeconds 120
```

**Purpur (u otro fork) local** — mismo script, sin subir el `.jar` al repo:

```powershell
pwsh -NoProfile -File .\scripts\smoke\run-smoke-server.ps1 `
  -Profile foundation-paper-12111 -MinecraftVersion 1.21.11 `
  -ServerJarPath "C:\Users\pablo\Downloads\purpur-1.21.11-2568.jar" `
  -NoBuild -Clean -TimeoutSeconds 180
```

O con el orquestador: `python scripts/smoke/smoke_orchestrate.py run-server --profile foundation --server-jar "C:\...\purpur-....jar" --minecraft 1.21.11 --no-build --clean --timeout 180`

**Purpur 1.21.11 + ProtocolLib:** el smoke sigue descargando ProtocolLib 5.3.0 para addons que lo piden; en **1.21.11** ese build puede **fallar al cargar** (NMS aún no alineado). Slimefun **foundation** no lo necesita para pasar el smoke. Para servidor real con addons que sí requieran ProtocolLib, usa un **build más nuevo** del plugin o quita `ProtocolLib.jar` de `plugins/` hasta tener uno compatible.

## Perfiles

- **`foundation`**: Paper + Slimefun core Drake; debe mantenerse verde como mínimo.
- **`monorepo-all`**: conjunto amplio de addons del monorepo; tarda más y exige más RAM/disco.
- Otros perfiles: ver `smoke-profiles.json`.

## Comprobaciones en el log

`run-smoke-server.ps1` falla si detecta patrones de error graves (carga de plugins, excepciones en enable, etc.). Ajustar patrones con cuidado: demasiado laxo oculta regresiones; demasiado estricto genera falsos positivos.

## GitHub Actions

El workflow **Smoke Runtime 1.21** (`.github/workflows/smoke-runtime-121.yml`) está pensado para **ejecución manual** (`workflow_dispatch`), no en cada push, para no saturar Actions.

## Banner de arranque

El servidor de smoke debe mostrar el banner acordado (JackStar / DrakesCraft / Chagui68) para confirmar que el JAR Drake correcto está en `plugins/`.
