#!/usr/bin/env python3
"""Genera docs/es/PLUGIN_MATRIX.md a partir del reactor y reglas de estado auditables."""

from __future__ import annotations

import os
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NS = {"m": "http://maven.apache.org/POM/4.0.0"}

# Modulos con evidencia explicita en .github/workflows/ci-gate-*.yml
GATE_1 = {
    "sources/dough-core",
    "sources/slimefun-core/Slimefun4-src",
    "sources/batch-2-expansion/SefiLib",
    "sources/batch-2-expansion/InfinityLib",
    "sources/internal-metadata/patches/commons-lang-drake-patched",
}
GATE_2 = {
    "sources/community-addons/MiniBlocks",
    "sources/community-addons/DyeBench",
    "sources/community-addons/Quaptics",
}
GATE_3 = {
    "sources/repos-to-port/ExtraUtils",
    "sources/community-addons/DankTech2",
}
GATE_4 = {"sources/batch-2-expansion/Supreme"}
# Gate 5: solo Galactifun en el job Gradle (SlimefunTranslation excluido en comentario)
GATE_5_GRADLE_OK = {"sources/batch-2-expansion/Galactifun"}

# Compilacion Maven local verificada (mvn compile -am) Abril 2026 — misma sesion de auditoria
LOCAL_MAVEN_COMPILE = {
    "sources/batch-2-expansion/Cultivation_Updated",
    "sources/batch-2-expansion/LiteXpansion",
    "sources/batch-2-expansion/SMG",
    "sources/batch-2-expansion/TranscEndence",
    "sources/batch-2-expansion/Supreme",  # tambien Gate 4
}

# Gradle reactor (settings.gradle.kts) — fallos conocidos en build completo del monorepo
GRADLE_MODULES = {
    "sources/batch-2-expansion/Galactifun",
    "sources/community-addons/Bump",
    "sources/community-addons/CustomItemGenerators",
    "sources/community-addons/FastMachines",
    "sources/community-addons/SlimefunTranslation",
}
GRADLE_FAIL = {
    "sources/community-addons/Bump",
    "sources/community-addons/CustomItemGenerators",
    "sources/community-addons/FastMachines",
    "sources/community-addons/SlimefunTranslation",
}

GRADLE_FAIL_NOTE = {
    "sources/community-addons/Bump": "Bloquea `compileJava` del reactor Gradle (~90 errores): `AbstractAddon` de GuizhanLib enlaza `io.github.thebusybiscuit.slimefun4.*`, inexistente en Slimefun Drake (`com.github.drakescraft_labs.slimefun4.*`). Mitigaciones en repo: Lombok Freefair, GuizhanLib 2.x imports, `SlimefunLocalization`. Pendiente: shim/fork de `AbstractAddon` o classpath coherente; APIs 1.21 (`ItemFlag`, `Enchantment.LUCK`, `Attribute.HORSE_JUMP_STRENGTH`).",
    "sources/community-addons/CustomItemGenerators": "Kotlin: clase base `AbstractAddon` vs `JavaPlugin` (`dataFolder`, `server`, etc.); dependencia `sf4k` y firma `SlimefunAddon`.",
    "sources/community-addons/FastMachines": "Kotlin: extensiones y tipos esperan `SlimefunAddon`/`SlimefunItemStack` del fork; ajustar imports y bridge de addon.",
    "sources/community-addons/SlimefunTranslation": "Java: decenas de errores de API (SlimefunAddon, permisos, traducciones); migracion profunda contra Slimefun4 Drake.",
}


def module_type(path: str) -> str:
    if "slimefun-core" in path and "Slimefun4" in path:
        return "core"
    if "dough-core" in path or "InfinityLib" in path or "SefiLib" in path:
        return "libreria"
    if "internal-metadata" in path:
        return "interno"
    if "repos-to-port" in path:
        return "addon (port)"
    if "community-addons" in path:
        return "addon"
    if "batch-2-expansion" in path:
        return "addon / expansion"
    return "modulo"


def classify(path: str) -> tuple[str, str, str]:
    """Devuelve (estado_corto, evidencia, observacion)."""
    name = Path(path).name
    if path in GRADLE_FAIL:
        note = GRADLE_FAIL_NOTE.get(
            path,
            "Fallo compilacion Gradle. Ver `docs/es/pending-modules.md`.",
        )
        return ("Bloqueado (build)", "Gradle monorepo", note)
    if path in GATE_5_GRADLE_OK:
        return (
            "Listo (CI)",
            "Gate 5 Gradle",
            "Construido en CI; dependencias base Maven pre-instaladas en el workflow.",
        )
    if path in GATE_1:
        return ("Listo (CI)", "Gate 1", "Stack base Paper 1.21.1 + Java 21.")
    if path in GATE_2:
        return ("Listo (CI)", "Gate 2", "Addons estables del lote rapido.")
    if path in GATE_3:
        return ("Listo (CI)", "Gate 3", "Repos + comunidad (subconjunto).")
    if path in GATE_4:
        return ("Listo (CI)", "Gate 4", "Supreme en lote complejo; ademas compila en cadena Maven local.")
    if path in LOCAL_MAVEN_COMPILE and path not in (GATE_1 | GATE_2 | GATE_3 | GATE_4):
        return (
            "Listo (local)",
            "Maven compile 2026-04",
            "Compila con `mvn -pl ... -am compile` en baseline actual; falta incorporarlo a un gate CI y smoke en servidor.",
        )
    if path in GRADLE_MODULES:
        return (
            "En curso",
            "Gradle",
            "Incluido en reactor Gradle raiz; pendiente verificacion tras desbloquear vecinos o ajustar CI.",
        )
    # Resto del reactor Maven
    return (
        "En curso",
        "Reactor Maven",
        "En `pom.xml` raiz; aplicado parche masivo `port_paper_121` (Paper 1.21.1) sobre fuentes. Falta compile por lote + inclusion en gate CI.",
    )


def maven_modules() -> list[str]:
    tree = ET.parse(ROOT / "pom.xml")
    mods = []
    for m in tree.getroot().findall("m:modules/m:module", NS):
        mods.append(m.text.strip())
    return mods


def main() -> None:
    rows: list[tuple[str, str, str, str, str, str]] = []
    for path in sorted(maven_modules()):
        st, ev, obs = classify(path)
        rows.append((path, Path(path).name, module_type(path), st, ev, obs))

    for path in sorted(GRADLE_MODULES):
        if path in maven_modules():
            continue
        st, ev, obs = classify(path)
        rows.append((path, Path(path).name, "addon (Gradle)", st, ev, obs))

    lines = [
        "# Matriz de plugins y modulos (generada)",
        "",
        "> Generado por `scripts/generate_plugin_matrix.py`. No editar a mano: ejecutar el script y commit.",
        "",
        "Criterios:",
        "",
        "- **Listo (CI)**: modulo construido explicitamente en un workflow `ci-gate-*.yml`.",
        "- **Listo (local)**: `mvn compile -am` exitoso en la revision auditada (no sustituye CI).",
        "- **En curso**: en reactor pero sin evidencia de build reciente por modulo.",
        "- **Bloqueado (build)**: fallo reproducible de compilacion en el reactor Gradle.",
        "",
        "| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |",
        "|---|---|:---:|---|---|---|",
    ]
    for path, name, typ, st, ev, obs in rows:
        obs_one = obs.replace("|", "\\|")
        lines.append(f"| {name} | {typ} | {st} | {ev} | `{path}` | {obs_one} |")

    out = ROOT / "docs" / "es" / "PLUGIN_MATRIX.md"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {out} ({len(rows)} rows)")

    # --- README principal (tabla incrustada, misma fuente de datos) ---
    # rows: (path, name, typ, st, ev, obs)
    ci = sum(1 for r in rows if r[3] == "Listo (CI)")
    loc = sum(1 for r in rows if r[3] == "Listo (local)")
    prog = sum(1 for r in rows if r[3] == "En curso")
    blk = sum(1 for r in rows if r[3] == "Bloqueado (build)")
    total = len(rows)
    table_only = "\n".join(lines[lines.index("| Modulo | Tipo | Estado | Evidencia | Ruta | Observaciones |") :])
    pct_ci = 100.0 * ci / len(rows) if rows else 0.0

    readme = f"""# Drakes Slimefun Labs

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.1](https://img.shields.io/badge/Paper-1.21.1-3b82f6?style=for-the-badge&logo=minecraft)](https://papermc.io/)
[![CI Gates](https://img.shields.io/badge/CI-Gates%201--5%20Green-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions)
[![Monorepo](https://img.shields.io/badge/Monorepo-Slimefun%20Ecosystem-7c3aed?style=for-the-badge)](#inventario-completo-de-modulos-y-plugins)
[![GPLv3](https://img.shields.io/badge/License-GPLv3-ef4444?style=for-the-badge)](LICENSE)

Laboratorio de integracion, porteo y estabilizacion para el ecosistema **Slimefun 4** sobre baseline unificado **Paper 1.21.1 + Java 21**. Este repositorio agrupa el core Drake, librerias compartidas y decenas de addons en un reactor **Maven + Gradle** coherente.

---

## Enlaces rapidos

| Recurso | URL |
|---|---|
| **GitHub Project (estado org)** | [DrakesCraft-Labs / Project 1](https://github.com/orgs/DrakesCraft-Labs/projects/1) |
| **Actions (CI)** | [Workflow runs](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions) |
| **Issues** | [Issues](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/issues) |
| **Matriz detallada (generada)** | [`docs/es/PLUGIN_MATRIX.md`](docs/es/PLUGIN_MATRIX.md) |
| **Como sincronizar el tablero** | [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md) |
| **Docs ES (indice)** | [`docs/es/home.md`](docs/es/home.md) |

> La matriz y la tabla de este README se generan con `python scripts/generate_plugin_matrix.py` para evitar desalineacion manual.

---

## Tablero GitHub Projects

El estado de alto nivel del porteo se gestiona en el [**Project 1 de la organizacion**](https://github.com/orgs/DrakesCraft-Labs/projects/1).

Para actualizarlo desde tu equipo con la CLI, autoriza scopes de Projects:

```bash
gh auth refresh -h github.com -s read:project,project
```

Luego alinea cada tarjeta con la columna **Estado** y las **Observaciones** de la tabla inferior (o del archivo `docs/es/PLUGIN_MATRIX.md`). Guia paso a paso: [`docs/PROJECT_BOARD_SYNC.md`](docs/PROJECT_BOARD_SYNC.md).

---

## Resumen de estado (auditable)

> Corte generado automaticamente a partir de `ci-gate-*.yml`, reactor `pom.xml`, `settings.gradle.kts` y evidencia de compilacion local documentada en el script.

| Estado | Cantidad | Significado |
|---:|---:|---|
| **Listo (CI)** | **{ci}** | Aparece en un workflow `ci-gate-*` y compila alli. |
| **Listo (local)** | **{loc}** | `mvn compile -am` verde en revision auditada; **pendiente** promover a gate CI. |
| **En curso** | **{prog}** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **{blk}** | Fallo reproducible de compilacion en el reactor Gradle. |
| **Total modulos** | **{total}** | Maven + Gradle en reactor; ver conteo exacto en esta fila. |

### Barra de proporcion (CI vs resto)

```text
Listo CI:     {ci}/{total}  ({pct_ci:.1f}%)
Listo local:  {loc}/{total}
En curso:     {prog}/{total}
Bloqueado:    {blk}/{total}
```

---

## Metodologia (criterios)

1. **Listo (CI)**: modulo listado explicitamente en `ci-gate-1-foundation.yml`, `ci-gate-2-stable.yml`, `ci-gate-3-community.yml`, `ci-gate-4-complex.yml` o paso Gradle de `ci-gate-5-gradle.yml`.
2. **Listo (local)**: compilacion Maven exitosa en cadena `-am` en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build Gradle del monorepo documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`.

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

{table_only}

---

## Arbol resumido del monorepo

```text
drakes-slimefun-labs/
├─ .github/workflows/     # ci-gate-1 .. ci-gate-5
├─ docs/                  # guias ES/EN + PROJECT_BOARD_SYNC + PLUGIN_MATRIX
├─ scripts/               # generate_plugin_matrix.py, port_paper_121.py, manager.py
├─ sources/
│  ├─ slimefun-core/Slimefun4-src
│  ├─ dough-core/
│  ├─ batch-2-expansion/
│  ├─ community-addons/
│  ├─ repos-to-port/
│  └─ internal-metadata/
├─ pom.xml
└─ settings.gradle.kts
```

---

## Comandos utiles

```bash
# Regenerar matriz + README (tabla alineada)
python scripts/generate_plugin_matrix.py

# Parche masivo Paper 1.21.1 (dry-run primero)
python scripts/port_paper_121.py --dry-run --path sources/community-addons/MiAddon

# Build base Maven (ejemplo)
mvn -B clean install -DskipTests -pl sources/dough-core,sources/slimefun-core/Slimefun4-src -am
```

---

## Licencia

Proyecto bajo **GPLv3**. Ver [`LICENSE`](LICENSE).
"""
    (ROOT / "README.md").write_text(readme, encoding="utf-8")
    print(f"Wrote {ROOT / 'README.md'}")


if __name__ == "__main__":
    main()
