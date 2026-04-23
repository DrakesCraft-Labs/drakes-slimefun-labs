import os
import xml.etree.ElementTree as ET
import sys

# Configuración
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCES_DIR = os.path.join(ROOT_DIR, "sources")
NAMESPACE = "{http://maven.apache.org/POM/4.0.0}"
ET.register_namespace('', "http://maven.apache.org/POM/4.0.0")

# La nueva identidad unificada
NEW_GROUP_ID = "com.github.drakescraft-labs"

def log(msg, level="INFO"):
    print(f"[{level}] {msg}")

def get_all_poms():
    poms = [os.path.join(ROOT_DIR, "pom.xml")]
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            poms.append(os.path.join(root, "pom.xml"))
    return poms

def unify_identity():
    """Asegura que todo el ecosistema use el mismo GroupID base."""
    log(f"Iniciando Unificación de Identidad a {NEW_GROUP_ID}...")
    
    poms = get_all_poms()
    for pom_path in poms:
        changed = False
        with open(pom_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        in_parent = False
        for line in lines:
            # 1. Actualizar Parent GroupID
            if "<parent>" in line: in_parent = True
            if "</parent>" in line: in_parent = False
            
            if in_parent and "<groupId>" in line:
                current = line.split(">")[1].split("<")[0]
                if current != NEW_GROUP_ID:
                    line = line.replace(current, NEW_GROUP_ID)
                    changed = True
            
            # 2. Actualizar GroupID del propio proyecto (si es de dev.drake)
            if not in_parent and "<groupId>" in line:
                current = line.split(">")[1].split("<")[0]
                if "dev.drake" in current:
                    line = line.replace(current, NEW_GROUP_ID)
                    changed = True
            
            # 3. Actualizar dependencias internas (para que se encuentren entre sí)
            if "<groupId>dev.drake" in line:
                line = line.replace("dev.drake", NEW_GROUP_ID)
                changed = True
                
            new_lines.append(line)
            
        if changed:
            with open(pom_path, 'w', encoding='utf-8') as f:
                f.writelines(new_lines)
            log(f"Unificado: {os.path.relpath(pom_path, ROOT_DIR)}")

if __name__ == "__main__":
    unify_identity()
