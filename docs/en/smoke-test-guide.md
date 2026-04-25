# Smoke Test Guide

## Goal

The smoke test is used to verify that the base stack and integrated addons not only compile, but also load in a minimally healthy runtime environment.

## When to use it

- after several closures in a row
- when the core or `dough-core` has been touched
- when a sensitive addon compiles but still deserves runtime review
- before calling a major roadmap block stabilized

## Available script

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\slimefun\smoke-test.ps1
```

## Minimum validation targets

- Paper/Purpur `1.21.11` starts cleanly with the base stack
- `Slimefun` loads correctly
- newly closed addons do not throw enable errors
- critical optional integrations do not crash on load

## Addons worth extra attention

- `MapJammers`
- `MissileWarfare`
- `SoundMuffler`
- `Simple-Storage`
- `SlimeCustomizer`
- `RykenSlimeCustomizer-EN`
- `Element-Manipulation`
- `HeadLimiter` if `Towny` is involved

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
