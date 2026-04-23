# 🏛️ Tomorrow-Handoff (v16.0)

## 📊 Estado de la Gran Obra
- **Rama**: `1.21-latin` | **Identidad**: `com.github.drakescraft-labs`
- **Integración Estructural**: **100% (89/89 addons)**.
- **Reactor Maven**: 80 módulos (Core + 78 Addons).
- **Reactor Gradle**: 9 módulos (Galactifun, Bump, etc.).
- **Progreso Quirúrgico**: 60/89 addons ya tienen rebranding `-drake`.

## 🛰️ Infraestructura CI/CD
- **Unified Engine**: El workflow maestro ya está operativo y valida ambos reactores en paralelo.
- **GitHub Packages**: Registro purgado de basura antigua. Solo queda el stack premium.

## 🐍 DrakesLab Manager
- Usar siempre `python scripts/manager.py audit` para ver el estado real antes de empezar.
- **NUEVO**: `python scripts/manager.py audit --sync` actualiza automáticamente las métricas del `README.md` (ES/EN) y Checklists.
- **REPARACIÓN MAESTRA**: `python scripts/manager.py repair` ahora fuerza la versión `11-SNAPSHOT` del padre y reconstruye bloques dañados.
- El comando `python scripts/manager.py` (sin flags) repara identidades XML y versiones de padre.

## 🎯 Siguiente Ruta Recomendada (Fase Quirúrgica)
1.  **Rebranding Masivo**: Aplicar el estándar `-drake` a los 20 addons que aún están en estado `STABILIZED`.
2.  **Smoke Testing**: Iniciar pruebas de carga en servidores Paper 1.21.1 reales con el stack completo.
3.  **Modernización Gradle**: Continuar con la limpieza de los addons de Gradle siguiendo el patrón de Galactifun.

## 🛠️ Comandos de Supervivencia
- **Audit**: `python scripts/manager.py audit`
- **Build Maven**: `mvn -pl sources/community-addons/AddonName -am -DskipTests package`
- **Build Gradle**: `./gradlew :sources:batch-2-expansion:Galactifun:build`

## 🔗 Enlaces Estratégicos
- [Checklist de Migración](migration-checklist.md)
- [Guía de Porteo 1.21.1](../docs/README-PORT-1.21.1.md)
- [Estrategia de Releases y CI](release-and-ci-strategy.md)
