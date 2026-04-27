#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Añade la dependencia drakes-labs-autoupdate y una llamada a
DrakesLabsReleaseUpdate.schedule(this, "<artifactId>") en onEnable/enable
de cada módulo plugin del reactor (Maven con plugin.yml y clase main Java/Kotlin).

Uso (desde la raíz del repo):
  python scripts/inject_drakes_autoupdate.py
  python scripts/inject_drakes_autoupdate.py --dry-run
"""

from __future__ import annotations

import argparse
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

# Módulos que no son plugins Slimefun o ya llevan otra política de build
SKIP_SUBSTRINGS = (
    "dough-core",
    "Slimefun4-src",
    "/SefiLib",
    "/InfinityLib",
    "commons-lang-drake-patched",
    "drakes-labs-autoupdate",
    "/ExtraUtils",  # módulo utilitario sin plugin.yml main
)

MARKER_SOURCE = "DrakesLabsReleaseUpdate.schedule"
MARKER_POM = "<artifactId>drakes-labs-autoupdate</artifactId>"

ENTRY_PATTERNS = (
    (r"(public\s+void\s+onEnable\s*\(\s*\)\s*\{)\s*\n", "java"),
    (r"(protected\s+void\s+enable\s*\(\s*\)\s*\{)\s*\n", "java"),
    (r"(override\s+fun\s+onEnable\s*\(\s*\)\s*\{)\s*\n", "kotlin"),
    (r"(override\s+fun\s+enable\s*\(\s*\)\s*\{)\s*\n", "kotlin"),
)

DEP_BLOCK = """        <dependency>
            <groupId>com.github.drakescraft_labs</groupId>
            <artifactId>drakes-labs-autoupdate</artifactId>
        </dependency>
"""


def local_tag(tag: str) -> str:
    return tag.split("}")[-1] if "}" in tag else tag


def parse_modules(pom_path: Path) -> list[str]:
    tree = ET.parse(pom_path)
    root = tree.getroot()
    mods: list[str] = []
    for el in root:
        if local_tag(el.tag) == "modules":
            for m in el:
                if local_tag(m.tag) == "module" and m.text:
                    mods.append(m.text.strip())
    return mods


def should_skip_module(module_rel: str) -> bool:
    return any(s in module_rel.replace("\\", "/") for s in SKIP_SUBSTRINGS)


def project_artifact_id(pom_path: Path) -> str | None:
    tree = ET.parse(pom_path)
    root = tree.getroot()
    for el in root:
        if local_tag(el.tag) == "artifactId" and el.text:
            return el.text.strip()
    return None


def read_main_from_plugin_yml(mod_dir: Path) -> str | None:
    for name in ("plugin.yml", "paper-plugin.yml"):
        yml = mod_dir / "src" / "main" / "resources" / name
        if not yml.is_file():
            continue
        text = yml.read_text(encoding="utf-8", errors="replace")
        m = re.search(r"^main:\s*(\S+)\s*$", text, re.MULTILINE)
        if m:
            return m.group(1).strip()
    return None


def find_main_source_file(mod_dir: Path, main_class: str) -> Path | None:
    rel_java = main_class.replace(".", "/") + ".java"
    rel_kt = main_class.replace(".", "/") + ".kt"
    for base in ("src/main/java", "src/main/kotlin"):
        for rel in (rel_java, rel_kt):
            p = mod_dir / base / rel
            if p.is_file():
                return p
    simple = main_class.rsplit(".", 1)[-1]
    for p in mod_dir.rglob(simple + ".java"):
        if "src" in p.parts:
            return p
    for p in mod_dir.rglob(simple + ".kt"):
        if "src" in p.parts:
            return p
    return None


def inject_call(source: str, artifact_id: str, lang: str) -> tuple[str, bool]:
    if MARKER_SOURCE in source:
        return source, False
    call_line = f'        DrakesLabsReleaseUpdate.schedule(this, "{artifact_id}");'
    if lang == "kotlin":
        call_line = f'        DrakesLabsReleaseUpdate.schedule(this, "{artifact_id}")'

    for pat, kind in ENTRY_PATTERNS:
        if kind != lang:
            continue
        m = re.search(pat, source)
        if not m:
            continue
        pos = m.end(1)
        insert = "\n" + call_line + "\n"
        return source[:pos] + insert + source[pos:], True
    return source, False


def add_import(source: str, ext: str) -> tuple[str, bool]:
    if "import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate" in source:
        return source, False
    if ext == ".kt":
        m = re.search(r"^package\s+[^\n]+\n", source, re.MULTILINE)
        if not m:
            return source, False
        pos = m.end()
        block = "\nimport com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate\n"
        return source[:pos] + block + source[pos:], True
    m = re.search(r"^package\s+[^;]+;\s*\n", source, re.MULTILINE)
    if not m:
        return source, False
    pos = m.end()
    block = "\nimport com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;\n"
    return source[:pos] + block + source[pos:], True


def ensure_pom_dependency(pom_text: str) -> tuple[str, bool]:
    if MARKER_POM in pom_text:
        return pom_text, False
    idx = pom_text.find("<dependencies>")
    if idx == -1:
        return pom_text, False
    ins = pom_text.find(">", idx) + 1
    return pom_text[:ins] + "\n" + DEP_BLOCK + pom_text[ins:], True


def detect_lang(path: Path) -> str:
    return "kotlin" if path.suffix == ".kt" else "java"


def process_module(mod_dir: Path, dry_run: bool) -> dict[str, str | bool]:
    result: dict[str, str | bool] = {"dir": str(mod_dir), "ok": False}
    pom = mod_dir / "pom.xml"
    if not pom.is_file():
        result["reason"] = "sin pom.xml"
        return result
    main_c = read_main_from_plugin_yml(mod_dir)
    if not main_c:
        result["reason"] = "sin plugin.yml main"
        return result
    src = find_main_source_file(mod_dir, main_c)
    if not src:
        result["reason"] = f"sin fuente para {main_c}"
        return result

    aid = project_artifact_id(pom)
    if not aid:
        result["reason"] = "sin artifactId"
        return result

    lang = detect_lang(src)
    body = src.read_text(encoding="utf-8", errors="replace")
    new_body, changed_call = inject_call(body, aid, lang)
    if not changed_call:
        if MARKER_SOURCE in body:
            result["reason"] = "ya tenía schedule"
        else:
            result["reason"] = "no encontré onEnable/enable"
        # igual puede faltar dependencia o import
    new_body, changed_imp = add_import(new_body, src.suffix)
    pom_text = pom.read_text(encoding="utf-8", errors="replace")
    new_pom, changed_pom = ensure_pom_dependency(pom_text)

    if dry_run:
        result["ok"] = True
        result["would_change"] = changed_call or changed_imp or changed_pom
        result["artifact"] = aid
        return result

    if changed_call or changed_imp:
        src.write_text(new_body, encoding="utf-8", newline="\n")
    if changed_pom:
        pom.write_text(new_pom, encoding="utf-8", newline="\n")

    result["ok"] = True
    result["changed"] = changed_call or changed_imp or changed_pom
    result["artifact"] = aid
    return result


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--dry-run", action="store_true")
    args = ap.parse_args()

    modules = parse_modules(ROOT / "pom.xml")
    stats = {"processed": 0, "skipped": 0, "failed": 0, "changed": 0}

    for rel in modules:
        if should_skip_module(rel):
            stats["skipped"] += 1
            continue
        mod_dir = ROOT / rel
        if not mod_dir.is_dir():
            stats["skipped"] += 1
            continue
        stats["processed"] += 1
        r = process_module(mod_dir, args.dry_run)
        if not r.get("ok"):
            stats["failed"] += 1
            print(f"[FALLO] {rel}: {r.get('reason', '?')}", file=sys.stderr)
            continue
        if args.dry_run:
            if r.get("would_change"):
                stats["changed"] += 1
                print(f"[dry-run] {rel} ({r.get('artifact')})")
        else:
            if r.get("changed"):
                stats["changed"] += 1
                print(f"[OK] {rel} ({r.get('artifact')})")

    print(
        f"Resumen: módulos revisados={stats['processed']}, "
        f"omitidos={stats['skipped']}, fallos={stats['failed']}, "
        f"{'cambiarían' if args.dry_run else 'modificados'}={stats['changed']}"
    )
    return 1 if stats["failed"] else 0


if __name__ == "__main__":
    sys.exit(main())
