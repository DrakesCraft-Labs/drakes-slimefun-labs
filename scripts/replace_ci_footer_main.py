#!/usr/bin/env python3
"""Reemplaza footers DRAKES-STATUS que citan 1.21-latin por main (rama canonica)."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SKIP_DIRS = {".git", "node_modules", "target", "build", ".gradle", ".m2"}

REPLACEMENTS = [
    (re.compile(r"CI principal en `1\.21-latin`"), "CI principal en `main`"),
    (re.compile(r"rama activa[:\s]+`?1\.21-latin`?", re.I), "rama activa: `main`"),
    (re.compile(r"Stable branch: 1\.21-latin"), "Stable branch: main"),
    (re.compile(r"\*\*`1\.21-latin`\*\*"), "**`main`**"),
    (re.compile(r"on `1\.21-latin`"), "on `main`"),
    (re.compile(r'branches: \["main", "1\.21-latin"\]'), 'branches: ["main"]'),
    (re.compile(r"branches: \[\"1\.21-latin\"\]"), 'branches: ["main"]'),
]

TEXT_EXTS = {".md", ".yml", ".yaml", ".py", ".txt"}


def should_skip(path: Path) -> bool:
    return any(part in SKIP_DIRS for part in path.parts)


def process_file(path: Path) -> bool:
    try:
        text = path.read_text(encoding="utf-8")
    except (UnicodeDecodeError, OSError):
        return False
    original = text
    for pattern, repl in REPLACEMENTS:
        text = pattern.sub(repl, text)
    if text != original:
        path.write_text(text, encoding="utf-8", newline="\n")
        return True
    return False


def main() -> None:
    changed = 0
    for path in ROOT.rglob("*"):
        if not path.is_file() or should_skip(path) or path.suffix not in TEXT_EXTS:
            continue
        if process_file(path):
            changed += 1
            print(path.relative_to(ROOT))
    print(f"[DONE] {changed} archivos actualizados")


if __name__ == "__main__":
    main()
