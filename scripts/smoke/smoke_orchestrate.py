#!/usr/bin/env python3
"""
Orquesta compilación Maven, build de artifacts smoke y ejecución del servidor de prueba.
Evita encadenar muchos comandos a mano en la terminal.

Uso (desde la raíz del repo):
  python scripts/smoke/smoke_orchestrate.py mvn-package
  python scripts/smoke/smoke_orchestrate.py build-artifacts --profile monorepo-all --clean
  python scripts/smoke/smoke_orchestrate.py run-server --profile monorepo-all --clean --timeout 120
  python scripts/smoke/smoke_orchestrate.py run-server --profile foundation-paper-12111 --clean --timeout 180
  python scripts/smoke/smoke_orchestrate.py parse-log --profile monorepo-all
  python scripts/smoke/smoke_orchestrate.py full --profile monorepo-all --clean --timeout 120
  python scripts/smoke/smoke_orchestrate.py mvn-package-pl --pl sources/community-addons/Foo,sources/community-addons/Bar
"""

from __future__ import annotations

import argparse
import re
import shutil
import subprocess
import sys
from pathlib import Path


def repo_root() -> Path:
    return Path(__file__).resolve().parent.parent.parent


def run(cmd: list[str], cwd: Path, *, env: dict | None = None) -> int:
    print("+", " ".join(cmd), flush=True)
    p = subprocess.run(cmd, cwd=str(cwd), env=env)
    return int(p.returncode)


def resolve_mvn() -> str:
    w = shutil.which("mvn")
    if w:
        return w
    fallbacks = [
        Path.home() / "Tools/apache-maven-3.9.9/bin/mvn.cmd",
        Path.home() / "Tools/apache-maven-3.9.9/bin/mvn.bat",
        Path(r"C:\Users\pablo\Tools\apache-maven-3.9.9\bin\mvn.cmd"),
    ]
    for p in fallbacks:
        if p.is_file():
            return str(p)
    raise FileNotFoundError("Maven (mvn) no está en PATH ni en rutas fallback conocidas.")


def mvn_package(root: Path) -> int:
    mvn = resolve_mvn()
    return run(
        [mvn, "-B", "-DskipTests", "package", "-q"],
        root,
    )


def mvn_package_pl(root: Path, modules_csv: str) -> int:
    """Compila solo los módulos indicados (argumento -pl de Maven, rutas relativas al reactor)."""
    mvn = resolve_mvn()
    return run(
        [mvn, "-B", "-DskipTests", "package", "-q", "-pl", modules_csv],
        root,
    )


def resolve_pwsh() -> str:
    w = shutil.which("pwsh")
    if w:
        return w
    w = shutil.which("powershell")
    if w:
        return w
    raise FileNotFoundError("No se encontró pwsh ni powershell en PATH.")


def pwsh_file(root: Path, script: Path, args: list[str]) -> int:
    return run(
        [resolve_pwsh(), "-NoProfile", "-File", str(script.resolve()), *args],
        root,
    )


def parse_log(profile: str) -> int:
    root = repo_root()
    log = root / ".smoke" / profile / "server" / "logs" / "latest.log"
    if not log.is_file():
        print(f"No existe log: {log}", file=sys.stderr)
        return 1
    text = log.read_text(encoding="utf-8", errors="replace")
    pattern = re.compile(r"Error occurred while enabling.*", re.IGNORECASE)
    hits = pattern.findall(text)
    if hits:
        print(f"=== {len(hits)} coincidencias 'Error occurred while enabling' ===")
        for h in hits:
            print(h)
        return 1
    print("OK: no hay líneas 'Error occurred while enabling' en", log)
    return 0


def main() -> int:
    ap = argparse.ArgumentParser(description="Smoke: Maven + artifacts + servidor")
    sub = ap.add_subparsers(dest="cmd", required=True)

    sub.add_parser("mvn-package", help="mvn -B -DskipTests package -q en la raíz del repo")

    mpl = sub.add_parser(
        "mvn-package-pl",
        help="mvn package -q solo para módulos (-pl), ej: sources/community-addons/Foo,sources/community-addons/Bar",
    )
    mpl.add_argument(
        "--pl",
        required=True,
        dest="pl",
        help="Lista separada por comas para Maven -pl (rutas de módulo respecto al pom raíz)",
    )

    b = sub.add_parser("build-artifacts", help="build-smoke-artifacts.ps1")
    b.add_argument("--profile", default="foundation")
    b.add_argument("--clean", action="store_true")

    r = sub.add_parser("run-server", help="run-smoke-server.ps1")
    r.add_argument("--profile", default="foundation")
    r.add_argument(
        "--minecraft",
        default="",
        help="Version de Minecraft para Paper (ej. 1.21.11). Vacío = perfil smoke o 1.21.1 por defecto.",
    )
    r.add_argument("--timeout", type=int, default=120)
    r.add_argument("--clean", action="store_true")
    r.add_argument("--no-build", action="store_true")

    f = sub.add_parser("full", help="mvn package + build artifacts + run smoke")
    f.add_argument("--profile", default="monorepo-all")
    f.add_argument(
        "--minecraft",
        default="",
        help="Version de Minecraft para Paper (ej. 1.21.11). Vacío = perfil smoke o 1.21.1 por defecto.",
    )
    f.add_argument("--timeout", type=int, default=120)
    f.add_argument("--clean", action="store_true")
    f.add_argument("--skip-mvn", action="store_true", help="Saltar mvn package")
    f.add_argument(
        "--skip-build-artifacts",
        action="store_true",
        help="No ejecutar build-smoke-artifacts (asume .smoke/<profile>/artifacts ya generados)",
    )

    p = sub.add_parser("parse-log", help="Buscar errores de enable en latest.log")
    p.add_argument("--profile", default="monorepo-all")

    args = ap.parse_args()
    root = repo_root()
    smoke_dir = root / "scripts" / "smoke"
    build_ps1 = smoke_dir / "build-smoke-artifacts.ps1"
    run_ps1 = smoke_dir / "run-smoke-server.ps1"

    if args.cmd == "mvn-package":
        return mvn_package(root)

    if args.cmd == "mvn-package-pl":
        return mvn_package_pl(root, args.pl)

    if args.cmd == "build-artifacts":
        ps_args = ["-Profile", args.profile]
        if args.clean:
            ps_args.append("-Clean")
        return pwsh_file(root, build_ps1, ps_args)

    if args.cmd == "run-server":
        ps_args = [
            "-Profile",
            args.profile,
            "-TimeoutSeconds",
            str(args.timeout),
        ]
        if getattr(args, "minecraft", ""):
            ps_args.extend(["-MinecraftVersion", args.minecraft])
        if args.clean:
            ps_args.append("-Clean")
        if args.no_build:
            ps_args.append("-NoBuild")
        return pwsh_file(root, run_ps1, ps_args)

    if args.cmd == "parse-log":
        return parse_log(args.profile)

    if args.cmd == "full":
        if not args.skip_mvn:
            c = mvn_package(root)
            if c != 0:
                return c
        if not args.skip_build_artifacts:
            ps_args = ["-Profile", args.profile]
            if args.clean:
                ps_args.append("-Clean")
            c = pwsh_file(root, build_ps1, ps_args)
            if c != 0:
                return c
        ps_args = [
            "-Profile",
            args.profile,
            "-TimeoutSeconds",
            str(args.timeout),
        ]
        if getattr(args, "minecraft", ""):
            ps_args.extend(["-MinecraftVersion", args.minecraft])
        if args.clean:
            ps_args.append("-Clean")
        if args.skip_build_artifacts:
            ps_args.append("-NoBuild")
        return pwsh_file(root, run_ps1, ps_args)

    return 1


if __name__ == "__main__":
    sys.exit(main())
