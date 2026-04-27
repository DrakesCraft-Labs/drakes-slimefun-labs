# Aircraft — aviso legal y upstream

## Origen del código

Este árbol se basa en el proyecto público **Aircraft** / **Aircraft-dev** de la organización MetaMechanists en GitHub (fork en cadena desde el trabajo de **Idra / LordIdra**).

- Repositorio de referencia: `https://github.com/metamechanists/Aircraft-dev`

## Licencia

En el momento de la integración en **DrakesCraft Labs**, el repositorio upstream **no incluía un archivo `LICENSE`** reconocible en la raíz. Eso implica **reserva de derechos por defecto** según la jurisdicción aplicable: **no asumir** que el código es de dominio público ni que permite redistribución comercial sin permiso explícito de los autores.

Este fork de laboratorio existe para **compilar contra Paper 1.21.x** y el Slimefun del monorepo. Cualquier uso público, distribución de binarios o publicación en una org distinta debe **revisarse con los mantenedores upstream** y, si aplica, obtener licencia por escrito.

## Dependencias de runtime

Además de **Slimefun** y **ProtocolLib**, Aircraft **requiere el plugin KinematicCore** (MetaMechanists) en el servidor. **Furnished** es opcional (`softdepend`).

## Compilar en DrakesCraft Labs

Desde la raíz del monorepo (rama **1.21-latin**), con Paper API `${paper.version}` (p. ej. `1.21.1-R0.1-SNAPSHOT`):

```bash
mvn -pl sources/repos-to-port/Aircraft-dev -am package -DskipTests
```

El JAR queda en `sources/repos-to-port/Aircraft-dev/target/`.

## Cambios respecto al upstream

- `pom.xml`: padre del reactor, `paper-api` y `slimefun-core` del laboratorio, `sefilib-drake`, repos Maven (Paper, MetaMechanists, etc.).
- Paquetes `io.github.thebusybiscuit.slimefun4` → `com.github.drakescraft_labs.slimefun4`.
- `Groups`: `dev.sefiraat.sefilib` → `dev.drake.sefilib` (SefiLib del monorepo).
- `plugin.yml`: `api-version: '1.21'`; **Furnished** como `softdepend`.
