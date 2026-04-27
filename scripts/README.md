# Scripts del monorepo

Automatización para **Drakes Slimefun Labs**: porteo Paper 1.21.1, coherencia Maven/Gradle, matrices y smoke runtime.

## Requisitos

- **Python 3.10+** para los scripts listados aquí.
- **Maven 3.9+** y **JDK 21** en PATH para builds.
- **PowerShell** (Windows) o **pwsh** para `scripts/smoke/*.ps1`.

## Documentación relacionada

| Contenido | Ruta |
|-----------|------|
| Índice general del repo | [../docs/README.md](../docs/README.md) |
| Smoke (perfiles, Paper, logs) | [smoke/README.md](smoke/README.md) |
| Guía smoke EN/ES | [../docs/en/smoke-test-guide.md](../docs/en/smoke-test-guide.md) · [../docs/es/smoke-test-guide.md](../docs/es/smoke-test-guide.md) |
| Actions y limpieza en GitHub | [../docs/github-maintenance.md](../docs/github-maintenance.md) |
| Wiki (updater, Aircraft YAML, despliegue) | [../docs/wiki/README.md](../docs/wiki/README.md) |

## Herramientas principales

### `inject_drakes_autoupdate.py`

Añade la dependencia Maven `drakes-labs-autoupdate` y la llamada `DrakesLabsReleaseUpdate.schedule(this, "<artifactId>")` en el `onEnable`/`enable` de los módulos plugin del reactor (según `plugin.yml` y clase principal). Útil al portar addons nuevos al monorepo.

```bash
python scripts/inject_drakes_autoupdate.py --dry-run
python scripts/inject_drakes_autoupdate.py
```

Detalle de runtime (GitHub `releases/latest`, carpeta `updates/`, desactivación): [docs/wiki/runtime-drakes-autoupdate.md](../docs/wiki/runtime-drakes-autoupdate.md).

### `release/collect_monorepo_jars.py`

Tras `mvn package` en la raíz, copia **un jar sombreado por módulo** Maven a un directorio plano (p. ej. `dist/monorepo-jars`) y escribe `manifest.json`. Lo usa el workflow **Release monorepo JARs** antes de subir assets.

```bash
python scripts/release/collect_monorepo_jars.py --out dist/monorepo-jars
```

### `generate_plugin_matrix.py`

Regenera `docs/es/PLUGIN_MATRIX.md` y la tabla larga del `README.md` raíz (incluye columna **Updater GH** por dependencia `drakes-labs-autoupdate`). Ejecutarlo siempre que cambie el inventario de módulos en `pom.xml` / `settings.gradle.kts` o el mapeo en CI.

```bash
python scripts/generate_plugin_matrix.py
```

### `manager.py`

Orquestación de mantenimiento del reactor (POMs, imports, sombras, auditoría). Revisar siempre el diff antes de fusionar cambios masivos.

```bash
python scripts/manager.py audit
python scripts/manager.py repair
python scripts/manager.py rebrand-imports
```

Los subcomandos exactos y backups `.bak` están descritos en el propio script.

### `fix_dough_compilation_imports.py`

Los addons que usan `Slimefun.getProtectionManager()` deben alinear tipos con el
jar **sombreado** de Slimefun (`com.github.drakescraft_labs.slimefun4.libraries.dough.protection.*`),
porque el `compile` del addon ocurre **despues** del `package` de Slimefun en el reactor.
El script reemplaza `dev.drake.dough.protection.Interaction` y el import
`import dev.drake.dough.protection.ProtectionManager` (cuando se asigna desde Slimefun)
bajo `sources/`, excluyendo `slimefun-core`, `dough-core`, `batch-2-expansion/SefiLib` e `InfinityLib` (compilan contra dough sin Slimefun sombreado).

```bash
python scripts/fix_dough_compilation_imports.py
```

### `fix_graalvm_js_community_poms.py`

Sustituye `org.graalvm.js` + `artifactId js` (metapaquete Enterprise, arrastra `truffle-enterprise`) por `js-community` en todos los `pom.xml` bajo `sources/` y el POM raíz. Validación XML antes de escribir.

```bash
python scripts/fix_graalvm_js_community_poms.py
python scripts/fix_graalvm_js_community_poms.py --audit
```

### `ci_hygiene_fixes.py`

Orquesta los dos scripts anteriores (GraalVM POM + imports Dough) para una pasada rápida antes de un `mvn package` o un release.

```bash
python scripts/ci_hygiene_fixes.py
```

### `port_paper_121.py`

Parches repetibles de API Paper 1.21.1; usar con `--dry-run` primero.

## Smoke y orquestación

- Perfiles: `scripts/smoke/smoke-profiles.json`.
- Orquestador Python: `python scripts/smoke/smoke_orchestrate.py --help` (subcomandos `full`, `mvn-package`, `mvn-package-pl`, `build-artifacts`, `run-server`, `parse-log`).
- Detalle operativo: [smoke/README.md](smoke/README.md).

Ejemplo de verificación pesada local (después de compilar):

```bash
python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
```

## Ámbito de los README bajo `sources/`

Los `README.md` dentro de `sources/repos-to-port/` u otros árboles de addon documentan ese proyecto concreto o son herencia upstream. No forman el manual central del laboratorio; el manual central vive en `docs/` y en este archivo.

## Después del “build verde”

El monorepo en **`1.21-latin`** apunta a **CI + smoke + release** (muchos JAR como assets de un solo GitHub Release cuando el equipo dispara el workflow). Lo que sigue es **gameplay** en servidores reales; el survival de referencia del equipo es **[DrakesCraft](https://drakescraft.cl)** (Chile), donde **Chagui** y la comunidad van encontrando detalle addon por addon.

Para probar un jar concreto sin pasar por `updates/`, muchos hosts permiten **sustituir solo el `.jar` dentro de `plugins/`** conservando la carpeta de datos del plugin; ver [docs/wiki/runtime-drakes-autoupdate.md](../docs/wiki/runtime-drakes-autoupdate.md).
