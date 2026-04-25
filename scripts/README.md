<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Scripts del monorepo

Automatización para **Drakes Slimefun Labs** en **`26.X-ToTheStars`**: porteo desde **Paper 1.21.x** hacia **Paper API 26.x** (`-Ppaper-26-preview`), coherencia Maven/Gradle, matrices y smoke runtime (hoy calibrado para **1.21.x** en servidor).

## Requisitos

- **Python 3.10+** para los scripts listados aquí.
- **Maven 3.9+** y **JDK 21** en PATH para builds.
- **PowerShell** (Windows) o **pwsh** para `scripts/smoke/*.ps1`.

## Documentación relacionada

| Contenido | Ruta |
|-----------|------|
| Índice general del repo | [../docs/README.md](../docs/README.md) · [Paper 26.x](../docs/paper-26-base.md) |
| Smoke (perfiles, Paper, logs) | [smoke/README.md](smoke/README.md) |
| Guía smoke EN/ES | [../docs/en/smoke-test-guide.md](../docs/en/smoke-test-guide.md) · [../docs/es/smoke-test-guide.md](../docs/es/smoke-test-guide.md) |
| Actions y limpieza en GitHub | [../docs/github-maintenance.md](../docs/github-maintenance.md) |

## Herramientas principales

### `generate_plugin_matrix.py`

Regenera `docs/es/PLUGIN_MATRIX.md` y la tabla larga del `README.md` raíz. Ejecutarlo siempre que cambie el inventario de módulos en `pom.xml` / `settings.gradle.kts` o el mapeo en CI.

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
bajo `sources/`, excluyendo `slimefun-core` y `dough-core`.

```bash
python scripts/fix_dough_compilation_imports.py
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

En la rama **`26.X-ToTheStars`**, la mayoría de los `.md` bajo el repo (incluidos muchos `sources/**`) llevan un **aviso de rama** al inicio, generado/actualizado con:

```bash
python scripts/apply_26x_branch_notice_to_markdown.py
```

(excluye `README.md` raíz, `docs/README.md` y `docs/paper-26-base.md`, que se editan a mano).
