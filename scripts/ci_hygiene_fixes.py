#!/usr/bin/env python3
"""
Ejecuta, en orden, los arreglos idempotentes pensados para CI / reactor Maven:

  1. fix_graalvm_js_community_poms.py — evita org.graalvm.js:js (enterprise / truffle-enterprise)
  2. fix_dough_compilation_imports.py — imports Interaction/ProtectionManager vs Slimefun sombreado

Pasar argumentos extra a cada script no está soportado; usar los scripts sueltos si hace falta.

Uso (raíz del repo):
  python scripts/ci_hygiene_fixes.py
  python scripts/ci_hygiene_fixes.py --dry-run
"""
from __future__ import annotations

import argparse
import subprocess
import sys
from pathlib import Path


def run(py: Path, args: list[str]) -> int:
    r = subprocess.run([sys.executable, str(py)] + args, cwd=py.parent.parent)
    return int(r.returncode)


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--dry-run", action="store_true")
    args = ap.parse_args()

    root = Path(__file__).resolve().parent.parent
    scripts = root / "scripts"

    for name in ("fix_graalvm_js_community_poms.py", "fix_dough_compilation_imports.py"):
        path = scripts / name
        if not path.is_file():
            print(f"Falta {path}", file=sys.stderr)
            return 1
        if name == "fix_dough_compilation_imports.py" and args.dry_run:
            print("(dry-run) omitido fix_dough_compilation_imports.py (no soporta --dry-run)")
            continue
        script_args = ["--dry-run"] if args.dry_run and name == "fix_graalvm_js_community_poms.py" else []
        code = run(path, script_args)
        if code != 0:
            return code
    return 0


if __name__ == "__main__":
    sys.exit(main())
