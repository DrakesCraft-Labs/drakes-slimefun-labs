import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.gradleup.shadow") version "8.3.2"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"

    id("io.github.seggan.uom")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.metamechanists.org/releases/")
}

dependencies {
    fun DependencyHandlerScope.compileOnlyAndTest(dependency: Any) {
        compileOnly(dependency)
        testImplementation(dependency)
    }

    // Kotlin + coroutines van en el shadow JAR relocados: paperLibrary mezclaba classloaders y rompía
    // kotlin-reflect (KClassImpl) en Paper 1.21.11+ con BytecodeModifyingURLClassLoader.
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation(kotlin("reflect"))
    implementation(kotlin("scripting-common"))
    implementation(kotlin("scripting-jvm"))
    implementation(kotlin("scripting-jvm-host"))
    implementation(kotlin("script-runtime"))

    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.drakescraft_labs:slimefun-core:11.0-Drake-1.21.1-SNAPSHOT")
    compileOnly("com.github.drakescraft_labs:dough-core:1.3.1-DRAKE-v11-SNAPSHOT")
    testImplementation("com.github.drakescraft_labs:slimefun-core:11.0-Drake-1.21.1-SNAPSHOT")
    testImplementation("com.github.drakescraft_labs:dough-core:1.3.1-DRAKE-v11-SNAPSHOT")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    implementation("org.metamechanists:DisplayModelLib:34")

    implementation(project(":sf4k-bakedlibs"))

    testImplementation(kotlin("test"))
    testImplementation("io.strikt:strikt-core:0.34.0")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.21:3.133.2")
    testImplementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-test:2.19.0")
}

group = "io.github.addoncommunity.galactifun"
version = "0.0.1-Drake-1.21.1-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

tasks.compileKotlin {
    compilerOptions.javaParameters = true
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }

    fun doRelocate(from: String) {
        val last = from.split(".").last()
        relocate(from, "io.github.addoncommunity.galactifun.shadowlibs.$last")
    }

    mergeServiceFiles()
    if (System.getenv("RELOCATE") != "false") {
        doRelocate("io.github.seggan.kfun")
        doRelocate("co.aikar.commands")
        doRelocate("co.aikar.locales")
        doRelocate("org.metamechanists.displaymodellib")
        relocate("kotlin", "io.github.addoncommunity.galactifun.shadowlibs.kotlin")
        relocate("kotlinx", "io.github.addoncommunity.galactifun.shadowlibs.kotlinx")
        relocate("com.github.shynixn.mccoroutine", "io.github.addoncommunity.galactifun.shadowlibs.mccoroutine")
    } else {
        archiveClassifier = "unrelocated"
    }

    archiveBaseName = "galactifun2"
}

paper {
    name = rootProject.name
    main = "io.github.addoncommunity.galactifun.Galactifun2"
    bootstrapper = "io.github.addoncommunity.galactifun.Galactifun2Bootstrapper"
    version = project.version.toString()
    author = "Seggan"
    apiVersion = "1.21"
    // Sin paper-libraries: Kotlin va en el shadow JAR (ver comentario en dependencies).
    generateLibrariesJson = false

    serverDependencies {
        register("Multiverse-Core") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.AFTER
        }
        // Convivencia opcional con Galactifun clásico (nombre Drake del JAR)
        register("Galactifun-drake") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.AFTER
        }
        register("Galactifun") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.AFTER
        }

        register("Slimefun") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
    }
}

tasks.runServer {
    javaLauncher = javaToolchains.launcherFor {
        @Suppress("UnstableApiUsage")
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition", "-XX:+AllowRedefinitionToAddDeleteMethods")
    downloadPlugins {
        url("https://blob.build/dl/Slimefun4/Dev/1154")
    }
    maxHeapSize = "4G"
    minecraftVersion("1.21.1")
}

uom {
    val kmPerLy = 9.461e12
    val kmPerAu = 1.495978707e8

    allowKoltinxSerialization = true
    pkg = "io.github.addoncommunity.galactifun.units"
    val time = existingMeasure("kotlin.time.Duration", "doubleSeconds") {
        scalarToUnit("seconds", true)
    }
    val distance = measure("Distance", "meters") {
        unit("lightYears", kmPerLy * 1000)
        unit("kilometers", 1000.0)
        unit("au", kmPerAu * 1000)
    }
    val mass = measure("Mass", "kilograms") {
        unit("tons", 1000.0)
    }
    val velocity = measure("Velocity", "metersPerSecond")
    val acceleration = measure("Acceleration", "metersPerSecondSquared")
    val force = measure("Force", "newtons") {
        unit("kilonewtons", 1000.0)
        unit("meganewtons", 1_000_000.0)
    }
    val pressure = measure("Pressure", "pascals") {
        unit("atmospheres", 101325.0)
    }
    val area = measure("Area", "squareMeters")
    val volume = measure("Volume", "cubicMeters") {
        unit("liters", 0.001)
    }
    val density = measure("Density", "kilogramsPerCubicMeter") {
        unit("kilogramsPerLiter", 1000.0)
    }

    distance times time resultsIn velocity
    acceleration times time resultsIn velocity
    density times volume resultsIn mass
    acceleration times mass resultsIn force
    distance times distance resultsIn area
    distance times area resultsIn volume
    pressure times area resultsIn force

    measure("Angle", "radians") {
        unit("degrees", Math.PI / 180)
    }
}