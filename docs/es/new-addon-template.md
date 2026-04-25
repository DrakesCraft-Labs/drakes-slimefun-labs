# New-Addon-Template

## Propósito

La carpeta `templates/slimefun-addon` existe para acelerar la creación de addons nuevos alineados al stack Drake actual, sin partir desde un proyecto viejo o con coordenadas obsoletas.

## Qué incluye la plantilla

- estructura mínima de addon Maven
- `pom.xml` orientado al reactor del laboratorio
- nombres de ejemplo fáciles de reemplazar
- README base para recordar el flujo de integración

## Qué debes reemplazar

- `TemplateAddon`
- `dev.drake.template`
- ids de ejemplo como `TEMPLATE_MACHINE`
- clases de muestra como `TemplateMachine`

## Si el addon va dentro de este monorepo

1. mover la carpeta a la zona adecuada:
   - `sources/repos-to-port`
   - `sources/batch-2-expansion`
   - `sources/community-addons`
2. corregir el `relativePath` del `pom.xml` si cambia la profundidad
3. agregar el módulo al `pom.xml` raíz
4. validar con build aislado

Comando recomendado:

```powershell
mvn -pl ruta/del/modulo -am -DskipTests package
```

## Qué no hacer

- no usar coordenadas viejas de `Slimefun 5`
- no dejar dependencias duplicadas si ya existen en el parent
- no asumir que por compilar ya quedó listo para producción

## Relación con el resto de la documentación

Esta plantilla sirve para crear módulos nuevos. Para el flujo de migración de addons existentes, usar además:

- [[Dev-Setup]]
- [[Checklist de Migración]]
- [[Estándares de Desarrollo]]

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
