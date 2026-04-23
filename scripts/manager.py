import os
import re
import sys

# Configuración
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
NEW_GROUP_ID = "com.github.drakescraft-labs"

# Mapeo de reparación (Cualquier combinación de viejo/nuevo -> Estado Perfecto)
REPAIR_BRIDGE = [
    (r"(io\.github\.mooy1|com\.github\.drakescraft-labs)", "InfinityLib", "infinitylib-core", "${infinitylib.version}"),
    (r"(dev\.sefiraat|com\.github\.drakescraft-labs)", "sefilib-core", "sefilib-core", "${sefilib.version}"),
    (r"(io\.github\.thebusybiscuit|com\.github\.Slimefun|com\.github\.drakescraft-labs)", "(Slimefun4|Slimefun|slimefun-core)", "slimefun-core", "${slimefun.drake.version}"),
    (r"(io\.github\.thebusybiscuit|com\.github\.drakescraft-labs)", "dough-(api|items|common|skins|data|blocks|inventories|protection|recipes|updater|core)", "dough-core", "${dough.version}")
]

def log(msg, level="INFO"):
    print(f"[{level}] {msg}")

def unify_and_bridge():
    log("Iniciando Reparación de Identidad y Versiones...")
    poms = []
    for root, dirs, files in os.walk(ROOT_DIR):
        if "pom.xml" in files: poms.append(os.path.join(root, "pom.xml"))
    
    for pom_path in poms:
        changed = False
        with open(pom_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        for gid_pat, aid_pat, target_aid, target_ver in REPAIR_BRIDGE:
            # Patrón que busca el bloque de dependencia independientemente de si ya es nuevo o viejo
            pattern = rf"(?s)<dependency>\s*<groupId>{gid_pat}</groupId>\s*<artifactId>{aid_pat}</artifactId>.*?</dependency>"
            
            def replace_dep(match):
                nonlocal changed
                # Solo reemplazar si no es ya perfecto
                new_block = f"""<dependency>
            <groupId>{NEW_GROUP_ID}</groupId>
            <artifactId>{target_aid}</artifactId>
            <version>{target_ver}</version>
            <scope>provided</scope>
        </dependency>"""
                if match.group(0).strip() != new_block.strip():
                    changed = True
                    return new_block
                return match.group(0)

            content = re.sub(pattern, replace_dep, content)
                    
        if changed:
            with open(pom_path, 'w', encoding='utf-8') as f:
                f.write(content)
            log(f"Reparado: {os.path.relpath(pom_path, ROOT_DIR)}")

if __name__ == "__main__":
    unify_and_bridge()
