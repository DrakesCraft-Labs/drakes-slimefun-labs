# Base Paper 26.x (rama `26.X-ToTheStars`)

Esta rama prepara el salto a la línea **Paper API 26.x** (artefactos tipo `26.1.x.build.*-alpha` en [repo.papermc.io](https://repo.papermc.io/repository/maven-public/)), manteniendo `1.21-latin` como referencia estable para **Paper 1.21.1** / smoke **1.21.1** y **1.21.11**.

## Maven

En el `pom.xml` raíz existe el perfil **`paper-26-preview`**, inactivo por defecto:

```bash
mvn -B -DskipTests -Ppaper-26-preview compile -fae
```

Sobreescribe la propiedad `paper.version` con la alpha indicada en el POM (revisar y actualizar cuando Paper publique builds nuevas).

## Expectativas

- Muchos addons seguirán fallando hasta alinear imports, deprecaciones y posibles cambios de paquetes respecto a `1.21.1-R0.1-SNAPSHOT`.
- El smoke local y el workflow **Smoke Runtime 1.21** siguen pensados para la serie 1.21; añadir jobs o perfiles específicos 26.x será trabajo posterior en esta rama.

## Referencias

- Javadoc Paper: [jd.papermc.io](https://jd.papermc.io/paper/)
- Descargas del servidor: [papermc.io/downloads/paper](https://papermc.io/downloads/paper)
