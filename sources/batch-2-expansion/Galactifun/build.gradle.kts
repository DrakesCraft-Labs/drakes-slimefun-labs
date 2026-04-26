plugins {
    java
    id("com.gradleup.shadow") version "8.3.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

version = "11.0-Drake-1.21.1-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.drakescraft_labs:slimefun-core:11.0-Drake-1.21.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    
    implementation("com.github.drakescraft_labs:infinitylib-drake:1.3.11-DRAKE-SNAPSHOT")
}

java.sourceCompatibility = JavaVersion.VERSION_21

tasks.shadowJar {
    relocate("io.github.mooy1.infinitylib", "com.github.drakescraft_labs.galactifun.infinitylib")
    archiveFileName.set("Galactifun-11.0-Drake-1.21.1-SNAPSHOT.jar")
}

bukkit {
    name = "Galactifun-drake"
    description = "Space addon for Slimefun"
    main = "com.github.drakescraft_labs.galactifun.Galactifun"
    version = project.version.toString()
    authors = listOf("Seggan", "Mooy1", "GallowsDove", "ProfElements")
    apiVersion = "1.21"
    depend = listOf("Slimefun")

    commands {
        register("galactifun-drake") {
            description = "Galactifun main command"
            usage = "/galactifun-drake <subcommand>"
            aliases = listOf("galactifun", "gf", "galactic")
        }
    }
}