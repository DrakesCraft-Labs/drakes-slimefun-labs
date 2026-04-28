import json
import subprocess
from dataclasses import dataclass
from datetime import datetime, timezone
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
UPSTREAM_DIR = ROOT / "sources" / "upstream-sf5"
REPORT_PATH = ROOT / "sources" / "internal-metadata" / "SLIMEFUN5_SYNC_REPORT.md"


@dataclass(frozen=True)
class Mapping:
    repo: str
    local_path: str


MAPPINGS = [
    Mapping("DynaTech", "sources/repos-to-port/DynaTech"),
    Mapping("InfinityExpansion", "sources/repos-to-port/InfinityExpansion"),
    Mapping("FluffyMachines", "sources/repos-to-port/FluffyMachines"),
    Mapping("LiteXpansion", "sources/batch-2-expansion/LiteXpansion"),
    Mapping("MissileWarfare", "sources/community-addons/MissileWarfare"),
    Mapping("ExoticGarden", "sources/repos-to-port/ExoticGarden"),
    Mapping("ExtraGear", "sources/repos-to-port/ExtraGear"),
    Mapping("SlimefunAdvancements", "sources/community-addons/SlimefunAdvancements"),
    Mapping("SlimeTinker", "sources/batch-2-expansion/SlimeTinker"),
    Mapping("ChestTerminal", "sources/repos-to-port/ChestTerminal-sf5"),
    Mapping("LuckyBlocks", "sources/repos-to-port/LuckyBlocks-sf5"),
    Mapping("SensibleToolbox", "sources/repos-to-port/SensibleToolbox-sf5"),
    Mapping("Galactifun", "sources/repos-to-port/Galactifun-sf5"),
]


def run(cmd: list[str], cwd: Path | None = None) -> str:
    proc = subprocess.run(
        cmd,
        cwd=str(cwd) if cwd else None,
        check=True,
        capture_output=True,
        text=True,
    )
    return proc.stdout.strip()


def ensure_clone(repo: str) -> Path:
    target = UPSTREAM_DIR / repo
    if target.exists():
        run(["git", "fetch", "--all", "--prune"], cwd=target)
        run(["git", "pull", "--ff-only"], cwd=target)
        return target

    UPSTREAM_DIR.mkdir(parents=True, exist_ok=True)
    run(["gh", "repo", "clone", f"Slimefun5/{repo}", str(target)])
    return target


def latest_commit(repo: str) -> dict:
    data = run(
        [
            "gh",
            "api",
            f"repos/Slimefun5/{repo}",
            "--jq",
            "{default:.default_branch,updated:.updated_at}",
        ]
    )
    meta = json.loads(data)
    commit = run(
        [
            "gh",
            "api",
            f"repos/Slimefun5/{repo}/commits/{meta['default']}",
            "--jq",
            "{sha:.sha,date:.commit.author.date,message:.commit.message}",
        ]
    )
    out = json.loads(commit)
    out["default_branch"] = meta["default"]
    out["repo_updated_at"] = meta["updated"]
    return out


def count_changed_files(local: Path, upstream: Path) -> int:
    if not local.exists():
        return -1
    # Solo comparamos codigo/resources para evitar ruido de build
    candidates = ["src/main", "src/test"]
    total = 0
    for rel in candidates:
        local_dir = local / rel
        up_dir = upstream / rel
        if not local_dir.exists() and not up_dir.exists():
            continue
        try:
            out = run(
                [
                    "git",
                    "diff",
                    "--no-index",
                    "--name-only",
                    str(local_dir),
                    str(up_dir),
                ]
            )
            if out:
                total += len([line for line in out.splitlines() if line.strip()])
        except subprocess.CalledProcessError as err:
            # git diff --no-index retorna 1 cuando hay diferencias, esto es normal
            text = (err.stdout or "").strip()
            if text:
                total += len([line for line in text.splitlines() if line.strip()])
    return total


def main() -> None:
    rows = []
    for item in MAPPINGS:
        upstream = ensure_clone(item.repo)
        local = ROOT / item.local_path
        commit = latest_commit(item.repo)
        changed = count_changed_files(local, upstream)
        rows.append((item.repo, item.local_path, commit, changed))

    now = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M UTC")
    lines = [
        "# Sync Slimefun5 -> Drake (sin migrar runtime)",
        "",
        f"Generado: **{now}**",
        "",
        "Este reporte compara codigo `src/main` y `src/test` entre el ultimo upstream de Slimefun5 y nuestros modulos Drake.",
        "",
        "| Repo | Modulo local | Commit upstream | Fecha | Archivos distintos (aprox) |",
        "| --- | --- | --- | --- | --- |",
    ]

    for repo, local_path, commit, changed in rows:
        sha = commit["sha"][:7]
        date = commit["date"][:10]
        changed_text = "N/A (modulo no existe)" if changed < 0 else str(changed)
        lines.append(
            f"| `{repo}` | `{local_path}` | `{sha}` | `{date}` | `{changed_text}` |"
        )

    lines.extend(
        [
            "",
            "## Notas",
            "- Este flujo **no** migra al stack Slimefun5; solo absorbe cambios de codigo sobre la base Drake.",
            "- Dependencias Drake (`slimefun-core`, `dough-core`, autoupdate) se mantienen en nuestros `pom.xml`.",
            "- Para plugins con alta diferencia, aplicar integracion por lotes y compilar por modulo.",
        ]
    )

    REPORT_PATH.parent.mkdir(parents=True, exist_ok=True)
    REPORT_PATH.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Reporte generado en: {REPORT_PATH}")


if __name__ == "__main__":
    main()
