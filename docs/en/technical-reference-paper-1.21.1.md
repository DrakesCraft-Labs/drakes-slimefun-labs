# Technical Reference (Paper 1.21.1)

## Scope Note

The target branch currently works on `Paper 1.21.11`, but many of the technical breakages discovered during the port started with the jump to the `1.21.x` line. This page summarizes those patterns.

## Breakage Patterns Already Observed

### Misaligned Maven dependencies

Typical cases:

- modules not inheriting from the reactor parent
- dependencies still pointing to `dev.drake:Slimefun:5.0-Drake-1.21.11`
- missing `dev.drake.dough:dough-core`
- remote dependencies that should already be satisfied inside the reactor

### Legacy Slimefun or Dough APIs

Cases already seen:

- old imports that had to move to `dev.drake.dough.*`
- legacy addon utilities that no longer exist the same way in the current stack
- outdated signatures that only still worked in historical branches

### Bukkit/Paper API changes

Cases already detected:

- obsolete particles such as `Particle.REDSTONE` in places that now require `Particle.DUST`
- warnings or adjustments caused by deprecated Bukkit APIs
- changes around attributes, serializers, and visual helpers

## Real Cases from This Repository

- `SimpleUtils`: closed only after aligning `InfinityLib` with the current reactor
- `EcoPower`: its code already pointed at the new stack, but its `pom.xml` did not inherit from the parent and did not declare `dough-core`
- `SlimeTinker`: still hardcoded `Slimefun 5.0-Drake-1.21.11` before being fixed
- `MissileWarfare`: required obsolete particle fixes
- `PotionExpansion`: still broken because of old API usage in code, not just Maven metadata

## Recommended Diagnosis Order

1. inspect `pom.xml`
2. confirm the reactor parent
3. confirm the `Slimefun` and `dough-core` coordinates
4. only then move into API/code breakages

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **CI Monorepo 1.21** cubre reactor Maven completo + 5 Gradle.
> Nota: quedan pendientes smoke tests de runtime y estrategia de releases; no hay bloqueos de compilacion en el corte actual.
<!-- DRAKES-STATUS:END -->
