plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.2.1"
}

group = "com.github.slimefun"
version = "1.0.0"
description = "SensibleToolbox is a Spigot/Paper plugin which adds tons of items and machines inspired by popular mods."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    "githubCompileOnly"("Slimefun5:Slimefun5:v5.0.3")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("com.gmail.filoghost.holographicdisplays:holographicdisplays-api:2.4.9")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8") {
        exclude(group = "de.schlichtherle", module = "truezip")
    }

    implementation("org.bstats:bstats-bukkit:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.107.0") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

configurations.testImplementation {
    extendsFrom(configurations.compileOnly.get())
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("SensibleToolbox v${project.version}.jar")
        relocate("org.bstats", "io.github.thebusybiscuit.sensibletoolbox.libraries.bstats")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
    test {
        useJUnitPlatform()
    }
}
