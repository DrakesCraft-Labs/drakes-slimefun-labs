#!/usr/bin/env python3
"""
Sustituye org.graalvm.js:js (metapaquete Enterprise → truffle-enterprise en Maven Central)
por org.graalvm.js:js-community (stack público: js-language + truffle-runtime).

Evita CI/servidores con JAR corrupto o artefacto no resoluble para `truffle-enterprise`.

Uso (raíz del repo):
  python scripts/fix_graalvm_js_community_poms.py
  python scripts/fix_graalvm_js_community_poms.py --dry-run
  python scripts/fix_graalvm_js_community_poms.py --audit   # solo lista POMs con js enterprise
"""
from __future__ import annotations

import argparse
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

# Tras groupId org.graalvm.js, solo el artefacto exacto "js" (no js-language, js-community, etc.)
ENTERPRISE_JS = re.compile(
    r"(<groupId>org\.graalvm\.js</groupId>\s*<artifactId>)js(</artifactId>)",
    re.MULTILINE,
)


def valid_xml(text: str) -> bool:
    try:
        ET.fromstring(text)
        return True
    except ET.ParseError:
        return False


def process_pom(path: Path, dry_run: bool) -> bool:
    raw = path.read_text(encoding="utf-8")
    new = ENTERPRISE_JS.sub(r"\1js-community\2", raw)
    if new == raw:
        return False
    if not valid_xml(new):
        print(f"ABORT: XML inválido tras reemplazo: {path}", file=sys.stderr)
        sys.exit(1)
    if not dry_run:
        path.write_text(new, encoding="utf-8")
    return True


def pom_paths(root: Path) -> list[Path]:
    out = [root / "pom.xml"] if (root / "pom.xml").is_file() else []
    sources = root / "sources"
    if sources.is_dir():
        out.extend(sorted(sources.rglob("pom.xml")))
    return out


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--dry-run", action="store_true", help="No escribe archivos")
    ap.add_argument("--audit", action="store_true", help="Lista POMs que aún tienen js enterprise")
    args = ap.parse_args()

    root = Path(__file__).resolve().parent.parent
    paths = pom_paths(root)
    hits_audit: list[str] = []

    for p in paths:
        text = p.read_text(encoding="utf-8")
        if ENTERPRISE_JS.search(text):
            hits_audit.append(p.relative_to(root).as_posix())

    if args.audit:
        if not hits_audit:
            print("OK: no hay pom.xml con org.graalvm.js + artifactId js (enterprise).")
            return 0
        print("POMs con dependencia GraalVM js (enterprise):")
        for h in hits_audit:
            print(f"  {h}")
        return 1 if hits_audit else 0

    changed = 0
    for p in paths:
        if process_pom(p, args.dry_run):
            print(p.relative_to(root).as_posix())
            changed += 1
    suffix = " (dry-run)" if args.dry_run else ""
    print(f"OK: {changed} archivo(s) actualizados{suffix}.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
