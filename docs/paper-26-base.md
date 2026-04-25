# Base Paper 26.x (rama `26.X-ToTheStars`)

## Conclusión (estado en esta rama)

| Aspecto | Situación |
|---------|-----------|
| **Objetivo** | Preparar el monorepo **Drakes Slimefun Labs** para **Minecraft / Paper 26.x** (nueva línea de API en [repo.papermc.io](https://repo.papermc.io/repository/maven-public/), p. ej. `26.1.2.build.*-alpha`). |
| **Qué hay hoy** | Perfil Maven **`paper-26-preview`** en el `pom.xml` raíz que fija `paper.version` a una build **alpha** 26.x, y documentación/README alineados con esta rama. El código y la mayoría de `plugin.yml` siguen pensados en el porte desde **1.21.x**. |
| **Qué no hay aún** | CI y smoke **dedicados** a servidor Paper 26.x en cada push; paridad completa de addons; scripts de porte masivo tipo `port_paper_26` (pendiente de definir según breaking changes). |
| **Referencia estable** | La rama **`1.21-latin`** sigue siendo la fuente de verdad para **Paper 1.21.x**, smoke **1.21.1 / 1.21.11** y releases publicados. |

Esta rama prepara el salto a la línea **Paper API 26.x** (artefactos tipo `26.1.x.build.*-alpha` en [repo.papermc.io](https://repo.papermc.io/repository/maven-public/)), manteniendo `1.21-latin` como referencia estable para **Paper 1.21.x** y los flujos de smoke/release allí definidos.

## Maven

En el `pom.xml` raíz existe el perfil **`paper-26-preview`**, inactivo por defecto:

```bash
mvn -B -DskipTests -Ppaper-26-preview compile -fae
```

Sobreescribe la propiedad `paper.version` con la alpha indicada en el POM (revisar y actualizar cuando Paper publique builds nuevas).

Sin el perfil, el reactor usa **`1.21.1-R0.1-SNAPSHOT`** como hasta ahora, para permitir comparar diffs de compilación módulo a módulo.

## Expectativas

- Muchos addons seguirán fallando con **`-Ppaper-26-preview`** hasta alinear imports, deprecaciones y posibles cambios de paquetes respecto a `1.21.1-R0.1-SNAPSHOT`.
- El smoke local y el workflow **Smoke Runtime 1.21** siguen pensados para la serie **1.21.x**; añadir jobs o perfiles específicos **26.x** será trabajo posterior en esta rama.

## Referencias

- Javadoc Paper: [jd.papermc.io](https://jd.papermc.io/paper/)
- Descargas del servidor: [papermc.io/downloads/paper](https://papermc.io/downloads/paper)
