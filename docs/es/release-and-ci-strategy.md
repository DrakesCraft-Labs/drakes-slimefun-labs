# Estrategia de Releases y CI

## Objetivo

Definir una política clara para publicación de artefactos y automatización en `drakes-slimefun-labs` sin confundir el laboratorio de migración con una distribución monolítica estable.

## Estado actual

Este repositorio es un `reactor Maven` grande y curado por etapas.

Eso implica que:

- no todos los módulos tienen el mismo nivel de validación runtime
- algunos todavía conservan versiones como `UNOFFICIAL`, `MODIFIED` o similares
- el hecho de que un módulo compile no significa automáticamente que deba publicarse como release estable

## Decisión principal

No se debe publicar una release masiva con todos los `.jar` del reactor como si fueran un solo producto listo para producción.

En su lugar, la estrategia recomendada es:

1. usar CI para validar grupos curados de módulos estables
2. conservar artifacts descargables por workflow para revisión rápida
3. publicar releases manuales o semimanuales solo para stacks o módulos seleccionados

## Qué sí conviene publicar

### Releases por stack base

Buenos candidatos:

- `dough-core`
- `Slimefun` Drake core

### Releases por librería o addon bien delimitado

Buenos candidatos:

- `InfinityLib`
- `InfinityExpansion`
- `SimpleUtils`
- `ExoticGarden`

### Releases por lote curado

Solo si se documentan bien:

- un batch de addons validados por build y smoke test
- una familia de módulos con dependencia común

## Qué no conviene hacer todavía

- una release única con decenas de `.jar` mezclados
- una etiqueta global que sugiera estabilidad homogénea de todo el reactor
- automatizar publicación de releases para cada `push` sin filtro

## Nueva Arquitectura de CI (Modular Gates)

A partir de la v7, el CI se ha dividido en **4 Gates (Puertas de Calidad)** paralelas para aislar fallos y asegurar el núcleo:

*   **Gate: Foundation** 🧱: Valida y publica el stack crítico (`slimefun-core`, `dough-core`, librerías base).
*   **Gate: Standard Addons** 📦: Addons oficiales y estables.
*   **Gate: Expansion Addons** 🌌: Expansiones de gran tamaño y complejidad.
*   **Gate: Community Addons** 👥: Addons externos integrados progresivamente.

Esto permite que un fallo en un addon de la comunidad no bloquee el despliegue del núcleo o de los addons oficiales.

## Política de releases

Una release debe hacerse cuando:

- el módulo o grupo tiene build validado de forma consistente
- su versión y nombre son razonables para consumo público
- no hay bloqueo activo conocido de API o dependencias
- si el módulo lo necesita, ya pasó una validación runtime básica

## Próximos pasos sugeridos

1. mantener y ajustar la CI curada
2. revisar naming/versionado de módulos candidatos a release
3. crear draft releases manuales para módulos verdaderamente estables
4. solo después evaluar automatización de releases
