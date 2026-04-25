#!/usr/bin/env python3
"""
Antepone un aviso de rama a archivos .md (uso en 26.X-ToTheStars).

Excluye rutas que se mantienen editadas a mano (README raíz, docs/README,
docs/paper-26-base). Idempotente: no duplica si ya existe el marcador HTML.
"""
from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MARKER = "<!-- drakes-labs:branch-26x-notice -->"
NOTICE = f"""{MARKER}

> **Rama `26.X-ToTheStars` — Minecraft / Paper 26.x:** porte hacia la API **Paper 26.x** (p. ej. artefactos `26.1.x.build.*-alpha` en repo.papermc.io). Por defecto el `pom.xml` raíz sigue con **`paper.version=1.21.1-R0.1-SNAPSHOT`**; para compilar contra la API 26.x: `mvn -B -DskipTests -Ppaper-26-preview compile -fae`. La línea estable **Paper 1.21.x**, CI y smoke de referencia están en la rama **`1.21-latin`**. Guía: `docs/paper-26-base.md`.

"""

SKIP_RELATIVE = frozenset(
    {
        Path("README.md"),
        Path("docs/README.md"),
        Path("docs/paper-26-base.md"),
    }
)


def should_skip(path: Path) -> bool:
    rel = path.relative_to(ROOT)
    if rel in SKIP_RELATIVE:
        return True
    if ".git" in rel.parts:
        return True
    return False


def main() -> int:
    patched = 0
    for path in sorted(ROOT.rglob("*.md")):
        if not path.is_file() or should_skip(path):
            continue
        text = path.read_text(encoding="utf-8")
        if MARKER in text[:2000]:
            continue
        path.write_text(NOTICE + text, encoding="utf-8", newline="\n")
        patched += 1
    print(f"OK: aviso 26.x anteponido en {patched} archivos .md")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
