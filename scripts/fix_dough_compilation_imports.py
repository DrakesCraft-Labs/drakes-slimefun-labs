#!/usr/bin/env python3
"""
Reemplaza imports de Dough que apuntan al namespace relocado dentro del jar sombreado
de Slimefun (solo valido en runtime dentro del plugin Slimefun).

En el monorepo, los modulos compilan contra dough-core + slimefun-core: deben usar
dev.drake.dough.* en codigo fuente.

Uso (raiz del repo):
  python scripts/fix_dough_compilation_imports.py
"""
from __future__ import annotations

import sys
from pathlib import Path

OLD = "com.github.drakescraft_labs.slimefun4.libraries.dough"
NEW = "dev.drake.dough"


def main() -> int:
    root = Path(__file__).resolve().parent.parent / "sources"
    if not root.is_dir():
        print("No existe sources/", file=sys.stderr)
        return 1
    n = 0
    for pattern in ("*.java", "*.kt"):
        for path in root.rglob(pattern):
            text = path.read_text(encoding="utf-8")
            if OLD not in text:
                continue
            path.write_text(text.replace(OLD, NEW), encoding="utf-8")
            print(path.relative_to(root.parent))
            n += 1
    print(f"OK: {n} archivos actualizados.", flush=True)
    return 0


if __name__ == "__main__":
    sys.exit(main())
