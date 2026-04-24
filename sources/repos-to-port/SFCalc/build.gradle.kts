plugins {
    java
    id("com.gradleup.shadow") version "8.3.10"
}

repositories {
    mavenLocal()
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.20.0")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.Slimefun:Slimefun4:RC-37")

    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.github.seggan:ErrorReporter-Java:1.1.0")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

group = "io.github.seggan.sfcalc"
version = "UNOFFICIAL"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.shadowJar {
    archiveClassifier.set("")

    relocate("io.github.seggan.errorreporter", "io.github.seggan.sfcalc.errorreporter")
    relocate("org.bstats", "io.github.seggan.sfcalc.bstats")
}
