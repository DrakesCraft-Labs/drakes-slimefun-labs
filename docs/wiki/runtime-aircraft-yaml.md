# Runtime: Aircraft (fork Drake) — YAML de vehículos

Módulo: `sources/repos-to-port/Aircraft-dev` · artefacto **`Aircraft-drake`**.

## Ubicación en disco

Con el servidor arrancado, los esquemas se leen desde:

`plugins/Aircraft/vehicles/<id>.yml`

por ejemplo `crude_airplane.yml`, `crude_airship.yml`, `crude_drone.yml`, `cessna.yml`, `hoverduck.yml`, `metacoin_ufo.yml`.

Los defaults **empaquetados** viven en `src/main/resources/vehicles/` del repo.

## Revisión de esquema (`VehicleConfigs`)

La clase `org.metamechanists.aircraft.VehicleConfigs` (invocada desde `Aircraft.onEnable`) mantiene un entero **`SCHEMA_REVISION`** y el fichero:

`plugins/Aircraft/vehicles/.schema_revision`

Si la revisión en disco es **menor** que la del código, se vuelcan de nuevo **todos** los YAML empaquetados (`saveResource(..., true)` por vehículo conocido). Así se corrigen copias viejas (por ejemplo `engine.force` como **número escalar** cuando MetaLib espera **lista de tres números** `[x, y, z]`).

**Cuando cambies el formato** de los YAML en el jar, incrementa `SCHEMA_REVISION` en `VehicleConfigs.java` para forzar una nueva escritura en servidores que ya tengan `.schema_revision` al día con la revisión anterior.

## Formato que exige MetaLib (resumen)

- Vectores (`translation`, `engine.force`, `rotation`, `location`, etc.): **listas YAML de tres números**, no escalares sueltos.
- Recursos (`VehicleResource`): incluir **`guiKey`** (carácter) si quieres que el GUI de cabina mapee barra/texto; la estructura **`gui`** (filas InvUI) debe ser coherente con esas claves.
- Tras un volcado, revisa `plugins/Aircraft/vehicles/` si personalizaste física o modelos.

## Upstream

Notas de fork y licencias: `sources/repos-to-port/Aircraft-dev/NOTICE-UPSTREAM.md`.
