#!/usr/bin/env python3
"""
Alinea el tipo Interaction con el API que ven los addons al compilar contra el jar
de Slimefun **ya empaquetado** (shade reloca dough dentro de Slimefun).

Maven ejecuta el lifecycle por modulo: el `package` de Slimefun (shade) termina
antes del `compile` de los addons, asi que `Slimefun.getProtectionManager()` expone
`com.github.drakescraft_labs.slimefun4.libraries.dough.protection.*` en el classpath.

NO modificar `sources/slimefun-core` ni `sources/dough-core`: alli el codigo fuente
debe seguir usando `dev.drake.dough`.

Uso (raiz del repo):
  python scripts/fix_dough_compilation_imports.py
"""
from __future__ import annotations

import sys
from pathlib import Path

OLD_INTERACTION = "dev.drake.dough.protection.Interaction"
NEW_INTERACTION = "com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction"
OLD_PM_IMPORT = "import dev.drake.dough.protection.ProtectionManager;"
NEW_PM_IMPORT = (
    "import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.ProtectionManager;"
)

SKIP_PREFIXES = (
    "sources" + "/" + "slimefun-core",
    "sources" + "/" + "dough-core",
    # Librerías internas: compilan contra dough-core sin jar Slimefun sombreado
    "sources/batch-2-expansion/SefiLib",
    "sources/batch-2-expansion/InfinityLib",
)


def should_skip(rel_posix: str) -> bool:
    return any(rel_posix.startswith(p) for p in SKIP_PREFIXES)


def main() -> int:
    root = Path(__file__).resolve().parent.parent
    sources = root / "sources"
    if not sources.is_dir():
        print("No existe sources/", file=sys.stderr)
        return 1
    n = 0
    for pattern in ("*.java", "*.kt"):
        for path in sources.rglob(pattern):
            rel = path.relative_to(root).as_posix()
            if should_skip(rel):
                continue
            text = path.read_text(encoding="utf-8")
            orig = text
            text = text.replace(OLD_INTERACTION, NEW_INTERACTION)
            text = text.replace(OLD_PM_IMPORT, NEW_PM_IMPORT)
            if text == orig:
                continue
            path.write_text(text, encoding="utf-8")
            print(rel)
            n += 1
    print(f"OK: {n} archivos (Interaction / ProtectionManager Slimefun shaded).", flush=True)
    return 0


if __name__ == "__main__":
    sys.exit(main())
