# Sincronizar el tablero GitHub Projects (org DrakesCraft-Labs)

Tablero oficial: [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1)

La matriz de estado auditada del codigo vive en:

- [`docs/es/PLUGIN_MATRIX.md`](es/PLUGIN_MATRIX.md) (generada; ver script abajo)

## Por que no se actualizo desde la CLI aqui

La CLI `gh` requiere scopes adicionales para leer o editar Projects v2:

```bash
gh auth refresh -h github.com -s read:project,project
```

Sin esos permisos, `gh project` devuelve error de scopes.

## Checklist manual recomendado (por cada release de docs)

1. Regenerar la matriz desde el repo:

   ```bash
   python scripts/generate_plugin_matrix.py
   ```

2. Abrir el [Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) en el navegador.

3. Para cada item del tablero que corresponda a un modulo del monorepo, alinear:

   - **Columna / estado** con la columna `Estado` de `PLUGIN_MATRIX.md` (Listo CI, Listo local, En curso, Bloqueado).
   - **Notas** con el texto de `Observaciones` (acortar si el campo es limitado).

4. Modulos nuevos en el reactor que no existan en el tablero: crear tarjeta y enlazar a la ruta `sources/...`.

## Mapeo sugerido estado -> columnas tipicas

| Matriz `PLUGIN_MATRIX` | Sugerencia de columna en tablero |
|---|---|
| Listo (CI) | Done / Ready |
| Listo (local) | In progress (validacion CI pendiente) |
| En curso | Todo o In progress |
| Bloqueado (build) | Blocked |

Ajustar nombres de columnas al esquema real del Project 1 de la org.
