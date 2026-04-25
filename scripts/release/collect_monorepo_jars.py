#!/usr/bin/env python3
"""
Copia un jar por módulo Maven del reactor (rutas <module> del pom raíz) hacia un directorio plano
para empaquetar releases (ZIP / GitHub Release).

Uso (desde la raíz del repo, tras mvn package):
  python scripts/release/collect_monorepo_jars.py --out dist/monorepo-jars
"""
from __future__ import annotations

import argparse
import json
import re
import shutil
import sys
from pathlib import Path


def skip_jar(name: str) -> bool:
    n = name.lower()
    if n.startswith("original-"):
        return True
    for sfx in ("-sources.jar", "-javadoc.jar", "-tests.jar", "-plain.jar"):
        if n.endswith(sfx):
            return True
    return False


def repo_root() -> Path:
    return Path(__file__).resolve().parent.parent.parent


def parse_modules(root_pom: Path) -> list[str]:
    text = root_pom.read_text(encoding="utf-8", errors="replace")
    return [m.strip() for m in re.findall(r"<module>([^<]+)</module>", text)]


def pick_jar(target_dir: Path) -> Path | None:
    if not target_dir.is_dir():
        return None
    jars = [p for p in target_dir.glob("*.jar") if p.is_file() and not skip_jar(p.name)]
    if not jars:
        return None
    jars.sort(key=lambda p: p.stat().st_mtime_ns, reverse=True)
    return jars[0]


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--out", type=Path, required=True, help="Directorio de salida (se crea).")
    ap.add_argument(
        "--root-pom",
        type=Path,
        default=None,
        help="pom.xml raíz (por defecto repo/pom.xml).",
    )
    args = ap.parse_args()
    root = repo_root()
    pom = args.root_pom or (root / "pom.xml")
    out = args.out
    if not out.is_absolute():
        out = root / out
    out.mkdir(parents=True, exist_ok=True)

    manifest: list[dict[str, str]] = []
    missing: list[str] = []

    for mod in parse_modules(pom):
        mod_path = root / mod
        jar = pick_jar(mod_path / "target")
        if jar is None:
            libs = mod_path / "build" / "libs"
            if libs.is_dir():
                cand = [p for p in libs.glob("*.jar") if p.is_file() and not skip_jar(p.name)]
                if cand:
                    cand.sort(key=lambda p: p.stat().st_mtime_ns, reverse=True)
                    jar = cand[0]
        if jar is None:
            missing.append(mod)
            continue
        dest = out / jar.name
        if dest.exists() and dest.samefile(jar):
            continue
        if dest.exists():
            dest.unlink()
        shutil.copy2(jar, dest)
        manifest.append({"module": mod, "jar": jar.name})

    (out / "manifest.json").write_text(
        json.dumps({"jars": manifest, "missing_modules": missing}, indent=2),
        encoding="utf-8",
    )
    if missing:
        print(f"WARN: sin jar en {len(missing)} módulos (no construidos o sin target):", file=sys.stderr)
        for m in missing[:30]:
            print(f"  - {m}", file=sys.stderr)
        if len(missing) > 30:
            print(f"  ... y {len(missing) - 30} más", file=sys.stderr)
    print(f"OK: {len(manifest)} jars en {out}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
