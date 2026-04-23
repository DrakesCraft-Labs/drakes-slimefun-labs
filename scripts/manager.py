import os
import sys
import re
from datetime import datetime
import xml.etree.ElementTree as ET

# Configuración de Rutas
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCES_DIR = os.path.join(ROOT_DIR, "sources")

def log(msg, level="INFO"):
    colors = {"INFO": "\033[94m", "SUCCESS": "\033[92m", "WARNING": "\033[93m", "ERROR": "\033[91m", "RESET": "\033[0m"}
    print(f"{colors.get(level, colors['INFO'])}[{level}] {msg}{colors['RESET']}")

def validate_xml(content):
    """Verifica si el contenido es un XML válido."""
    try:
        ET.fromstring(content)
        return True
    except ET.ParseError:
        return False

def safe_replace_pom(path, transformations, dry_run=False):
    """Aplica transformaciones de forma segura a un POM."""
    if not os.path.exists(path): return False
    
    try:
        with open(path, 'r', encoding='utf-8') as f:
            original_content = f.read()
        
        new_content = original_content
        for pattern, replacement in transformations:
            new_content = re.sub(pattern, replacement, new_content)
        
        if new_content == original_content:
            return False
        
        # VALIDACIÓN CRÍTICA
        if not validate_xml(new_content):
            log(f"¡ABORTADO! La transformación rompería el XML en: {os.path.relpath(path, ROOT_DIR)}", "ERROR")
            return False
        
        if dry_run:
            log(f"[DRY-RUN] Cambios detectados en: {os.path.relpath(path, ROOT_DIR)}", "WARNING")
            return True
        
        # Backup de seguridad
        with open(path + ".bak", 'w', encoding='utf-8') as f:
            f.write(original_content)
            
        with open(path, 'w', encoding='utf-8') as f:
            f.write(new_content)
            
        return True
    except Exception as e:
        log(f"Error procesando {path}: {str(e)}", "ERROR")
        return False

def audit():
    log(f"Iniciando Auditoría Blindada (89 Addons)...", "INFO")
    status = {"SURGICAL": [], "STABILIZED": [], "GRADLE": [], "LEGACY": []}
    
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            path = os.path.join(root, "pom.xml")
            rel_path = os.path.relpath(root, SOURCES_DIR)
            
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            if "-drake" in content: status["SURGICAL"].append(rel_path)
            else: status["STABILIZED"].append(rel_path)
        
        if "build.gradle" in files or "build.gradle.kts" in files:
            rel_path = os.path.relpath(root, SOURCES_DIR)
            if rel_path != ".": status["GRADLE"].append(rel_path)

    print("\n" + "="*60)
    print(f"ESTADO DEL REACTOR - {datetime.now().strftime('%Y-%m-%d %H:%M')}")
    print("="*60)
    perc = (len(status['SURGICAL'])/89)*100
    print(f"PROGRESO QUIRÚRGICO: {perc:.1f}% ({len(status['SURGICAL'])}/89)")
    print("="*60)
    return status

def repair(dry_run=False):
    log("Iniciando Reparación Blindada del Reactor...", "INFO")
    
    # Transformaciones Maestras
    transformations = [
        # 1. Asegurar GroupID del Parent
        (r"(<parent>[\s\S]*?<groupId>).*?(</groupId>)", r"\g<1>com.github.drakescraft-labs\g<2>"),
        # 2. Asegurar ArtifactID del Parent
        (r"(<parent>[\s\S]*?<artifactId>).*?(</artifactId>)", r"\g<1>drakes-slimefun-labs\g<2>"),
        # 3. Forzar Versión 11-SNAPSHOT
        (r"(<parent>[\s\S]*?<version>).*?(</version>)", r"\g<1>11-SNAPSHOT\g<2>"),
        # 4. Reparar corrupciones previas (I-SNAPSHOT)
        (r"I-SNAPSHOT</version>", r"<version>11-SNAPSHOT</version>")
    ]
    
    count = 0
    for root, _, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            if safe_replace_pom(os.path.join(root, "pom.xml"), transformations, dry_run):
                count += 1
    
    log(f"Reparación completada. Módulos afectados: {count}", "SUCCESS")

def sync_docs(status):
    log("Sincronizando métricas en toda la flota de documentos...", "INFO")
    
    targets = [
        os.path.join(ROOT_DIR, "README.md"),
        os.path.join(ROOT_DIR, "README_EN.md"),
        os.path.join(ROOT_DIR, "docs", "es", "migration-checklist.md"),
        os.path.join(ROOT_DIR, "docs", "en", "migration-checklist.md")
    ]
    
    total_maven = len(status['STABILIZED']) + len(status['SURGICAL'])
    total_gradle = len(status['GRADLE'])
    perc = (len(status['SURGICAL'])/89)*100
    progress_str = f"{perc:.1f}% ({len(status['SURGICAL'])}/89)"

    for path in targets:
        if not os.path.exists(path): continue
        
        try:
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # Actualizaciones de Regex
            content = re.sub(r"\|\s*\*\*REACTOR MAVEN\*\*\s*\|\s*`\d+`", f"| **REACTOR MAVEN** | `{total_maven}`", content)
            content = re.sub(r"\|\s*\*\*REACTOR GRADLE\*\*\s*\|\s*`\d+`", f"| **REACTOR GRADLE** | `{total_gradle}`", content)
            content = re.sub(r"\|\s*\*\*Progreso Quirúrgico\*\*\s*\|\s*\*\*[\d\.]+% \(\d+/89\)\*\*", f"| **Progreso Quirúrgico** | **{progress_str}**", content)
            
            # Sincronizar Resumen de Flota
            content = re.sub(r"- \*\*Reactor Maven\*\*: \d+ Módulos", f"- **Reactor Maven**: {total_maven} Módulos", content)
            content = re.sub(r"- \*\*Reactor Gradle\*\*: \d+ Módulos", f"- **Reactor Gradle**: {total_gradle} Módulos", content)
            content = re.sub(r"- \*\*Identidad com.github.drakescraft-labs\*\*: .*", f"- **Identidad com.github.drakescraft-labs**: Implementada en el core y {len(status['SURGICAL'])} addons ({perc:.1f}%).", content)

            with open(path, 'w', encoding='utf-8') as f:
                f.write(content)
            log(f"Sincronizado: {os.path.relpath(path, ROOT_DIR)}", "SUCCESS")
        except Exception as e:
            log(f"Error sincronizando {path}: {str(e)}", "ERROR")

def inject_dough(dry_run=False):
    log("Iniciando Inyección Masiva de Dough...", "INFO")
    
    count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            pom_path = os.path.join(root, "pom.xml")
            
            # 1. ¿Usa Dough en el código?
            needs_dough = False
            for r, d, f in os.walk(root):
                for file in f:
                    if file.endswith(".java"):
                        with open(os.path.join(r, file), 'r', encoding='utf-8', errors='ignore') as jf:
                            if "dev.drake.dough" in jf.read():
                                needs_dough = True
                                break
                if needs_dough: break
            
            if needs_dough:
                with open(pom_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                if "dough-core" not in content:
                    log(f"Inyectando dough-core en: {os.path.relpath(pom_path, ROOT_DIR)}", "WARNING")
                    
                    # Transformación: Inyectar antes de </dependencies>
                    dependency_block = """
        <dependency>
            <groupId>com.github.drakescraft-labs</groupId>
            <artifactId>dough-core</artifactId>
            <version>${dough.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>"""
                    
                    new_content = content.replace("</dependencies>", dependency_block)
                    
                    if not dry_run:
                        if validate_xml(new_content):
                            with open(pom_path + ".bak", 'w', encoding='utf-8') as f:
                                f.write(content)
                            with open(pom_path, 'w', encoding='utf-8') as f:
                                f.write(new_content)
                            count += 1
                        else:
                            log(f"Error de validación inyectando en {pom_path}", "ERROR")
                    else:
                        count += 1

    log(f"Inyección de Dough completada. Módulos alimentados: {count}", "SUCCESS")

def inject_lombok(dry_run=False):
    log("Iniciando Inyección Masiva de Lombok...", "INFO")
    
    count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            pom_path = os.path.join(root, "pom.xml")
            
            # 1. ¿Usa Lombok en el código?
            needs_lombok = False
            for r, d, f in os.walk(root):
                for file in f:
                    if file.endswith(".java"):
                        with open(os.path.join(r, file), 'r', encoding='utf-8', errors='ignore') as jf:
                            code = jf.read()
                            if any(x in code for x in ["@Getter", "@Setter", "@UtilityClass", "@NoArgsConstructor", "@AllArgsConstructor", "@Data"]):
                                needs_lombok = True
                                break
                if needs_lombok: break
            
            if needs_lombok:
                with open(pom_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                if "lombok" not in content.lower():
                    log(f"Inyectando Lombok en: {os.path.relpath(pom_path, ROOT_DIR)}", "WARNING")
                    
                    dependency_block = """
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.34</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>"""
                    
                    new_content = content.replace("</dependencies>", dependency_block)
                    
                    if not dry_run:
                        if validate_xml(new_content):
                            with open(pom_path + ".bak", 'w', encoding='utf-8') as f:
                                f.write(content)
                            with open(pom_path, 'w', encoding='utf-8') as f:
                                f.write(new_content)
                            count += 1
                        else:
                            log(f"Error de validación inyectando en {pom_path}", "ERROR")
                    else:
                        count += 1

    log(f"Inyección de Lombok completada. Módulos alimentados: {count}", "SUCCESS")

def migrate_to_paper(dry_run=False):
    log("Iniciando Migración Masiva a Paper-API...", "INFO")
    
    # Patrón para detectar spigot-api y capturar su bloque entero
    spigot_pattern = r"<(dependency)>\s*<groupId>org\.spigotmc</groupId>\s*<artifactId>spigot-api</artifactId>[\s\S]*?</\1>"
    
    paper_replacement = """<dependency>
            <groupId>io.papermc.paper</groupId>
            <artifactId>paper-api</artifactId>
            <version>${paper.version}</version>
            <scope>provided</scope>
        </dependency>"""
    
    count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            pom_path = os.path.join(root, "pom.xml")
            
            with open(pom_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            if "spigot-api" in content:
                log(f"Migrando a Paper-API: {os.path.relpath(pom_path, ROOT_DIR)}", "WARNING")
                new_content = re.sub(spigot_pattern, paper_replacement, content)
                
                if new_content != content:
                    if not dry_run:
                        if validate_xml(new_content):
                            with open(pom_path + ".bak", 'w', encoding='utf-8') as f:
                                f.write(content)
                            with open(pom_path, 'w', encoding='utf-8') as f:
                                f.write(new_content)
                            count += 1
                        else:
                            log(f"Error de validación migrando en {pom_path}", "ERROR")
                    else:
                        count += 1

    log(f"Migración completada. Módulos modernizados: {count}", "SUCCESS")

if __name__ == "__main__":
    action = sys.argv[1] if len(sys.argv) > 1 else "repair"
    is_dry = "--dry-run" in sys.argv
    
    if action == "audit":
        res = audit()
        if "--sync" in sys.argv: sync_docs(res)
    elif action == "repair":
        repair(dry_run=is_dry)
    elif action == "inject-dough":
        inject_dough(dry_run=is_dry)
    elif action == "inject-lombok":
        inject_lombok(dry_run=is_dry)
    elif action == "migrate-to-paper":
        migrate_to_paper(dry_run=is_dry)
    else:
        log(f"Acción desconocida: {action}", "ERROR")
