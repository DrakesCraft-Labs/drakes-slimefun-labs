/**
 * Vendored sf4k (https://github.com/Seggan/sf4k) for Drake Slimefun + bakedlibs dough.
 * Changes: Slimefun packages -> com.github.drakescraft_labs.slimefun4; BlockPosition -> io.github.bakedlibs.dough.
 */
plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "1.9.23"
    `java-library`
}

group = "io.github.drakescraft_labs.sf4k"
version = "0.8.2-DRAKE-BAKEDLIBS-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.metamechanists.org/releases/")
}

dependencies {
    compileOnlyApi("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnlyApi("com.github.drakescraft_labs:slimefun-core:11.0-Drake-1.21.1-SNAPSHOT")
    compileOnlyApi("com.github.drakescraft_labs:dough-core:1.3.1-DRAKE-v11-SNAPSHOT")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.19.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.19.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
    api(kotlin("reflect"))
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

tasks.compileKotlin {
    compilerOptions {
        javaParameters = true
    }
}
