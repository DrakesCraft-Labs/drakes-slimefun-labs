from pathlib import Path
import shutil


ROOT = Path(__file__).resolve().parents[1]
LOCAL = ROOT / "sources" / "repos-to-port" / "DynaTech"
UPSTREAM = ROOT / "sources" / "upstream-sf5" / "DynaTech"


def replace_all(path: Path, replacements: list[tuple[str, str]]) -> None:
    text = path.read_text(encoding="utf-8")
    out = text
    for old, new in replacements:
        out = out.replace(old, new)
    if out != text:
        path.write_text(out, encoding="utf-8")


def main() -> None:
    local_main = LOCAL / "src" / "main"
    upstream_main = UPSTREAM / "src" / "main"

    if local_main.exists():
        shutil.rmtree(local_main)
    shutil.copytree(upstream_main, local_main)

    java_files = list((local_main / "java").rglob("*.java"))
    replacements = [
        ("io.github.thebusybiscuit.slimefun4.libraries.dough", "dev.drake.dough"),
        ("io.github.thebusybiscuit.slimefun4", "com.github.drakescraft_labs.slimefun4"),
        ("me.mrCookieSlime.Slimefun", "com.github.drakescraft_labs.slimefun4.legacy.Slimefun"),
    ]
    for file in java_files:
        replace_all(file, replacements)

    plugin_yml = local_main / "resources" / "plugin.yml"
    if plugin_yml.exists():
        replace_all(
            plugin_yml,
            [
                ("version: ${version}", "version: ${project.version}"),
                ("api-version: '1.16'", "api-version: \"1.21\""),
            ],
        )

    dyna = local_main / "java" / "me" / "profelements" / "dynatech" / "DynaTech.java"
    text = dyna.read_text(encoding="utf-8")
    if "DrakesLabsReleaseUpdate" not in text:
        text = text.replace(
            "package me.profelements.dynatech;\n\n",
            "package me.profelements.dynatech;\n\nimport com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;\n",
        )
        text = text.replace(
            "protected void enable() {\n",
            "protected void enable() {\n        DrakesLabsReleaseUpdate.schedule(this, \"DynaTech-drake\");\n",
        )
        dyna.write_text(text, encoding="utf-8")

    print("DynaTech strong migration applied.")


if __name__ == "__main__":
    main()
