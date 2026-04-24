#!/usr/bin/env python3
"""
Porteo asistido hacia Paper 1.21.1 / API Bukkit moderna.

Aplica transformaciones textuales conservadoras sobre .java y .kt.
Siempre usar primero --dry-run y revisar el diff antes de --apply.

Ejemplos:
  python scripts/port_paper_121.py --dry-run --path sources/community-addons/Bump
  python scripts/port_paper_121.py --apply --path sources/community-addons/Bump --backup
  python scripts/port_paper_121.py --dry-run --rules libraries-dough,libraries-commons,potion,particle
"""

from __future__ import annotations

import argparse
import os
import re
import sys
from dataclasses import dataclass
from datetime import datetime, timezone
from typing import Callable, Iterable, List, Optional, Tuple

ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DEFAULT_SOURCES = os.path.join(ROOT_DIR, "sources")

SKIP_DIR_NAMES = {".git", "target", "build", ".gradle", "out", "node_modules", ".idea"}


@dataclass(frozen=True)
class TextRule:
    rule_id: str
    description: str
    pattern: re.Pattern[str]
    repl: str | Callable[[re.Match[str]], str]


def _rules() -> List[TextRule]:
    """Reglas ordenadas: primero rutas FQCN largas, luego constantes sueltas."""
    return [
        TextRule(
            "libraries-dough",
            "Ruta legacy slimefun4.libraries.dough.* -> dev.drake.dough.*",
            re.compile(r"com\.github\.drakescraft_labs\.slimefun4\.libraries\.dough\."),
            "dev.drake.dough.",
        ),
        TextRule(
            "libraries-commons",
            "Ruta legacy slimefun4.libraries.commons.lang.* -> org.apache.commons.lang.*",
            re.compile(r"com\.github\.drakescraft_labs\.slimefun4\.libraries\.commons\.lang\."),
            "org.apache.commons.lang.",
        ),
        TextRule(
            "potion",
            "PotionEffectType: constantes renombradas/removidas en API moderna",
            re.compile(
                r"\bPotionEffectType\.("
                r"INCREASE_DAMAGE|FAST_DIGGING|SLOW_DIGGING|DAMAGE_RESISTANCE|"
                r"SLOW|CONFUSION|JUMP|WEAKNESS|WITHER|HUNGER|HEAL|HARM"
                r")\b"
            ),
            _potion_repl,
        ),
        TextRule(
            "particle",
            "Particle: constantes renombradas",
            re.compile(r"\bParticle\.(ENCHANTMENT_TABLE|EXPLOSION_HUGE)\b"),
            _particle_repl,
        ),
    ]


_POTION_MAP = {
    "INCREASE_DAMAGE": "STRENGTH",
    "FAST_DIGGING": "HASTE",
    "SLOW_DIGGING": "MINING_FATIGUE",
    "DAMAGE_RESISTANCE": "RESISTANCE",
    "SLOW": "SLOWNESS",
    "CONFUSION": "NAUSEA",
    "JUMP": "JUMP_BOOST",
    "WEAKNESS": "WEAKNESS",
    "WITHER": "WITHER",
    "HUNGER": "HUNGER",
    "HEAL": "INSTANT_HEALTH",
    "HARM": "INSTANT_DAMAGE",
}


def _potion_repl(m: re.Match[str]) -> str:
    old = m.group(1)
    new = _POTION_MAP.get(old, old)
    return f"PotionEffectType.{new}"


_PARTICLE_MAP = {
    "ENCHANTMENT_TABLE": "ENCHANT",
    "EXPLOSION_HUGE": "EXPLOSION",
}


def _particle_repl(m: re.Match[str]) -> str:
    old = m.group(1)
    new = _PARTICLE_MAP.get(old, old)
    return f"Particle.{new}"


def iter_source_files(base: str) -> Iterable[str]:
    for dirpath, dirnames, filenames in os.walk(base):
        dirnames[:] = [d for d in dirnames if d not in SKIP_DIR_NAMES]
        for name in filenames:
            if name.endswith(".java") or name.endswith(".kt"):
                yield os.path.join(dirpath, name)


def load_rule_ids() -> List[str]:
    return [r.rule_id for r in _rules()]


def select_rules(ids: Optional[List[str]]) -> List[TextRule]:
    all_rules = _rules()
    if not ids:
        return all_rules
    by_id = {r.rule_id: r for r in all_rules}
    missing = [i for i in ids if i not in by_id]
    if missing:
        print(f"ERROR: reglas desconocidas: {missing}. Usa --list-rules.", file=sys.stderr)
        sys.exit(2)
    return [by_id[i] for i in ids]


def apply_to_content(content: str, rules: List[TextRule]) -> Tuple[str, List[Tuple[str, int]]]:
    """Devuelve (nuevo_texto, [(rule_id, delta_matches), ...]) por regla con al menos un cambio."""
    out = content
    per_rule: List[Tuple[str, int]] = []
    for rule in rules:
        if isinstance(rule.repl, str):
            new_out, n = rule.pattern.subn(rule.repl, out)
        else:
            new_out, n = rule.pattern.subn(rule.repl, out)
        if n:
            per_rule.append((rule.rule_id, n))
            out = new_out
    return out, per_rule


def process_file(
    path: str,
    rules: List[TextRule],
    dry_run: bool,
    backup: bool,
) -> Optional[List[Tuple[str, int]]]:
    with open(path, "r", encoding="utf-8", errors="replace") as f:
        original = f.read()
    new_content, hits = apply_to_content(original, rules)
    if new_content == original:
        return None
    if dry_run:
        return hits
    if backup:
        with open(path + ".portbak", "w", encoding="utf-8") as b:
            b.write(original)
    with open(path, "w", encoding="utf-8", newline="") as f:
        f.write(new_content)
    return hits


def main() -> None:
    parser = argparse.ArgumentParser(description="Porteo asistido Paper 1.21.1 (parches textuales auditables).")
    parser.add_argument(
        "--root",
        default=ROOT_DIR,
        help="Raiz del monorepo (default: directorio padre de scripts/).",
    )
    parser.add_argument(
        "--path",
        action="append",
        default=[],
        help="Ruta relativa bajo --root a procesar (se puede repetir). Por defecto: sources/.",
    )
    parser.add_argument("--dry-run", action="store_true", help="Solo informar; no escribir archivos.")
    parser.add_argument("--apply", action="store_true", help="Aplicar cambios (mutuamente excluyente con dry-run).")
    parser.add_argument("--backup", action="store_true", help="Con --apply, guarda copia .portbak por archivo.")
    parser.add_argument(
        "--rules",
        type=str,
        default="",
        help="Lista separada por comas de IDs de regla (default: todas). Ver --list-rules.",
    )
    parser.add_argument("--list-rules", action="store_true", help="Listar IDs y descripciones, y salir.")
    parser.add_argument(
        "--report",
        type=str,
        default="",
        help="Opcional: escribe un resumen en texto plano a esta ruta (relativa a --root si no es absoluta).",
    )
    args = parser.parse_args()

    if args.list_rules:
        for r in _rules():
            print(f"{r.rule_id}\t{r.description}")
        return

    if args.dry_run and args.apply:
        print("ERROR: usa solo --dry-run o solo --apply.", file=sys.stderr)
        sys.exit(2)
    if not args.dry_run and not args.apply:
        print("ERROR: indica --dry-run o --apply.", file=sys.stderr)
        sys.exit(2)

    rule_ids = [x.strip() for x in args.rules.split(",") if x.strip()] if args.rules else None
    rules = select_rules(rule_ids)

    roots: List[str] = []
    if args.path:
        for p in args.path:
            roots.append(os.path.normpath(os.path.join(args.root, p)))
    else:
        roots.append(os.path.normpath(os.path.join(args.root, "sources")))

    for base in roots:
        if not os.path.isdir(base):
            print(f"ERROR: no es directorio: {base}", file=sys.stderr)
            sys.exit(2)

    dry_run = args.dry_run
    total_files = 0
    changed_files = 0
    report_lines: List[str] = []
    ts = datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")
    report_lines.append(f"# port_paper_121 report {ts}")
    report_lines.append(f"# mode={'dry-run' if dry_run else 'apply'}")
    report_lines.append(f"# rules={','.join(r.rule_id for r in rules)}")
    report_lines.append("")

    for base in roots:
        for path in iter_source_files(base):
            total_files += 1
            rel = os.path.relpath(path, args.root)
            try:
                hits = process_file(path, rules, dry_run=dry_run, backup=args.backup)
            except OSError as e:
                print(f"ERROR leyendo/escribiendo {rel}: {e}", file=sys.stderr)
                sys.exit(1)
            if hits:
                changed_files += 1
                detail = ", ".join(f"{rid}*{n}" for rid, n in hits)
                line = f"{rel}\t{detail}"
                print(line)
                report_lines.append(line)

    summary = (
        f"Archivos escaneados: {total_files}, modificados: {changed_files} "
        f"({'simulacion' if dry_run else 'escritos'})"
    )
    print(summary)
    report_lines.append("")
    report_lines.append(summary)

    if args.report:
        report_path = args.report if os.path.isabs(args.report) else os.path.join(args.root, args.report)
        os.makedirs(os.path.dirname(report_path) or ".", exist_ok=True)
        with open(report_path, "w", encoding="utf-8") as rf:
            rf.write("\n".join(report_lines) + "\n")
        print(f"Reporte: {report_path}")


if __name__ == "__main__":
    main()
