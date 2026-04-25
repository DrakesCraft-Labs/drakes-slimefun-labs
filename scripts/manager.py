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

# Mapeo Maestro de Namespaces
MASTER_MAPPING = {
    "slimefun4": "com.github.drakescraft_labs.slimefun4",
    "dough": "dev.drake.dough",
    "infinitylib": "dev.drake.infinitylib",
    "sefilib": "dev.drake.sefilib"
}

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
        
        elif "build.gradle" in files or "build.gradle.kts" in files:
            fname = "build.gradle" if "build.gradle" in files else "build.gradle.kts"
            path = os.path.join(root, fname)
            rel_path = os.path.relpath(root, SOURCES_DIR)
            with open(path, 'r', encoding='utf-8') as f:
                content = f.read()
            if "drake" in content.lower(): status["SURGICAL"].append(rel_path)
            else: status["GRADLE"].append(rel_path)

    print("\n" + "="*60)
    print(f"ESTADO DEL REACTOR - {datetime.now().strftime('%Y-%m-%d %H:%M')}")
    print("="*60)
    total = len(status['SURGICAL']) + len(status['STABILIZED']) + len(status['GRADLE']) + len(status['LEGACY'])
    perc = (len(status['SURGICAL'])/total)*100 if total > 0 else 0
    print(f"PROGRESO QUIRÚRGICO: {perc:.1f}% ({len(status['SURGICAL'])}/{total})")
    print("="*60)
    if status['STABILIZED']:
        print(f"\n[!] Módulos Maven Pendientes de Cirugía ({len(status['STABILIZED'])}):")
        for m in sorted(status['STABILIZED']):
            print(f"  - {m}")
    if status['GRADLE']:
        print(f"\n[GRADLE] Módulos Gradle en el Reactor ({len(status['GRADLE'])}):")
        for m in sorted(status['GRADLE']):
            print(f"  - {m}")
    print("="*60)
    return status

def repair(dry_run=False):
    log("Iniciando Reparación Blindada del Reactor...", "INFO")
    
    # Transformaciones Maestras
    transformations = [
        # 1. Asegurar GroupID del Parent
        (r"(<parent>[\s\S]*?<groupId>).*?(</groupId>)", r"\g<1>com.github.drakescraft_labs\g<2>"),
        # 2. Asegurar ArtifactID del Parent
        (r"(<parent>[\s\S]*?<artifactId>).*?(</artifactId>)", r"\g<1>drakes-slimefun-labs\g<2>"),
        # 3. Forzar Versión 11-SNAPSHOT del Parent
        (r"(<parent>[\s\S]*?<version>).*?(</version>)", r"\g<1>11-SNAPSHOT\g<2>"),
        # 4. Reparar GroupID en Dependencias (MASIVO)
        (r"<groupId>((io|com)\.github\.(thebusybiscuit|mooy1|seggan|sefiraat|slimefunguguproject|addoncommunity|baked-?libs(\.[\w-]+)*)|me\.(mr_cookie|pika|vaan))</groupId>", r"<groupId>com.github.drakescraft_labs</groupId>"),
        (r"(<groupId>)com\.github\.drakescraft-labs(</groupId>)", r"\g<1>com.github.drakescraft_labs\g<2>"),
        # 4b. RESTAURAR LIBRERÍAS EXTERNAS (Si se rebrandearon por error)
        (r"<groupId>com\.github\.drakescraft_labs</groupId>(\s*)<artifactId>ErrorReporter-Java</artifactId>", r"<groupId>io.github.seggan</groupId>\1<artifactId>ErrorReporter-Java</artifactId>"),
        (r"<groupId>com\.github\.drakescraft_labs</groupId>(\s*)<artifactId>GuizhanLib-API</artifactId>", r"<groupId>net.guizhanss</groupId>\1<artifactId>GuizhanLib-API</artifactId>"),
        # 5. Mapeo de Librerías Internas (artifactId)
        (r"<artifactId>infinitylib-core</artifactId>", r"<artifactId>infinitylib-drake</artifactId>"),
        (r"<artifactId>sefilib-core</artifactId>", r"<artifactId>sefilib-drake</artifactId>"),
        (r"<artifactId>Networks</artifactId>", r"<artifactId>Networks-drake</artifactId>"),
        (r"<artifactId>dough-(api|items|common|protection|skins|inventories|recipes|chat|data|versions)</artifactId>", r"<artifactId>dough-core</artifactId>"),
        # 5b. Mapeo de Addons Cruzados (artifactId)
        (r"<artifactId>ExoticGarden</artifactId>", r"<artifactId>ExoticGarden-drake</artifactId>"),
        (r"<artifactId>InfinityExpansion</artifactId>", r"<artifactId>InfinityExpansion-drake</artifactId>"),
        # 6. Forzar el uso de Propiedades en Librerías Internas (SOLO EN DEPENDENCIAS)
        (r"(<dependency>[\s\S]*?<artifactId>infinitylib-drake</artifactId>)\s*<version>.*?</version>", r"\1\n            <version>${infinitylib.version}</version>"),
        (r"(<dependency>[\s\S]*?<artifactId>sefilib-drake</artifactId>)\s*<version>.*?</version>", r"\1\n            <version>${sefilib.version}</version>"),
        (r"(<dependency>[\s\S]*?<artifactId>slimefun-core</artifactId>)\s*<version>.*?</version>", r"\1\n            <version>${slimefun.drake.version}</version>"),
        (r"(<dependency>[\s\S]*?<artifactId>dough-core</artifactId>)\s*<version>.*?</version>", r"\1\n            <version>${dough.version}</version>"),
        # 7. Forzar Herencia en Paper y ACF (Eliminar versiones fijas)
        (r"(<artifactId>(paper-api|acf-paper)</artifactId>)\s*<version>.*?</version>", r"\1"),
        # 8. Sustitución de Commons-Lang v2 por Versión Blindada (EXCLUYENDO EL PROPIO PARCHE)
        # Esta regla se aplicará solo si no estamos en el directorio del parche (se maneja en el loop)
        (r"<groupId>commons-lang</groupId>[\s\S]*?<artifactId>commons-lang</artifactId>([\s\S]*?<version>.*?</version>)?", "<groupId>com.github.drakescraft_labs</groupId>\n            <artifactId>commons-lang-drake-patched</artifactId>\n            <version>2.6.1-DRAKE-PATCHED</version>"),
        # 8. Reparar corrupciones previas (I-SNAPSHOT)
        (r"I-SNAPSHOT</version>", r"<version>11-SNAPSHOT</version>"),
        # 9. Actualizar maven-shade-plugin a 3.6.0 para soporte de Java 21 (Major version 65)
        (r"(<artifactId>maven-shade-plugin</artifactId>\s*<version>).*?(</version>)", r"\g<1>3.6.0\g<2>"),
        # 10. GraalVM: js (enterprise) → js-community (evita truffle-enterprise en Maven público / CI)
        (r"(<groupId>org\.graalvm\.js</groupId>\s*<artifactId>)js(</artifactId>)", r"\g<1>js-community\g<2>"),
    ]
    
    count = 0
    for root, _, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            pom_path = os.path.join(root, "pom.xml")
            
            # EXCEPCIÓN CRÍTICA: No aplicar la regla de commons-lang al propio módulo del parche
            active_transformations = transformations
            if "commons-lang-drake-patched" in root:
                # Filtrar la regla de commons-lang (es la penúltima)
                active_transformations = [t for t in transformations if "commons-lang-drake-patched" not in t[1]]
            
            if safe_replace_pom(pom_path, active_transformations, dry_run):
                count += 1
    
    log(f"Reparación completada. Módulos afectados: {count}", "SUCCESS")

def sync_docs(status):
    log("Sincronizando métricas en toda la flota de documentos...", "INFO")
    
    targets = [
        os.path.join(ROOT_DIR, "README.md"),
        os.path.join(ROOT_DIR, "docs", "es", "migration-checklist.md"),
        os.path.join(ROOT_DIR, "docs", "en", "migration-checklist.md"),
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
            content = re.sub(r"- \*\*Identidad com.github.drakescraft_labs\*\*: .*", f"- **Identidad com.github.drakescraft_labs**: Implementada en el core y {len(status['SURGICAL'])} addons ({perc:.1f}%).", content)

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

def inject_jsr305(dry_run=False):
    log("Iniciando Inyección Masiva de JSR-305 (Anotaciones)...", "INFO")
    
    count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            pom_path = os.path.join(root, "pom.xml")
            
            # 1. ¿Usa anotaciones en el código?
            needs_jsr = False
            for r, d, f in os.walk(root):
                for file in f:
                    if file.endswith(".java"):
                        try:
                            with open(os.path.join(r, file), 'r', encoding='utf-8', errors='ignore') as jf:
                                code = jf.read()
                                if any(x in code for x in ["@Nonnull", "@Nullable", "javax.annotation"]):
                                    needs_jsr = True
                                    break
                        except: pass
                if needs_jsr: break
            
            if needs_jsr:
                with open(pom_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                if "jsr305" not in content.lower():
                    log(f"Inyectando JSR-305 en: {os.path.relpath(pom_path, ROOT_DIR)}", "WARNING")
                    
                    dependency_block = """
        <dependency>
            <groupId>com.google.code.findbugs</groupId>
            <artifactId>jsr305</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>"""
                    
                    new_content = content.replace("</dependencies>", dependency_block)
                    
                    if not dry_run:
                        if validate_xml(new_content):
                            with open(pom_path, 'w', encoding='utf-8') as f:
                                f.write(new_content)
                            count += 1
                    else:
                        count += 1

    log(f"Inyección de JSR-305 completada. Módulos alimentados: {count}", "SUCCESS")

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

    log(f"Rebranding completado. Módulos actualizados: {count}", "SUCCESS")

def rebrand_shades(dry_run=False):
    log("Iniciando Rebranding de Sombras...", "INFO")
    
    # Mapeo de namespaces antiguos a nuevos
    remap = {
        "io.github.thebusybiscuit.slimefun4": MASTER_MAPPING["slimefun4"],
        "io.github.thebusybiscuit": "com.github.drakescraft_labs",
        "io.github.seggan": "com.github.drakescraft_labs",
        "io.github.mooy1.infinitylib": MASTER_MAPPING["infinitylib"],
        "io.github.mooy1": "com.github.drakescraft_labs",
        "io.github.baked-?libs.dough": MASTER_MAPPING["dough"],
        "io.github.baked-?libs": "dev.drake",
        "dev.sefiraat.sefilib": MASTER_MAPPING["sefilib"],
        "com.github.drakescraft-labs": "com.github.drakescraft_labs"
    }
    
    count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            pom_path = os.path.join(root, "pom.xml")
            
            with open(pom_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            new_content = content
            for old, new in remap.items():
                # Rebrandear tanto en <pattern> como en <shadedPattern>
                for tag in ["pattern", "shadedPattern"]:
                    pattern = rf"<{tag}>{old}(\.?)(.*?)</{tag}>"
                    replacement = rf"<{tag}>{new}\1\2</{tag}>"
                    new_content = re.sub(pattern, replacement, new_content)
            
            if new_content != content:
                log(f"Rebrandeando sombras en: {os.path.relpath(pom_path, ROOT_DIR)}", "WARNING")
                if not dry_run:
                    if validate_xml(new_content):
                        with open(pom_path + ".bak", 'w', encoding='utf-8') as f:
                            f.write(content)
                        with open(pom_path, 'w', encoding='utf-8') as f:
                            f.write(new_content)
                        count += 1
                    else:
                        log(f"Error de validación rebrandeando en {pom_path}", "ERROR")
                else:
                    count += 1

    log(f"Rebranding de POMs completado. Módulos actualizados: {count}", "SUCCESS")

def rebrand_imports(dry_run=False):
    log("Iniciando Rebranding de Imports en Código Fuente (.java)...", "INFO")
    
    # Mapeo de namespaces de librerías y autores originales
    remap = {
        r"io\.github\.thebusybiscuit\.slimefun4": MASTER_MAPPING["slimefun4"],
        r"io\.github\.thebusybiscuit": "com.github.drakescraft_labs",
        r"io\.github\.seggan": "com.github.drakescraft_labs",
        r"io\.github\.mooy1\.infinitylib": MASTER_MAPPING["infinitylib"],
        r"io\.github\.mooy1": "com.github.drakescraft_labs",
        r"io\.github\.slimefunguguproject": "com.github.drakescraft_labs",
        r"io\.github\.addoncommunity": "com.github.drakescraft_labs",
        r"io\.github\.baked-?libs\.dough": MASTER_MAPPING["dough"],
        r"io\.github\.baked-?libs": "dev.drake",
        r"dev\.sefiraat\.sefilib": MASTER_MAPPING["sefilib"],
        r"me\.mrCookieSlime\.Slimefun": f"{MASTER_MAPPING['slimefun4']}.legacy",
        r"me\.mr_cookie": "com.github.drakescraft_labs",
        r"me\.pika": "com.github.drakescraft_labs",
        r"me\.vaan": "com.github.drakescraft_labs",
        # Parche para corregir errores de rebranding previos
        r"com\.github\.drakescraft_labs\.dough": MASTER_MAPPING["dough"],
        r"com\.github\.drakescraft_labs\.infinitylib": MASTER_MAPPING["infinitylib"],
        r"com\.github\.drakescraft_labs\.sefilib": MASTER_MAPPING["sefilib"],
        r"com\.github\.drakescraft-labs": "com.github.drakescraft_labs",
        # Restaurar librerías externas que NO deben rebrandearse en imports
        r"com\.github\.drakescraft_labs\.guizhanlib": "net.guizhanss.guizhanlib",
        r"com\.github\.drakescraft_labs\.errorreporter": "io.github.seggan.errorreporter"
    }
    
    # Reglas de limpieza de telemetría obsoleta (bStats/InfinityLib Metrics)
    telemetry_rules = [
        (r"import\s+dev\.drake\.infinitylib\.metrics\..*?;", ""),
        (r"import\s+io\.github\.mooy1\.infinitylib\.metrics\..*?;", ""),
        (r".*?new\s+Metrics\(this,\s*\d+\);", ""),
        (r".*?metrics\.addCustomChart\(.*?\);", "")
    ]
    
    count = 0
    file_count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        for f in files:
            if f.endswith(".java") or f.endswith(".kt"):
                file_path = os.path.join(root, f)
                
                try:
                    with open(file_path, 'r', encoding='utf-8', errors='ignore') as jf:
                        content = jf.read()
                    
                    new_content = content
                    
                    # Aplicar reglas de telemetría
                    for pattern, replacement in telemetry_rules:
                        new_content = re.sub(pattern, replacement, new_content)
                    
                    # Aplicar rebranding de imports
                    for old, new in remap.items():
                        new_content = re.sub(old, new, new_content)
                    
                    # Alineación de sombras para Dough en expansiones y addons
                    # Slimefun-core y dough-core deben usar dev.drake.dough directamente
                    if "dough-core" not in root and "slimefun-core" not in root:
                        new_content = re.sub(r"dev\.drake\.dough", r"com.github.drakescraft_labs.slimefun4.libraries.dough", new_content)
                    
                    if new_content != content:
                        if not dry_run:
                            with open(file_path, 'w', encoding='utf-8') as jf:
                                jf.write(new_content)
                        count += 1
                    file_count += 1
                except Exception as e:
                    log(f"Error procesando {file_path}: {str(e)}", "ERROR")

    log(f"Rebranding de código completado. Archivos modificados: {count} de {file_count}", "SUCCESS")

def rebrand_folders():
    log("Iniciando Reestructuración Física de Carpetas...", "INFO")
    
    # Definir los mapeos de carpetas (Base Antigua -> Base Nueva)
    mappings = [
        (os.path.join("io", "github", "thebusybiscuit"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("io", "github", "seggan"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("io", "github", "mooy1"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("io", "github", "slimefunguguproject"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("io", "github", "addoncommunity"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("io", "github", "bakedlibs"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("me", "mrCookieSlime"), os.path.join("com", "github", "drakescraft_labs", "slimefun4", "legacy")),
        (os.path.join("me", "pika"), os.path.join("com", "github", "drakescraft_labs")),
        (os.path.join("net", "guizhanss"), os.path.join("com", "github", "drakescraft_labs")),
        # Parche para carpetas con guion
        (os.path.join("com", "github", "drakescraft-labs"), os.path.join("com", "github", "drakescraft_labs"))
    ]
    
    count = 0
    import shutil
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "src" in root and "main" in root and ("java" in root or "kotlin" in root):
            java_root = root
            
            for old_base, new_base in mappings:
                old_path = os.path.join(java_root, old_base)
                new_path = os.path.join(java_root, new_base)
                
                if os.path.exists(old_path):
                    log(f"Reorganizando {old_base} en: {os.path.relpath(java_root, SOURCES_DIR)}", "WARNING")
                    
                    # Asegurar que el destino exista
                    os.makedirs(os.path.dirname(new_path), exist_ok=True)
                    
                    # Mover el contenido de forma recursiva y segura
                    if os.path.exists(new_path):
                        for item in os.listdir(old_path):
                            s = os.path.join(old_path, item)
                            d = os.path.join(new_path, item)
                            if os.path.isdir(s):
                                if os.path.exists(d):
                                    # Combinar directorios
                                    for subitem in os.listdir(s):
                                        shutil.move(os.path.join(s, subitem), os.path.join(d, subitem))
                                    os.rmdir(s)
                                else:
                                    shutil.move(s, d)
                            else:
                                if os.path.exists(d): os.remove(d)
                                shutil.move(s, d)
                        try:
                            os.rmdir(old_path)
                            # Limpiar carpetas padres vacías (io/github o me)
                            parent = os.path.dirname(old_path)
                            if not os.listdir(parent): os.rmdir(parent)
                            grandparent = os.path.dirname(parent)
                            if not os.listdir(grandparent): os.rmdir(grandparent)
                        except: pass
                    else:
                        shutil.move(old_path, new_path)
                        try:
                            # Limpiar carpetas padres vacías
                            parent = os.path.dirname(old_path)
                            if not os.listdir(parent): os.rmdir(parent)
                            grandparent = os.path.dirname(parent)
                            if not os.listdir(grandparent): os.rmdir(grandparent)
                        except: pass
                    count += 1
                
    log(f"Reestructuración completa. Operaciones realizadas: {count}", "SUCCESS")

def security_sentinel(fix=False):
    """Escanea y repara vulnerabilidades de dependencias."""
    log("Iniciando Módulo Sentinel (Security Audit)...", "INFO")
    
    # Libreras bajo vigilancia y sus versiones mnimas seguras
    VULNERABLE_ARTIFACTS = {
        "commons-lang3": "3.20.0",
        "guava": "33.6.0-jre",
        "spring-context": "6.1.21"
    }
    
    CRITICAL_DEPRECATED = ["commons-lang"] # Versin 2.x EOL
    
    found_count = 0
    fixed_count = 0
    
    for root, dirs, files in os.walk(SOURCES_DIR):
        if "pom.xml" in files:
            path = os.path.join(root, "pom.xml")
            rel_path = os.path.relpath(path, SOURCES_DIR)
            
            with open(path, 'r', encoding='utf-8', errors='ignore') as f:
                content = f.read()
            
            needs_fix = False
            for artifact in VULNERABLE_ARTIFACTS:
                # Busca <artifactId>artifact</artifactId> seguido de un <version>
                pattern = fr"(<artifactId>{artifact}</artifactId>)\s*<version>.*?</version>"
                if re.search(pattern, content):
                    log(f"VULNERABILIDAD: {artifact} detectado con versin hardcodeada en {rel_path}", "WARNING")
                    found_count += 1
                    needs_fix = True
            
            for artifact in CRITICAL_DEPRECATED:
                if f"<artifactId>{artifact}</artifactId>" in content:
                    log(f"CRTICO: Librera EOL detectada: {artifact} en {rel_path}. Migrar a v3 pronto.", "ERROR")
                    found_count += 1

            if fix and needs_fix:
                # Reparacin: eliminar la etiqueta <version> para forzar herencia del root
                new_content = content
                for artifact in VULNERABLE_ARTIFACTS:
                    pattern = fr"(<artifactId>{artifact}</artifactId>)\s*<version>.*?</version>"
                    new_content = re.sub(pattern, r"\1", new_content)
                
                if new_content != content and validate_xml(new_content):
                    with open(path, 'w', encoding='utf-8') as f:
                        f.write(new_content)
                    fixed_count += 1
    
    if found_count == 0:
        log("Sentinel: No se detectaron vulnerabilidades conocidas en la flota.", "SUCCESS")
    else:
        log(f"Sentinel: Escaneo completado. Encontrados: {found_count}, Reparados: {fixed_count}", "SUCCESS")

def clean_backups():
    log("Iniciando Limpieza de Archivos .bak...", "INFO")
    count = 0
    for root, dirs, files in os.walk(SOURCES_DIR):
        for f in files:
            if f.endswith(".bak"):
                os.remove(os.path.join(root, f))
                count += 1
    log(f"Limpieza completada. Archivos eliminados: {count}", "SUCCESS")

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
    elif action == "inject-jsr305":
        inject_jsr305(dry_run=is_dry)
    elif action == "migrate-to-paper":
        migrate_to_paper(dry_run=is_dry)
    elif action == "rebrand-shades":
        rebrand_shades(dry_run=is_dry)
    elif action == "rebrand-imports":
        rebrand_imports(dry_run=is_dry)
    elif action == "rebrand-folders":
        rebrand_folders()
    elif action == "security":
        security_sentinel(fix=False)
    elif action == "security-fix":
        security_sentinel(fix=True)
    elif action == "clean-backups":
        clean_backups()
    else:
        log(f"Acción desconocida: {action}", "ERROR")
