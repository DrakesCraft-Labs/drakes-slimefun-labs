plugins {
    kotlin("jvm") version "2.1.0-Beta1"
    id("com.gradleup.shadow") version "8.3.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.github.drakescraft_labs"
version = "1.21-Drake-v1"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

dependencies {
    library(kotlin("stdlib"))
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.Slimefun:Slimefun4:RC-37")
    implementation("io.github.seggan:sf4k:0.7.1")
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

bukkit {
    name = "CustomItemGenerators"
    main = "com.github.drakescraft_labs.customitemgen.CustomItemGenerators"
    apiVersion = "1.20"
    version = project.version.toString()
    author = "Vaan1310"
    depend = listOf("Slimefun")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveFileName.set("CustomItemGenerators-$version.jar")
    relocate("org.bstats", "me.vaan")

    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk7"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-reflect"))
        exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
