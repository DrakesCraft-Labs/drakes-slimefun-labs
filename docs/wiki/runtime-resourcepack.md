# Pipeline del Resource Pack (pack.drakescraft.cl)

El sistema de texturas y modelos 3D de DrakesCraft (vinculado a Slimefun y otros plugins) se distribuye y actualiza de manera automatizada a través de un flujo que conecta GitHub, GitHub Actions, Discord y el servidor remoto `star`.

---

## Arquitectura General

```mermaid
graph TD
    Developer[Desarrollador / Git Push] -->|Push a PortTextura/| GitHub[Repositorio GitHub]
    GitHub -->|Trigger Workflow| Actions[GitHub Actions CI]
    Actions -->|1. Genera ZIP| Actions
    Actions -->|2. Crea Release| Release[GitHub Releases]
    Actions -->|3. Notifica Webhook| Discord[Canal Discord]
    
    subgraph Servidor star (remoto)
        Cron[Cron Job: cada 5 min] -->|Ejecuta update.sh| Script[update.sh]
        Script -->|Consulta Releases API| GitHub
        Script -->|Descarga latest.zip + sha1| NginxDir[/srv/resourcepack/]
        Nginx[Nginx Local :80] -->|Sirve archivos| Cloudflared[Cloudflare Tunnel]
    end

    Cloudflared -->|Expone en internet| Domain[https://pack.drakescraft.cl]
    MinecraftServer[Servidores Minecraft] -->|Descarga zip + valida hash| Domain
```

---

## 1. Integración Continua (CI) en GitHub

El workflow que empaqueta y publica las texturas está definido en:
[`.github/workflows/resourcepack-release.yml`](../../.github/workflows/resourcepack-release.yml)

### Disparadores
* **Automático:** Push a la rama `main` afectando la ruta `PortTextura/Jackstar-Slimefun-Drakecraft/**`, el builder de variantes o el propio workflow.
* **Manual:** Mediante `workflow_dispatch` en la interfaz de GitHub Actions.

### Acciones del CI
1. **Calcular Versión:** Genera un tag dinámico basado en la fecha y el SHA del commit: `resourcepack-YYYYMMDD-shortSHA`.
2. **Variante derivada:** Genera una build `sf-only`, pensada para conservar Slimefun/addons sin la capa de texturas vanilla recoloreadas.
3. **Empaquetado:** Comprime esa variante en `Jackstar-Slimefun-Drakecraft.zip`, dejando ese nombre como asset canónico para la URL fija.
4. **GitHub Release:** Sube ese ZIP y crea una release pública marcada como la más reciente (latest).
5. **Notificación en Discord:** Envía un webhook al canal configurado de Discord con los siguientes datos:
   * Versión / Tag generado.
   * Tamaño del archivo.
   * Hash SHA1 (requerido por Minecraft).
   * URL de descarga directa.

---

## 2. Servidor star — Servidor Web y Sincronización

El servidor `star` actúa como el host local y espejo del resource pack descargado.

### Servidor Web (Nginx)
El dominio **`pack.drakescraft.cl`** está configurado a través de un túnel Cloudflare en `star` apuntando a `http://127.0.0.1:80`. Nginx sirve el directorio local `/srv/resourcepack/`.

* **Archivos servidos:**
  * `index.html`: Una página minimalista con tema oscuro que muestra el hash SHA1 actual, un botón de descarga directa y las líneas de configuración para el servidor.
  * `latest.zip`: El recurso comprimido descargado.
  * `sha1` / `latest.sha1`: El hash SHA1 textual del zip actual.

### Script de Sincronización Automática (`update.sh`)
Ubicado en `/srv/resourcepack/update.sh` y ejecutado por el servicio cron cada 5 minutos (`*/5 * * * *`).

* **Flujo del script:**
  1. Consulta la API de GitHub Releases del repositorio de manera ordenada.
  2. Filtra releases que correspondan a tags `resourcepack-*` y los ordena por su fecha de publicación (`published_at`) para evitar desfases cronológicos.
  3. Obtiene el ZIP de la release más reciente y lo compara con el local.
  4. Si hay una versión más nueva, descarga el archivo, calcula el hash SHA1, reemplaza `latest.zip` y actualiza el archivo `sha1` (y `/srv/resourcepack/latest.sha1`).
  5. Escribe un log persistente en `/var/log/resourcepack-update.log`.

---

## 3. Configuración del Servidor Minecraft

Para implementar el resource pack en el servidor de juego, añade las siguientes líneas en el archivo `server.properties`:

```properties
resource-pack=https://pack.drakescraft.cl/latest.zip
resource-pack-sha1=<Copiar el hash de https://pack.drakescraft.cl/sha1>
resource-pack-prompt=DrakesCraft Resource Pack
```

> [!IMPORTANT]  
> Asegúrate de que el hash en `resource-pack-sha1` coincida exactamente con el de la web, de lo contrario los clientes de Minecraft no actualizarán el paquete si ya tienen una versión previa en caché.

---

## Mantenimiento y Troubleshooting

### Forzar actualización manual en star
Si deseas forzar al script a revisar de inmediato sin esperar el cron:
```bash
sudo /srv/resourcepack/update.sh
```

### Consultar los logs de actualización
```bash
tail -f /var/log/resourcepack-update.log
```

### nginx y túnel Cloudflare
Si el dominio devuelve error 502/504, verifica el estado del túnel Cloudflare y nginx en `star`:
```bash
sudo systemctl status cloudflared
sudo systemctl status nginx
```
