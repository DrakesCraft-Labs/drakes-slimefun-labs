# Documentación del monorepo

Fuente de verdad para **Drakes Slimefun Labs** en la rama **`26.X-ToTheStars`**: porte hacia **Minecraft / Paper 26.x** (API publicada como `26.1.x.build.*-alpha`, etc.), con **Java 21** y reactor Maven + proyectos Gradle en la raíz.

## Contexto de rama

| Rama | Rol |
|------|-----|
| **`1.21-latin`** | Baseline ** estable**: Paper **1.21.x**, CI **Monorepo 1.21**, smoke runtime 1.21.x / 1.21.11, releases. |
| **`26.X-ToTheStars`** (aquí) | Preparación del salto a **Paper API 26.x**; el `pom.xml` sigue en **1.21.1-R0.1-SNAPSHOT** por defecto y añade el perfil **`paper-26-preview`** para compilar contra 26.x. |

Guía técnica del porte 26.x: **[paper-26-base.md](paper-26-base.md)**.

## Dónde empezar

| Objetivo | Documento |
|----------|-------------|
| Visión general del repo y tabla de módulos | [README raíz](../README.md) (regenerar tabla con `python scripts/generate_plugin_matrix.py` en alineación con `1.21-latin` si aplica) |
| Matriz auditada por módulo (no editar a mano) | [es/PLUGIN_MATRIX.md](es/PLUGIN_MATRIX.md) |
| Arranque local y convenciones | [en/development-setup.md](en/development-setup.md) · [es/development-setup.md](es/development-setup.md) |
| Smoke test (servidor real; hoy calibrado para Paper 1.21.x) | [en/smoke-test-guide.md](en/smoke-test-guide.md) · [es/smoke-test-guide.md](es/smoke-test-guide.md) |
| Scripts Python y PowerShell | [../scripts/README.md](../scripts/README.md) |
| Tablero GitHub Projects (org) | [PROJECT_BOARD_SYNC.md](PROJECT_BOARD_SYNC.md) |
| Limpieza de Actions, PRs y alertas | [github-maintenance.md](github-maintenance.md) |

## Idiomas

- **Inglés**: índice [en/home.md](en/home.md); guías bajo `docs/en/` (cada archivo lleva aviso de rama 26.x al inicio).
- **Español**: índice [es/home.md](es/home.md); guías bajo `docs/es/`.

Los dos idiomas cubren los mismos temas; en esta rama, cualquier referencia a **1.21.1** como “versión actual del servidor” debe interpretarse como **origen del porte** o como estado de **`1.21-latin`**, salvo que el texto indique explícitamente **26.x**.

## Qué queda fuera de esta carpeta

- `sources/**/README.md` y demás `.md` bajo `sources/`: documentación por addon o heredada; en **`26.X-ToTheStars`** llevan un **aviso de rama** al inicio; el cuerpo puede seguir describiendo 1.21.x hasta portar cada proyecto.
- Wiki de GitHub (si existe): debe alinearse con `docs/en/`; este árbol es la referencia editable en git.

## Compilación (addons y Slimefun sombreado)

En el reactor, el `package` de **Slimefun** sombrea Dough bajo `com.github.drakescraft_labs.slimefun4.libraries.dough.protection`. Los módulos que dependen de Slimefun compilan **después** de ese paso, así que las llamadas a `Slimefun.getProtectionManager()` deben usar esos tipos relocados, no `dev.drake.dough.protection` (salvo en `slimefun-core` y `dough-core`). Un `mvn compile` aislado puede dar una imagen distinta del classpath; la verificación alineada con CI en 1.21.x es `mvn package`. Ajuste masivo: `python scripts/fix_dough_compilation_imports.py` (ver [scripts/README.md](../scripts/README.md)).

Para **Paper 26.x API** en esta rama:

```bash
mvn -B -DskipTests -Ppaper-26-preview compile -fae
```

## Estado operativo (referencia rápida)

- Rama de trabajo **estable y releases**: `1.21-latin`.
- Rama de trabajo **porte 26.x** (esta documentación en contexto): `26.X-ToTheStars`.
- CI principal heredado del monorepo 1.21: **CI Monorepo 1.21** (`.github/workflows/ci-monorepo-121.yml`) — revisar si aplica tal cual en 26.x o si se duplica con jobs nuevos.
- Smoke opcional: **Smoke Runtime 1.21** (manual, `workflow_dispatch`) — pensado para servidor **1.21.x**; smoke dedicado 26.x es trabajo pendiente.

Para fechas y cortes históricos, usa Issues o el Project de la org; evita duplicar “handoffs” largos en markdown sueltos.
