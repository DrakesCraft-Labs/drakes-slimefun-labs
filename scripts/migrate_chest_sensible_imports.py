from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
TARGETS = [
    ROOT / "sources" / "repos-to-port" / "ChestTerminal-sf5" / "src" / "main" / "java",
    ROOT / "sources" / "repos-to-port" / "SensibleToolbox-sf5" / "src" / "main" / "java",
]

REPLACEMENTS = [
    ("io.github.thebusybiscuit.slimefun4.libraries.dough", "com.github.drakescraft_labs.slimefun4.libraries.dough"),
    ("io.github.thebusybiscuit.slimefun4", "com.github.drakescraft_labs.slimefun4"),
    ("me.mrCookieSlime.Slimefun", "com.github.drakescraft_labs.slimefun4.legacy.Slimefun"),
    ("com.github.drakescraft_labs.slimefun4.legacy.Slimefun.api", "com.github.drakescraft_labs.slimefun4.legacy.api"),
    ("com.github.drakescraft_labs.slimefun4.legacy.Slimefun.Objects", "com.github.drakescraft_labs.slimefun4.legacy.Objects"),
]


def patch_file(path: Path) -> bool:
    original = path.read_text(encoding="utf-8")
    patched = original
    for old, new in REPLACEMENTS:
        patched = patched.replace(old, new)
    if patched != original:
        path.write_text(patched, encoding="utf-8")
        return True
    return False


def main() -> None:
    changed = 0
    scanned = 0
    for target in TARGETS:
        for java_file in target.rglob("*.java"):
            scanned += 1
            if patch_file(java_file):
                changed += 1
    print(f"Scanned {scanned} files, changed {changed}.")


if __name__ == "__main__":
    main()
