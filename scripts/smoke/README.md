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

### ProtocolLib (GPL-2.0, proyecto ajeno)

**No hace falta “portearlo” en DrakesCraft:** el soporte **1.21.11** ya está en el upstream **[dmulloy2/ProtocolLib](https://github.com/dmulloy2/ProtocolLib)** (p. ej. [#3578](https://github.com/dmulloy2/ProtocolLib/pull/3578), [#3589](https://github.com/dmulloy2/ProtocolLib/pull/3589)). Código nuevo → PR al **repo de dmulloy2**. **DrakesCraft no es autor**; GPL-2.0 en el upstream.

| JVM del servidor | Qué hacer |
|------------------|-----------|
| **Java 25+** | [dev-build `ProtocolLib.jar`](https://github.com/dmulloy2/ProtocolLib/releases/download/dev-build/ProtocolLib.jar) (puede ir con bytecode muy nuevo). |
| **Java 21** | El `dev-build` reciente puede fallar (`UnsupportedClassVersionError`). Compilá el upstream con **Gradle** en commit **`774e679`** (incluye “Mark 1.21.11” y bytecode Java 17): `git clone …` → `git checkout 774e679` → `.\gradlew.bat build -x test` → usá `build/libs/ProtocolLib.jar` en `plugins/`. |

**Smoke:** exportá **`PROTOCOL_LIB_PATH`** (ruta al `ProtocolLib.jar`) o **`PROTOCOL_LIB_URL`**. Por defecto se intentan releases **5.4.0** / **5.3.0** (JVM 21 OK; en 1.21.11 pueden fallar por NMS) y al final **dev-build** (útil si la JVM es 25+).

## Perfiles

- **`foundation`**: Paper + Slimefun core Drake; debe mantenerse verde como mínimo.
- **`monorepo-all`**: conjunto amplio de addons del monorepo; tarda más y exige más RAM/disco.
- Otros perfiles: ver `smoke-profiles.json`.

## Comprobaciones en el log

`run-smoke-server.ps1` falla si detecta patrones de error graves (carga de plugins, excepciones en enable, etc.). Ajustar patrones con cuidado: demasiado laxo oculta regresiones; demasiado estricto genera falsos positivos.

## GitHub Actions

El workflow **Smoke Runtime 1.21** (`.github/workflows/smoke-runtime-121.yml`) está pensado para **ejecución manual** (`workflow_dispatch`), no en cada push, para no saturar Actions.

## Banner de arranque

El servidor de smoke debe mostrar el banner acordado del pack Drake (cadenas listadas en `smoke-profiles.json` por perfil) para confirmar que el JAR correcto está en `plugins/`.
