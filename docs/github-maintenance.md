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

## Dependabot y vulnerabilidades

### Dónde mirar

- **Dependabot alerts**: *Security → Dependabot alerts* en el repo (o API REST).
- **Dependabot version updates**: los PRs automáticos siguen `.github/dependabot.yml` (GitHub Actions + POM raíz Maven).
- **GitHub “Dependabot security updates”** y el grafo **Dependency review** pueden mostrar el mismo CVE en varios manifiestos; no es obligatorio tener *cero* filas duplicadas si el estado global es **fixed** o **dismissed** con motivo documentado.
- **Code scanning**: solo aparece si hay análisis configurado (CodeQL u otro); si la API devuelve 404, no hay alertas de código que listar.

### Revisión rápida por CLI (mantenedor)

Alertas Dependabot abiertas:

```bash
gh api "repos/DrakesCraft-Labs/drakes-slimefun-labs/dependabot/alerts?state=open&per_page=100" --jq 'length'
```

Resumen de alertas de dependencias (incluye histórico *fixed* / *dismissed*):

```bash
gh api graphql -f query='query($o:String!,$n:String!){repository(owner:$o,name:$n){vulnerabilityAlerts(first:50){nodes{state securityAdvisory{summary severity}}}}}' -f o=DrakesCraft-Labs -f n=drakes-slimefun-labs
```

### Cómo mitiga este monorepo

En el **`pom.xml` raíz**, `dependencyManagement` fija versiones seguras de uso frecuente (commons-lang3, Guava, Spring context, Plexus Utils, Commons IO, Jackson, Log4j2, etc.). Los submódulos heredan el BOM al resolver transitivos.

En **`build.gradle.kts`** del reactor Gradle, `resolutionStrategy` fuerza las mismas líneas críticas (commons-io, commons-lang3, jackson-core, log4j-api / log4j-core) para no diverger del árbol Maven.

**Commons Lang 2.x**: el código vulnerable upstream no recibe parche en Maven Central; aquí se usa **`commons-lang-drake-patched`** (reemplazo interno). Las alertas sobre `commons-lang:commons-lang` suelen **cerrarse como “dismissed”** o equivalente con la nota de que el runtime usa el artefacto parcheado.

Tras subir versiones: `mvn -B -DskipTests package -fae` (o el subconjunto que toque) y fusionar PRs de Dependabot con revisión humana.

### Alertas “del bot” (PRs / comentarios)

- **Dependabot**: PRs con etiquetas `dependencies`, `java`, `maven` o `github-actions`; revisar CI y conflictos con `1.21-latin`.
- **Copilot / otros bots**: mismo criterio que un PR humano: CI verde y alcance claro.
- **Issues**: si un bot abre un issue de seguridad, enlazar el GHSA y el commit o PR que lo mitiga.

## CI Maven: fundación vs reactor completo

El workflow **CI Monorepo 1.21** (`.github/workflows/ci-monorepo-121.yml`) define dos comportamientos distintos:

1. **Maven · fundación** ejecuta `mvn clean compile` solo sobre el stack base (Dough, Slimefun core, SefiLib, InfinityLib, commons-lang parcheado). **No** se ejecuta el *shade* de Slimefun, por tanto **no existen** paquetes `com.github.drakescraft_labs.slimefun4.libraries.dough.*` en el classpath. **SefiLib** e **InfinityLib** deben importar **`dev.drake.dough.protection.*`**. El script `scripts/fix_dough_compilation_imports.py` **excluye** esos árboles para no sustituir imports por los tipos sombreados.

2. **Maven · reactor completo** ejecuta `mvn package` sobre todo el reactor. Ahí Slimefun **sí** empaqueta con shade antes de los addons que dependen de él, así que los addons pueden usar **`com.github.drakescraft_labs.slimefun4.libraries.dough.protection.*`** (ver script anterior y comentarios en `docs/README.md`).

El workflow **Release monorepo JARs** también usa `package`, coherente con el reactor completo.

## GraalVM (RykenSlimeCustomizer) y CI

- En Maven, **`org.graalvm.js:js`** (tipo POM “enterprise”) arrastra **`truffle-enterprise`**, problemático en Maven público y en GitHub Actions. Debe usarse **`org.graalvm.js:js-community`** (mismo rango de versión, p. ej. `24.1.2`). El script **`scripts/fix_graalvm_js_community_poms.py`** (`--audit`, `--dry-run`) y **`scripts/ci_hygiene_fixes.py`** documentan y automatizan el barrido; **`scripts/manager.py repair`** incluye la misma regla en transformaciones POM.

- En **Libby** (carga en runtime), no se debe pedir el artefacto **`truffle-enterprise`**; el stack community usa **`truffle-runtime`** junto con `js-language`, `truffle-api`, etc.

## Que todo quede “en verde”

1. Rama objetivo: `1.21-latin` (o la que defina el equipo).
2. Comprobar el último run de **CI Monorepo 1.21** y del smoke manual si aplica.
3. Si un job falla por infraestructura (caché, red), *Re-run jobs*; si es código, arreglar y empujar.

## Projects (tablero org)

Ver [PROJECT_BOARD_SYNC.md](PROJECT_BOARD_SYNC.md). La CLI necesita scopes `read:project` y `project` tras `gh auth refresh`.

## Release de todos los JAR del monorepo

El workflow **Release monorepo JARs** (`release-monorepo-jars.yml`) se lanza a mano (*Actions → Release monorepo JARs → Run workflow*). Ejecuta `mvn -B -DskipTests package` en el reactor, recopila un jar por módulo con `scripts/release/collect_monorepo_jars.py`, genera `monorepo-plugins.zip` y crea un **GitHub Release** con ese ZIP adjunto.

- **Tag**: obligatorio y único (por ejemplo `v11-plugins-2026-04-25`). Si el tag ya existe, el paso de release fallará hasta que elijas otro.
- **Draft / Prerelease**: útil la primera vez para revisar notas y adjuntos antes de publicar.
- Los módulos sin `target/*.jar` (no compilados) aparecen en `manifest.json` dentro del ZIP con la lista `missing_modules`; conviene revisar ese archivo si el ZIP parece incompleto.

Tras publicar, el despliegue típico en survival es manual (por ejemplo en **[DrakesCraft](https://drakescraft.cl)**); el ZIP es un solo paquete coherente para actualizar el pack sin publicar un release por cada addon.
