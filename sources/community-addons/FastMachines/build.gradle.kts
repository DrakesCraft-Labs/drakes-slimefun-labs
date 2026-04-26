import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "8.3.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    fun compileOnlyAndTestImpl(dependencyNotation: Any) {
        compileOnly(dependencyNotation)
        testImplementation(dependencyNotation)
    }

    compileOnly(kotlin("stdlib")) // loaded through library loader
    compileOnly(kotlin("reflect")) // loaded through library loader
    compileOnlyAndTestImpl("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnlyAndTestImpl("com.github.drakescraft_labs:slimefun-core:11.0-Drake-1.21.1-SNAPSHOT")
    compileOnlyAndTestImpl("com.github.drakescraft_labs:dough-core:1.3.1-DRAKE-v11-SNAPSHOT")
    compileOnly("net.guizhanss:SlimefunTranslation:e6da231617")
    compileOnly("com.github.schntgaispock:SlimeHUD:1.3.0")
    compileOnly("com.github.drakescraft_labs:InfinityExpansion-drake:1.20.6-Drake-SNAPSHOT")
    compileOnly("com.github.VoperAD:SlimeFrame:8af2379a01")
    compileOnly("net.guizhanss:InfinityExpansion2:8d3e6c40f6")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("net.guizhanss:guizhanlib-all:2.5.0")
    implementation("net.guizhanss:guizhanlib-kt-all:0.2.0")

    testImplementation(kotlin("test"))
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.10.0")
}

group = "com.github.drakescraft_labs"
description = "FastMachines"

version = "11.0-Drake-1.21.1-SNAPSHOT"

/** Paquete real del fork Drake (upstream Guizhan); coincide con las fuentes Kotlin. */
val mainPackage = "net.guizhanss.fastmachines"

java {
    disableAutoTargetJvm()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        javaParameters = true
        jvmTarget = JvmTarget.JVM_21
    }
}

// Shadow necesita el JAR de Kotlin como entrada; el clasificador "thin" evita confundirlo con el fat JAR del servidor.
tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier.set("thin")
}

tasks.shadowJar {
    fun doRelocate(from: String, to: String? = null) {
        val last = to ?: from.split(".").last()
        relocate(from, "com.github.drakescraft_labs.fastmachines.libs.$last")
    }

    doRelocate("net.byteflux.libby")
    doRelocate("net.guizhanss.guizhanlib")
    doRelocate("org.bstats")
    doRelocate("io.papermc.lib", "com.github.drakescraft_labs.fastmachines.libs.paperlib")
    relocate("io.github.thebusybiscuit.slimefun4", "com.github.drakescraft_labs.slimefun4")
    // minimize() podía quitar la clase principal FastMachines del JAR sombreado
    archiveClassifier.set("")
    archiveFileName.set("FastMachines-11.0-Drake-1.21.1-SNAPSHOT.jar")
}

bukkit {
    main = "$mainPackage.FastMachines"
    apiVersion = "1.18"
    authors = listOf("ybw0014")
    description = "More Slimefun machines that bulk craft items with all shapeless recipes"
    depend = listOf("Slimefun")
    softDepend = listOf(
        "GuizhanLibPlugin",
        "SlimefunTranslation",
        "InfinityExpansion",
        "SlimeFrame",
        "SlimeHUD",
        "InfinityExpansion2",
    )
    loadBefore = listOf(
        "SlimeCustomizer",
        "RykenSlimeCustomizer",
        "SlimeFunRecipe"
    )
}

tasks.runServer {
    downloadPlugins {
        // Slimefun
        url("https://builds.guizhanss.com/api/download/SlimefunGuguProject/Slimefun4/master/latest")
        // SlimeHUD
        url("https://blob.build/dl/SlimeHUD/Dev/latest")
        // GuizhanCraft for testing convenient
        url("https://builds.guizhanss.com/api/download/ybw0014/GuizhanCraft/master/latest")
    }
    jvmArgs("-Dcom.mojang.eula.agree=true")
    minecraftVersion("1.21.1")
}

tasks.test {
    useJUnitPlatform()
}
