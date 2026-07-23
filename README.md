<div align="center">

<img src="https://raw.githubusercontent.com/DrakesCraft-Labs/drakes-slimefun-labs/main/labs_addons_banner.svg" alt="Drakes Slimefun Labs Banner" width="920" />

# 🧪 Drakes Slimefun Labs

**Matriz Unificada de Mantenimiento y Aceleración en Rust para los 44 Addons de Slimefun en DrakesCraft**

<p>
  <a href="https://github.com/DrakesCraft-Labs/drakes-slimefun-labs"><img src="https://img.shields.io/badge/GitHub-Drakes--Slimefun--Labs-181717?style=for-the-badge&logo=github" alt="GitHub"/></a>
  <img src="https://img.shields.io/badge/Java-21_FFM_Panama-F89820?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21 FFM"/>
  <img src="https://img.shields.io/badge/Rust-FFM_Accelerated-FF4500?style=for-the-badge&logo=rust&logoColor=white" alt="Rust Native"/>
  <img src="https://img.shields.io/badge/Addons-44_Integrated-10B981?style=for-the-badge" alt="44 Addons"/>
</p>

</div>

---

## ⚡ Modelo Híbrido Cero-Riesgo (Inyección Nativa Panama FFM)

`drakes-slimefun-labs` integra el componente **`RustNativeBridge`** utilizando **Java 21 Project Panama (FFM API)**.

Conecta la matriz de los 44 Addons (`sources/community-addons/`) directamente al motor compilado en Rust (`Slimefun-Rust` / `slimefun_ffi`):
- 🚀 **Ejecución de Tickers en Nanosegundos**: Ticks paralelos multihilo sin Garbage Collector (GC).
- 🛡️ **Preservación Total de Datos (SQLite 0-Reset)**: Mantiene intactos todos los bloques e inventarios existentes en `stored-blocks.db`.

---

## 🛠️ Compilación

```bash
# Compilación global del laboratorio con Gradle / Maven
./gradlew build
```

---

<div align="center">

**DrakesCraft Labs** · Dirigido por [**JackStar6677-1**](https://github.com/JackStar6677-1)

</div>
