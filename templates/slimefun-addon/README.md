# TemplateAddon

Plantilla base para crear addons nuevos alineados al stack de `drakes-slimefun-labs`.

## Qué reemplazar

- `TemplateAddon` por el nombre real del addon
- `dev.drake.template` por tu package real
- `TemplateMachine` por tu primer item o máquina
- ids como `TEMPLATE_MACHINE` y `template_research`

## Si el addon va dentro del monorepo

1. mueve esta carpeta a `sources/repos-to-port`, `sources/batch-2-expansion` o `sources/community-addons`
2. corrige `relativePath` del `pom.xml` si cambia la profundidad
3. agrega el módulo al `pom.xml` raíz
4. valida con build aislado

## Primer build

```powershell
mvn -pl ruta/del/modulo -am -DskipTests package
```

## Reglas rápidas

- usa el parent del reactor cuando el módulo viva dentro de este repo
- no fijes coordenadas viejas de `Slimefun 5`
- si usas `dough`, apunta al stack `dev.drake.dough`
- no marques el addon como listo solo porque compile una vez

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
