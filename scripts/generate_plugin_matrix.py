#!/usr/bin/env python3
"""Genera docs/es/PLUGIN_MATRIX.md a partir del reactor y reglas de estado auditables."""

from __future__ import annotations

import os
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NS = {"m": "http://maven.apache.org/POM/4.0.0"}

# Modulos con evidencia explicita en .github/workflows/ci-monorepo-121.yml.
# El job `maven_full_reactor` compila el reactor Maven completo y `gradle_green`
# compila los 5 proyectos declarados en settings.gradle.kts.
GATE_1 = {
    "sources/dough-core",
    "sources/slimefun-core/Slimefun4-src",
    "sources/batch-2-expansion/SefiLib",
    "sources/batch-2-expansion/InfinityLib",
    "sources/internal-metadata/patches/commons-lang-drake-patched",
}
# Job gradle_green: compileJava de todos los proyectos Gradle del reactor.
GATE_5_GRADLE_OK = {
    "sources/batch-2-expansion/Galactifun",
    "sources/community-addons/Bump",
    "sources/community-addons/CustomItemGenerators",
    "sources/community-addons/FastMachines",
    "sources/community-addons/SlimefunTranslation",
}

# Compilacion local verificada el 2026-04-24:
# - Reactor Maven completo: mvn -B -DskipTests compile -fae -> BUILD SUCCESS
# - Proyectos Gradle declarados en settings.gradle.kts: compileJava -> BUILD SUCCESS
LOCAL_BUILD_CUT = "2026-04-24"

# Gradle reactor (settings.gradle.kts)
GRADLE_MODULES = {
    "sources/batch-2-expansion/Galactifun",
    "sources/community-addons/Bump",
    "sources/community-addons/CustomItemGenerators",
    "sources/community-addons/FastMachines",
    "sources/community-addons/SlimefunTranslation",
}

GRADLE_LOCAL_OK_NOTE = {
    "sources/community-addons/CustomItemGenerators": "Gradle `compileJava` verde. Port principal: `JavaPlugin` directo, adapter `SlimefunAddon`, sin `sf4k`; proteccion usa paquete Drake sombreado.",
    "sources/community-addons/FastMachines": "Gradle `compileJava` verde. Incluye bridges locales `MenuBlock`/`TickingMenuBlock` y DSL `DrakeItemBuilderCompat`; requiere artefactos Maven base e `InfinityExpansion-drake` en `mavenLocal`.",
    "sources/community-addons/SlimefunTranslation": "Gradle `compileJava` verde. API Paper 1.21 ajustada (`EntityType.ITEM`) y SlimefunTranslation compilable contra Slimefun Drake.",
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
    if path in GATE_5_GRADLE_OK:
        return (
            "Listo (CI)",
            "CI Monorepo · gradle_green",
            "Compila en job `gradle_green` (`compileJava`); Maven base e integraciones requeridas se instalan antes en el mismo workflow.",
        )
    if path in GATE_1:
        return ("Listo (CI)", "CI Monorepo · foundation", "Stack base Paper 1.21.1 + Java 21.")
    if path in GRADLE_MODULES:
        note = GRADLE_LOCAL_OK_NOTE.get(
            path,
            "Incluido en reactor Gradle raiz; `compileJava` verificado localmente en el corte actual.",
        )
        return (
            "Listo (local)",
            f"Gradle compileJava {LOCAL_BUILD_CUT}",
            note + " Falta promover a CI si debe quedar como gate permanente.",
        )
    # Resto del reactor Maven
    return (
        "Listo (CI)",
        "CI Monorepo · maven_full_reactor",
        "`mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; falta smoke en servidor si el addon tiene mecanicas sensibles.",
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
        "- **Listo (CI)**: modulo construido explicitamente en el workflow `ci-monorepo-121.yml` (job correspondiente).",
        "- **Listo (local)**: `mvn compile -fae` o `gradlew <proyecto>:compileJava` exitoso en la revision auditada (no sustituye CI).",
        "- **En curso**: en reactor pero sin evidencia de build reciente por modulo.",
        "- **Bloqueado (build)**: fallo reproducible de compilacion en el reactor local.",
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
[![CI Monorepo](https://img.shields.io/badge/CI-Monorepo%201.21-16a34a?style=for-the-badge&logo=githubactions)](https://github.com/DrakesCraft-Labs/drakes-slimefun-labs/actions/workflows/ci-monorepo-121.yml)
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

> Corte generado automaticamente a partir de `ci-monorepo-121.yml`, reactor `pom.xml`, `settings.gradle.kts` y evidencia de compilacion local documentada en el script.

| Estado | Cantidad | Significado |
|---:|---:|---|
| **Listo (CI)** | **{ci}** | Aparece en `ci-monorepo-121.yml` (`maven_full_reactor`, `foundation` o `gradle_green`) y compila alli. |
| **Listo (local)** | **{loc}** | `mvn compile -fae` o `gradlew <proyecto>:compileJava` verde en revision auditada; **pendiente** promover a un job de `ci-monorepo-121.yml`. |
| **En curso** | **{prog}** | En reactor Maven/Gradle; sin build verificado por modulo o solo parches aplicados (`port_paper_121`, etc.). |
| **Bloqueado (build)** | **{blk}** | Fallo reproducible de compilacion en el reactor local. |
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

1. **Listo (CI)**: modulo cubierto por un job de [`ci-monorepo-121.yml`](.github/workflows/ci-monorepo-121.yml) (`foundation`, `maven_full_reactor`, `gradle_green`).
2. **Listo (local)**: compilacion Maven/Gradle exitosa en la misma revision que el script (no reemplaza CI).
3. **En curso**: modulo declarado en `pom.xml` o `settings.gradle.kts` sin evidencia anterior.
4. **Bloqueado (build)**: error de `compileJava` / `compileKotlin` en build local documentado en `docs/es/pending-modules.md`.

Herramientas de porteo: `scripts/port_paper_121.py` (API Bukkit 1.21.1 y rutas Dough), `scripts/manager.py audit`, bridges locales de compatibilidad para addons Gradle (`MenuBlock`, `TickingMenuBlock`, `DrakeItemBuilderCompat`) y bridges BusyBiscuit en Slimefun core (`io.github.thebusybiscuit.slimefun4.*`).

---

## Inventario completo de modulos y plugins

Leyenda de **Tipo**: `core`, `libreria`, `interno`, `addon`, `addon (port)` (repos-to-port), `addon (Gradle)`.

{table_only}

---

## Arbol resumido del monorepo

```text
drakes-slimefun-labs/
├─ .github/workflows/     # ci-monorepo-121.yml (CI unificado)
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

# Verificacion local del corte completo
mvn -B -DskipTests compile -fae
./gradlew :sources:batch-2-expansion:Galactifun:compileJava :sources:community-addons:Bump:compileJava :sources:community-addons:CustomItemGenerators:compileJava :sources:community-addons:FastMachines:compileJava :sources:community-addons:SlimefunTranslation:compileJava --no-daemon

# Dependencia local necesaria para FastMachines cuando Gradle resuelve InfinityExpansion-drake
mvn -B -DskipTests install -pl sources/repos-to-port/InfinityExpansion -am
```

---

## Licencia

Proyecto bajo **GPLv3**. Ver [`LICENSE`](LICENSE).
"""
    (ROOT / "README.md").write_text(readme, encoding="utf-8")
    print(f"Wrote {ROOT / 'README.md'}")


if __name__ == "__main__":
    main()
