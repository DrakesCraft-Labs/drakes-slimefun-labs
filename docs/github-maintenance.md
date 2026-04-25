# Mantenimiento en GitHub (Actions, PRs, seguridad)

Guía operativa para mantener el repositorio **drakes-slimefun-labs** ordenado en GitHub. Los permisos requieren rol de mantenedor en la org o el repo.

## Historial largo de workflow runs

GitHub **no** ofrece un botón “borrar todo el historial” de ejecuciones. Opciones reales:

1. **Retención automática** (recomendado): en el repo, *Settings → Actions → General → Artifact and log retention* (y políticas de la org). Reduce ruido sin scripts.
2. **Borrar ejecuciones con la CLI** (por lotes): con token que incluya `workflow`:

   ```bash
   gh run list --repo DrakesCraft-Labs/drakes-slimefun-labs --limit 200 --json databaseId -q '.[].databaseId' | xargs -n1 gh run delete --repo DrakesCraft-Labs/drakes-slimefun-labs
   ```

   En Windows PowerShell puedes iterar con un bucle corto sobre `gh run list`. Respeta los límites de tasa de la API; pausa entre lotes si hay cientos de entradas.

3. **Archivar el repo** o duplicar historial: medida extrema; no suele ser necesaria solo por “limpieza visual”.

No confundas borrar *runs* con borrar *logs de artifact*; son ajustes distintos en Settings.

## Pull requests

1. `gh pr list --repo DrakesCraft-Labs/drakes-slimefun-labs --state open`
2. Para cada PR: revisar CI, conflicto con `1.21-latin`, y si el cambio sigue la política del monorepo.
3. **Merge** cuando CI esté verde y el alcance sea claro; **cerrar** con comentario si está obsoleta o duplica trabajo ya integrado.

Los merges los debe hacer alguien con contexto del porte; esta guía no sustituye revisión humana.

## Dependabot y “vulnerabilities”

- Revisa *Security → Dependabot alerts* (y *Code scanning* si está habilitado).
- En Java, muchas alertas vienen de dependencias transitivas: solución típica = actualizar el padre BOM, subir versión explícita en el `pom` raíz, o exclusiones controladas (documentar el porqué).
- Tras cambios de versiones: `mvn -B -DskipTests verify` o al menos `compile -fae` en local antes de fusionar.

## Que todo quede “en verde”

1. Rama objetivo: `1.21-latin` (o la que defina el equipo).
2. Comprobar el último run de **CI Monorepo 1.21** y del smoke manual si aplica.
3. Si un job falla por infraestructura (caché, red), *Re-run jobs*; si es código, arreglar y empujar.

## Projects (tablero org)

Ver [PROJECT_BOARD_SYNC.md](PROJECT_BOARD_SYNC.md). La CLI necesita scopes `read:project` y `project` tras `gh auth refresh`.
