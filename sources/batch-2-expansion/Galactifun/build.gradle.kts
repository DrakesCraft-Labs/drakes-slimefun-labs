plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    api("com.github.drakescraft_labs:infinitylib-drake:1.3.11-DRAKE-SNAPSHOT")
}

java.sourceCompatibility = JavaVersion.VERSION_21

tasks.shadowJar {
    relocate("io.github.mooy1.infinitylib", "com.github.drakescraft_labs.galactifun.infinitylib")
    archiveFileName.set("Galactifun-drake.jar")
}

bukkit {
    name = "Galactifun-drake"
    description = "Space addon for Slimefun"
    main = "io.github.addoncommunity.galactifun.Galactifun"
    version = project.version.toString()
    authors = listOf("Seggan", "Mooy1", "GallowsDove", "ProfElements")
    apiVersion = "1.21"
    depend = listOf("Slimefun")

    commands {
        register("galactifun") {
            description = "Galactifun main command"
            usage = "/galactifun <subcommand>"
            aliases = listOf("gf", "galactic")
        }
    }
}