#!/usr/bin/env python3
"""
Descarga dependencias opcionales del servidor de smoke (p. ej. ProtocolLib para SoundMuffler).
Uso: python scripts/smoke/fetch_smoke_optional_deps.py <carpeta_plugins_del_servidor>
"""

from __future__ import annotations

import sys
import urllib.request
from pathlib import Path

PROTOCOL_LIB_URLS = (
    "https://github.com/dmulloy2/ProtocolLib/releases/download/5.3.0/ProtocolLib.jar",
    "https://repo.dmulloy2.net/repository/public/com/comphenix/protocol/ProtocolLib/5.3.0/ProtocolLib-5.3.0.jar",
)


def main() -> int:
    if len(sys.argv) != 2:
        print("Uso: fetch_smoke_optional_deps.py <plugins_dir>", file=sys.stderr)
        return 2
    plugins = Path(sys.argv[1]).resolve()
    plugins.mkdir(parents=True, exist_ok=True)
    dest = plugins / "ProtocolLib.jar"
    if dest.is_file() and dest.stat().st_size > 10_000:
        print(f"Ya existe {dest}, omitiendo descarga.")
        return 0
    last_err: BaseException | None = None
    for url in PROTOCOL_LIB_URLS:
        try:
            print(f"Descargando ProtocolLib ({url}) -> {dest}", flush=True)
            urllib.request.urlretrieve(url, dest)
            if dest.is_file() and dest.stat().st_size > 10_000:
                with dest.open("rb") as fh:
                    if fh.read(2) == b"PK":
                        print("Listo.")
                        return 0
        except Exception as e:
            last_err = e
            print(f"WARN: fallo URL: {e}", flush=True)
    print(f"ERROR: no se pudo descargar ProtocolLib: {last_err}", file=sys.stderr)
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
