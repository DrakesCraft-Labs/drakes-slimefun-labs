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

# Observaciones especificas por modulo (sobrescribe el texto generico de classify).
MAVEN_MODULE_OBSERVATION_OVERRIDES: dict[str, tuple[str, str, str]] = {
    "sources/repos-to-port/Aircraft-dev": (
        "Listo (CI)",
        "CI Monorepo · maven_full_reactor",
        "`mvn -B compile -DskipTests -fae` cubre el reactor Maven completo; en runtime los YAML de vehiculos van en `plugins/Aircraft/vehicles/` (empaquetados en el jar). El fork Drake usa `vehicles/.schema_revision` para volcar defaults cuando cambia el formato (MetaLib exige vectores como listas de tres numeros, etc.). Smoke en servidor recomendado.",
    ),
}

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
    if "drakes-labs-autoupdate" in path:
        return "libreria (updater)"
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
    if path in MAVEN_MODULE_OBSERVATION_OVERRIDES:
        return MAVEN_MODULE_OBSERVATION_OVERRIDES[path]
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


def github_updater_badge(path: str) -> str:
    """Indica si el modulo integra el updater de releases GitHub del monorepo (dependencia drakes-labs-autoupdate)."""
    if path == "sources/drakes-labs-autoupdate":
        return "—"
    if "internal-metadata" in path or "commons-lang-drake-patched" in path:
        return "—"
    if path in {
        "sources/dough-core",
        "sources/slimefun-core/Slimefun4-src",
        "sources/batch-2-expansion/SefiLib",
        "sources/batch-2-expansion/InfinityLib",
    }:
        return "—"
    p = ROOT / path
    pom = p / "pom.xml"
    if pom.is_file():
        try:
            txt = pom.read_text(encoding="utf-8", errors="replace")
        except OSError:
            return "?"
        return "Sí" if "drakes-labs-autoupdate" in txt else "No"
    for rel in ("build.gradle.kts", "plugin/build.gradle.kts"):
        g = p / rel
        if g.is_file():
            try:
                t = g.read_text(encoding="utf-8", errors="replace")
            except OSError:
                continue
            if "drakes-labs-autoupdate" in t:
                return "Sí"
    return "No"


def maven_modules() -> list[str]:
    tree = ET.parse(ROOT / "pom.xml")
    mods = []
    for m in tree.getroot().findall("m:modules/m:module", NS):
        mods.append(m.text.strip())
    return mods


def main() -> None:
    rows: list[tuple[str, str, str, str, str, str, str]] = []
    for path in sorted(maven_modules()):
        st, ev, obs = classify(path)
        rows.append(
            (path, Path(path).name, module_type(path), st, ev, obs, github_updater_badge(path))
        )

    for path in sorted(GRADLE_MODULES):
        if path in maven_modules():
            continue
        st, ev, obs = classify(path)
        rows.append(
            (path, Path(path).name, "addon (Gradle)", st, ev, obs, github_updater_badge(path))
        )

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
        "- **Updater GH**: columna **Sí** si el `pom.xml` / `build.gradle.kts` declara la dependencia `drakes-labs-autoupdate` (comprobacion de releases del repo del laboratorio). **—** en nucleo/librerias internas sin plugin.",
        "",
        "| Modulo | Tipo | Estado | Evidencia | Updater GH | Ruta | Observaciones |",
        "|---|---|:---:|---|:---:|---|---|",
    ]
    for path, name, typ, st, ev, obs, upd in rows:
        obs_one = obs.replace("|", "\\|")
        lines.append(f"| {name} | {typ} | {st} | {ev} | {upd} | `{path}` | {obs_one} |")

    out = ROOT / "docs" / "es" / "PLUGIN_MATRIX.md"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {out} ({len(rows)} rows)")

    print("README.md is now a curated landing page; this script only updates docs/es/PLUGIN_MATRIX.md.")


if __name__ == "__main__":
    main()
