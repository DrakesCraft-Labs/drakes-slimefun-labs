import os
import re
import sys
from datetime import datetime

# Configuración
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCES_DIR = os.path.join(ROOT_DIR, "sources")
NEW_GROUP_ID = "com.github.drakescraft-labs"

# Mapeo de reparación total
REPAIR_BRIDGE = [
    (r"(io\.github\.mooy1|com\.github\.drakescraft-labs)", "InfinityLib", "infinitylib-core", "${infinitylib.version}"),
    (r"(dev\.sefiraat|com\.github\.drakescraft-labs)", "sefilib-core", "sefilib-core", "${sefilib.version}"),
    (r"(io\.github\.thebusybiscuit|com\.github\.Slimefun|com\.github\.drakescraft-labs)", "(Slimefun4|Slimefun|slimefun-core)", "slimefun-core", "${slimefun.drake.version}"),
    (r"(io\.github\.thebusybiscuit|com\.github\.drakescraft-labs)", "dough-(api|items|common|skins|data|blocks|inventories|protection|recipes|updater|core)", "dough-core", "${dough.version}"),
    (r"(io\.github\.mooy1|com\.github\.drakescraft-labs)", "InfinityExpansion", "InfinityExpansion", "1.20.6-Drake-SNAPSHOT"),
    (r"(io\.github\.thebusybiscuit|com\.github\.drakescraft-labs)", "DynaTech", "DynaTech", "1.21.11-Drake")
]

def log(msg, level="INFO"):
    colors = {"INFO": "\033[94m", "SUCCESS": "\033[92m", "WARNING": "\033[93m", "ERROR": "\033[91m", "RESET": "\033[0m"}
    print(f"{colors.get(level, '')}[{level}] {msg}{colors['RESET']}")

def unify_and_bridge():
    log("Iniciando Sincronización Maestra del Reactor...", "INFO")
    poms = []
    for root, dirs, files in os.walk(ROOT_DIR):
        if "pom.xml" in files: poms.append(os.path.join(root, "pom.xml"))
    
    for pom_path in poms:
        changed = False
        with open(pom_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # Unificar GroupID si es dev.drake o el antiguo de SF
        if "dev.drake" in content:
            content = content.replace("dev.drake", NEW_GROUP_ID)
            changed = True
            
        for gid_pat, aid_pat, target_aid, target_ver in REPAIR_BRIDGE:
            pattern = rf"(?s)<dependency>\s*<groupId>{gid_pat}</groupId>\s*<artifactId>{aid_pat}</artifactId>.*?</dependency>"
            
            def replace_dep(match):
                nonlocal changed
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
            log(f"Sincronizado: {os.path.relpath(pom_path, ROOT_DIR)}", "SUCCESS")

def audit():
    log("Iniciando Auditoría de la Gran Obra (89 Addons)...", "INFO")
    status = {"SURGICAL": [], "STABILIZED": [], "GRADLE": [], "MISSING": []}
    for category in ["batch-2-expansion", "community-addons", "repos-to-port"]:
        cat_path = os.path.join(SOURCES_DIR, category)
        if not os.path.exists(cat_path): continue
        for addon in os.listdir(cat_path):
            rel_path = f"{category}/{addon}"
            addon_path = os.path.join(cat_path, addon)
            if not os.path.isdir(addon_path): continue
            pom_path = os.path.join(addon_path, "pom.xml")
            if os.path.exists(pom_path):
                with open(pom_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    if "-drake" in content: status["SURGICAL"].append(rel_path)
                    else: status["STABILIZED"].append(rel_path)
            elif os.path.exists(os.path.join(addon_path, "build.gradle.kts")) or os.path.exists(os.path.join(addon_path, "build.gradle")):
                status["GRADLE"].append(rel_path)
            else: status["MISSING"].append(rel_path)
    
    print("\n" + "="*60)
    print(f"AUDITORIA DE DRAKESLAB - {datetime.now().strftime('%Y-%m-%d %H:%M')}")
    print("="*60)
    print(f"PROGRESO: {((len(status['SURGICAL'])/89)*100):.1f}% ({len(status['SURGICAL'])}/89)")
    print("="*60)

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "audit": audit()
    else: unify_and_bridge()
