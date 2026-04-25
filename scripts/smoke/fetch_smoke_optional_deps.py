#!/usr/bin/env python3
"""
Descarga dependencias opcionales del servidor de smoke (p. ej. ProtocolLib para SoundMuffler).
Uso: python scripts/smoke/fetch_smoke_optional_deps.py <carpeta_plugins_del_servidor>
"""

from __future__ import annotations

import sys
import urllib.request
from pathlib import Path

PROTOCOL_LIB_URL = (
    "https://github.com/dmulloy2/ProtocolLib/releases/download/5.3.0/ProtocolLib.jar"
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
    print(f"Descargando ProtocolLib -> {dest}", flush=True)
    urllib.request.urlretrieve(PROTOCOL_LIB_URL, dest)
    print("Listo.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
