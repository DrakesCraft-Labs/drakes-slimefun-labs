<!-- drakes-labs:branch-26x-notice -->

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

# Sincronizar GitHub Projects con la matriz

**Tablero:** [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1)

**Datos auditados:** [docs/es/PLUGIN_MATRIX.md](es/PLUGIN_MATRIX.md) (generado; no editar a mano). Mientras el porte **26.x** avanza, la matriz sigue reflejando sobre todo el estado **1.21-latin** hasta que se regenere con criterios **26.x**. Regenerar README y matriz:

```bash
python scripts/generate_plugin_matrix.py
```

## CLI `gh` y permisos

Para leer o mutar Projects v2 hace falta refrescar scopes:

```bash
gh auth refresh -h github.com -s read:project,project
```

Sin eso, los subcomandos `gh project` fallan por permisos.

## Proceso recomendado

1. Regenerar la matriz (comando anterior).
2. Abrir el Project 1 en el navegador.
3. Por cada módulo del monorepo: alinear la columna de estado con la columna **Estado** de `PLUGIN_MATRIX.md` y copiar o resumir **Observaciones**.
4. Añadir tarjetas para módulos nuevos en `sources/...` que aún no estén en el tablero.

## Sugerencia de mapeo

| Estado en matriz | Columna típica en el tablero |
|------------------|------------------------------|
| Listo (CI) | Done / Ready |
| Listo (local) | In progress (falta validar en CI) |
| En curso | Todo o In progress |
| Bloqueado (build) | Blocked |

Ajusta los nombres a las columnas reales del Project 1.
