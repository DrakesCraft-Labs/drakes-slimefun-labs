# Development Standards

## Operating Principles

- prefer small validated changes
- do not compile the full reactor unless there is a real reason
- diagnose `pom.xml` and dependencies before deep API porting
- document every state change in `README.md`, `README_EN.md`, and the wiki

## Build Standard

```powershell
mvn -pl path/to/module -am -DskipTests package
```

It is also valid to use `compile` when only a quick compile validation is needed.

## Documentation Standard

When an addon changes state:

1. update the inventory in `README.md`
2. update `README_EN.md`
3. update the wiki if counts or route changed
4. leave observations if it compiles but still has runtime risk

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
