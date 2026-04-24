# EnderCarryOn (1.20.6 Port)

EnderCarryOn es un plugin para PaperMC basado en el mod CarryOn. Esta versión ha sido adaptada específicamente para **Minecraft 1.20.6**, utilizando `CustomModelData` para las texturas de carga.

## Características
- Carga de Cofres, Cofres Trampa, Ender Chests y Barriles.
- Restricciones de movimiento e interacción mientras se carga un bloque.
- Renderizado 3D del bloque cargado en las manos del jugador (requiere Resource Pack).

## Dependencias Obligatorias
Este plugin **requiere** [EnderCore](https://github.com/JackStar6677-1/EnderCore) para funcionar correctamente. Asegúrate de tener instalado `EnderCore v0.2.0` o superior.

## 🎨 Resource Pack (Requerido)
Para la versión 1.20.6, el sistema de texturas nativo de 1.21.3 no funciona. Por ello, es **obligatorio** usar el Resource Pack adaptado incluido en este repositorio.

### Instalación del Resource Pack:
1. Descarga el archivo [**resourcepack.zip**](./resourcepack.zip) de este repositorio.
2. Puedes instalarlo localmente en tu cliente (`.minecraft/resourcepacks`).
3. **Recomendado**: Súbelo a un host (como Dropbox o MCPacks) y coloca el enlace en la `config.yml` del plugin para que los jugadores lo descarguen automáticamente al entrar.

## Configuración
En el `config.yml`, puedes ajustar:
- `resource_pack.use`: Activa/Desactiva el envío automático del pack.
- `resource_pack.url`: El link de descarga directa de tu Resource Pack.
- `resource_pack.hash`: El hash SHA-1 (en minúsculas) del archivo zip.

## Uso
- **Shift (Agacharse) + Clic Derecho** con la **mano vacía** sobre un bloque válido para cargarlo.
- Para soltarlo, simplemente haz clic derecho de nuevo.

## Créditos
Basado en el mod **CarryOn** y el plugin original de **Endkind**.
Esta versión 1.20.6 fue adaptada por JackStar6677-1.

## Licencia
Este proyecto está bajo la licencia [MIT](LICENSE).

<!-- DRAKES-STATUS:BEGIN -->
> Estado de sincronizacion: **2026-04-24**.
> Baseline tecnico vigente: **Paper 1.21.1 + Java 21**.
> CI principal en `1.21-latin`: **Gates 1-5 en verde**.
> Nota: el monorepo completo sigue en migracion incremental por lotes.
<!-- DRAKES-STATUS:END -->
