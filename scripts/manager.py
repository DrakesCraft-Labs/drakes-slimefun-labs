import os
import xml.etree.ElementTree as ET
import sys

# Configuración
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCES_DIR = os.path.join(ROOT_DIR, "sources")
NAMESPACE = "{http://maven.apache.org/POM/4.0.0}"
ET.register_namespace('', "http://maven.apache.org/POM/4.0.0")

def log(msg, level="INFO"):
    print(f"[{level}] {msg}")

def get_all_poms():
    poms = []
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            poms.append(os.path.join(root, "pom.xml"))
    return poms

def sync_parent():
    """Sincroniza el groupId del parent en todos los submódulos."""
    root_pom = os.path.join(ROOT_DIR, "pom.xml")
    tree = ET.parse(root_pom)
    root = tree.getroot()
    
    new_group_id = root.find(f"{NAMESPACE}groupId").text
    log(f"Nuevo GroupID raíz detectado: {new_group_id}")
    
    poms = get_all_poms()
    for pom_path in poms:
        changed = False
        with open(pom_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        in_parent = False
        for line in lines:
            if "<parent>" in line: in_parent = True
            if "</parent>" in line: in_parent = False
            
            if in_parent and "<groupId>" in line:
                current_group = line.split(">")[1].split("<")[0]
                if current_group != new_group_id:
                    indent = line.split("<")[0]
                    line = f"{indent}<groupId>{new_group_id}</groupId>\n"
                    changed = True
            new_lines.append(line)
            
        if changed:
            with open(pom_path, 'w', encoding='utf-8') as f:
                f.writelines(new_lines)
            log(f"Sincronizado: {os.path.relpath(pom_path, ROOT_DIR)}")

def check_health():
    log("Iniciando escaneo de salud del ecosistema...")
    poms = get_all_poms()
    poms.append(os.path.join(ROOT_DIR, "pom.xml"))
    errors = 0
    for pom in poms:
        try:
            ET.parse(pom)
        except Exception as e:
            log(f"Error en {pom}: {e}", "ERROR")
            errors += 1
    if errors == 0: log("¡Ecosistema saludable!", "SUCCESS")

if __name__ == "__main__":
    cmd = sys.argv[1].lower() if len(sys.argv) > 1 else ""
    if cmd == "sync":
        sync_parent()
    elif cmd == "health":
        check_health()
    else:
        print("Uso: python manager.py [sync|health]")
