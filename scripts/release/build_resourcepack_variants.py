#!/usr/bin/env python3
"""Genera variantes derivadas del resource pack principal.

La variante `sf-only` conserva los assets necesarios para Slimefun y addons
relacionados, pero omite la capa de texturas vanilla recoloreadas.
"""

from __future__ import annotations

import argparse
import shutil
import sys
from pathlib import Path


CUSTOM_TOKENS = ("slimefun/", "jackstar/", "minetorio/", "slimefun:", "jackstar:", "minetorio:")
ROOT_FILES = ("pack.png",)
MINECRAFT_DIRS = (
    "citresewn",
    "optifine/cit",
    "variants-cit",
    "models/slimefun",
    "models/jackstar",
    "models/minetorio",
    "textures/item/racingsky",
    "textures/entity/equipment/humanoid/slimefun",
    "textures/entity/equipment/humanoid/racingsky",
    "textures/entity/equipment/humanoid_leggings/slimefun",
    "textures/entity/equipment/humanoid_leggings/racingsky",
    "textures/entity/equipment/wings/slimefun",
    "textures/entity/equipment/wings/racingsky",
)
MINECRAFT_FILES = (
    "textures/models/armor/leather_layer_1_overlay.png",
    "textures/models/armor/leather_layer_2_overlay.png",
)
TOP_LEVEL_NAMESPACES = ("slimefun", "jackstar", "minetorio")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Build derived resource-pack variants.")
    parser.add_argument("source", type=Path, help="Ruta del pack fuente")
    parser.add_argument(
        "--output-root",
        type=Path,
        default=None,
        help="Directorio donde se escribirán las variantes (default: PortTextura/build)",
    )
    return parser.parse_args()


def copy_tree(src: Path, dst: Path) -> None:
    if not src.exists():
        return
    shutil.copytree(src, dst, dirs_exist_ok=True)


def copy_file(src: Path, dst: Path) -> None:
    if not src.exists():
        return
    dst.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src, dst)


def should_keep_json(path: Path) -> bool:
    try:
        content = path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        content = path.read_text(encoding="utf-8", errors="ignore")
    return any(token in content for token in CUSTOM_TOKENS)


def write_pack_meta(source_pack: Path, target_pack: Path) -> None:
    original = (source_pack / "pack.mcmeta").read_text(encoding="utf-8")
    mixed = "§eJackstar §7· §fDrakesCraft Labs §8| §aSlimefun Completo §8& §dVanilla Coronalis 32x §8| §71.21-1.21.1"
    sf_only = "§eJackstar §7· §fDrakesCraft Labs §8| §aSlimefun SF-Only §8| §7Sin overrides vanilla §8| §71.21-1.21.1"
    target_pack.joinpath("pack.mcmeta").write_text(original.replace(mixed, sf_only), encoding="utf-8")


def build_sf_only(source_pack: Path, output_root: Path) -> Path:
    target_pack = output_root / "Jackstar-Slimefun-Drakecraft-sf-only"
    if target_pack.exists():
        shutil.rmtree(target_pack)
    target_pack.mkdir(parents=True)

    write_pack_meta(source_pack, target_pack)

    for root_name in ROOT_FILES:
        copy_file(source_pack / root_name, target_pack / root_name)

    for namespace in TOP_LEVEL_NAMESPACES:
        copy_tree(source_pack / "assets" / namespace, target_pack / "assets" / namespace)

    minecraft_src = source_pack / "assets" / "minecraft"
    minecraft_dst = target_pack / "assets" / "minecraft"
    for rel_dir in MINECRAFT_DIRS:
        copy_tree(minecraft_src / rel_dir, minecraft_dst / rel_dir)
    for rel_file in MINECRAFT_FILES:
        copy_file(minecraft_src / rel_file, minecraft_dst / rel_file)

    for rel_dir in ("items", "models/item"):
        src_dir = minecraft_src / rel_dir
        dst_dir = minecraft_dst / rel_dir
        for path in sorted(src_dir.glob("*.json")):
            if should_keep_json(path):
                copy_file(path, dst_dir / path.name)

    return target_pack


def main() -> int:
    try:
        args = parse_args()
        source_pack = args.source.resolve()
        if not source_pack.exists():
            raise FileNotFoundError(f"No existe el pack fuente: {source_pack}")

        output_root = args.output_root.resolve() if args.output_root else source_pack.parent / "build"
        output_root.mkdir(parents=True, exist_ok=True)

        target_pack = build_sf_only(source_pack, output_root)
        print(f"[SUCCESS] Variante generada: {target_pack}")
        return 0
    except Exception as exc:  # pragma: no cover - CLI defensive path
        print(f"[ERROR] {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
