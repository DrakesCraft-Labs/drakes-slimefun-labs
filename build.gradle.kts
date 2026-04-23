plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "com.github.drakescraft-labs"
    version = "11.0-Drake-1.21.1-SNAPSHOT"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
        maven("https://maven.pkg.github.com/DrakesCraft-Labs/drakes-slimefun-labs") {
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    dependencies {
        // Todas las dependencias comunes se gestionan aquí
        val paperVersion = "1.21.1-R0.1-SNAPSHOT"
        val slimefunVersion = "11.0-Drake-1.21.1-SNAPSHOT"

        compileOnly("io.papermc.paper:paper-api:$paperVersion")
        compileOnly("com.github.drakescraft-labs:slimefun-core:$slimefunVersion")
        compileOnly("com.github.drakescraft-labs:dough-core:1.3.1-DRAKE-v11-SNAPSHOT")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}
