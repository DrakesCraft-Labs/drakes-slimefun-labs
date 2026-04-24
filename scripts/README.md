# 🛠️ DrakesCraft Scripts & Automation

Este directorio contiene el motor de automatización utilizado para la modernización y mantenimiento masivo del ecosistema **DrakesVanillaSlimefun+**.

## 🐍 ¿Por qué Python?

Dadas las dimensiones del proyecto (+89 módulos), realizar cambios estructurales en los archivos `pom.xml` o refactorizar namespaces en miles de archivos Java de forma manual es inviable. Hemos elegido **Python 3** por:

1. **Manipulación Masiva**: Capacidad para iterar sobre cientos de archivos y aplicar reglas de sustitución (Regex) de forma quirúrgica.
2. **Validación de Integridad**: El script verifica que cada cambio mantenga la estructura XML válida antes de guardar.
3. **Agilidad en el Porting**: Permite actualizar versiones de APIs (Paper, Slimefun) en toda la flota con un solo comando.
4. **Independencia del IDE**: Las reparaciones se pueden ejecutar en entornos CI/CD (GitHub Actions) o localmente sin depender de refactorizaciones pesadas del IDE.

---

## 🎮 El Manager (`manager.py`)

El `manager.py` es el centro de mando del reactor. Sus funciones principales son:

### 1. `repair`
Realiza una "cirugía estructural" en todos los módulos Maven:
- **Herencia Forzada**: Asegura que todos los módulos hereden del `pom.xml` raíz.
- **Unificación de Versiones**: Elimina versiones hardcodeadas de `paper-api` y `acf-paper` para usar las del reactor global.
- **Identidad de Laboratorio**: Rebrandea GroupIds y ArtifactIds al estándar `com.github.drakescraft_labs`.
- **Seguridad**: Inyecta automáticamente parches para librerías vulnerables (como `commons-lang-drake-patched`).

### 2. `rebrand-imports`
Escanea los archivos `.java` y `.kt` para actualizar los namespaces de las librerías internas:
- Migra de `io.github.bakedlibs` a `dev.drake.dough`.
- Sincroniza los paquetes de `Slimefun`, `InfinityLib` y `SefiLib` a la nueva estructura unificada.

### 3. `rebrand-shades`
Sincroniza las reglas de reubicación (`<relocation>`) en los archivos POM para que coincidan con los nuevos namespaces del código fuente, evitando colisiones de clases en el servidor.

### 4. `audit`
Genera un informe detallado del estado del reactor:
- % de progreso de la migración.
- Lista de módulos estabilizados vs. pendientes.
- Sincronización automática de métricas con el `README.md`.

---

## 🚀 Uso Rápido

```powershell
# Reparar estructura de todos los POMs
python scripts/manager.py repair

# Refactorizar imports en todo el código fuente
python scripts/manager.py rebrand-imports

# Ver estado actual del reactor
python scripts/manager.py audit --sync
```

> [!IMPORTANT]
> El manager realiza backups automáticos (`.bak`) de cada archivo modificado. Puedes limpiarlos con `python scripts/manager.py clean-backups`.
