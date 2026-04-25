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
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.drakescraft_labs:slimefun-core:11.0-Drake-1.21.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    implementation("net.guizhanss:guizhanlib-all:2.2.0")
    implementation("org.bstats:bstats-bukkit:3.1.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.shadowJar {
    relocate("net.guizhanss.guizhanlib", "com.github.drakescraft_labs.slimefuntranslation.libs.guizhanlib")
    relocate("org.bstats", "com.github.drakescraft_labs.slimefuntranslation.libs.bstats")
    archiveFileName.set("SlimefunTranslation-drake.jar")
    minimize()
}

bukkit {
    main = "net.guizhanss.slimefuntranslation.SlimefunTranslation"
    apiVersion = "1.21"
    authors = listOf("ybw0014")
    description = "A Slimefun Addon that translates items without actually modifying the items. DrakesLab Edition."
    depend = listOf("Slimefun", "ProtocolLib", "PlaceholderAPI")

    commands {
        register("sftranslation") {
            description = "SlimefunTranslation command"
            aliases = listOf("slimefuntranslation", "sft", "sftr", "sftransl")
        }
    }
}
