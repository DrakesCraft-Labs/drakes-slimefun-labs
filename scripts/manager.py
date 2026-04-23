import os
import re
import sys

# Configuración
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCES_DIR = os.path.join(ROOT_DIR, "sources")
NEW_GROUP_ID = "com.github.drakescraft-labs"

# Mapeo de dependencias (Regex friendly)
DEP_BRIDGE = [
    (r"io\.github\.mooy1", "InfinityLib", "infinitylib-core"),
    (r"dev\.sefiraat", "sefilib-core", "sefilib-core"),
    (r"(io\.github\.thebusybiscuit|com\.github\.Slimefun)", "(Slimefun4|Slimefun)", "slimefun-core"),
    (r"io\.github\.thebusybiscuit", "dough-(api|items|common|skins|data|blocks|inventories|protection|recipes|updater)", "dough-core")
]

def log(msg, level="INFO"):
    print(f"[{level}] {msg}")

def unify_and_bridge():
    log("Iniciando Unificación de Alto Nivel...")
    poms = []
    for root, dirs, files in os.walk(ROOT_DIR):
        if "pom.xml" in files: poms.append(os.path.join(root, "pom.xml"))
    
    for pom_path in poms:
        changed = False
        with open(pom_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # 1. Unificar GroupID (dev.drake -> com.github.drakescraft-labs)
        if "dev.drake" in content:
            content = content.replace("dev.drake", NEW_GROUP_ID)
            changed = True
            
        # 2. Puente de Dependencias con Regex
        for old_gid_pat, old_aid_pat, new_aid in DEP_BRIDGE:
            # Buscar el bloque <dependency> completo
            pattern = rf"(?s)<dependency>\s*<groupId>{old_gid_pat}</groupId>\s*<artifactId>{old_aid_pat}</artifactId>.*?</dependency>"
            
            def replace_dep(match):
                nonlocal changed
                dep_block = match.group(0)
                # Reemplazar GroupID y ArtifactID
                dep_block = re.sub(rf"<groupId>{old_gid_pat}</groupId>", f"<groupId>{NEW_GROUP_ID}</groupId>", dep_block)
                dep_block = re.sub(rf"<artifactId>{old_aid_pat}</artifactId>", f"<artifactId>{new_aid}</artifactId>", dep_block)
                # Quitar versión si existe (para heredar del Parent)
                dep_block = re.sub(r"\s*<version>.*?</version>", "", dep_block)
                changed = True
                return dep_block

            content = re.sub(pattern, replace_dep, content)
                    
        if changed:
            with open(pom_path, 'w', encoding='utf-8') as f:
                f.write(content)
            log(f"Unificado: {os.path.relpath(pom_path, ROOT_DIR)}")

if __name__ == "__main__":
    unify_and_bridge()
