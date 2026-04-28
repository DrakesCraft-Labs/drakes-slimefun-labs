plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.2.1"
}

group = "io.github.addoncommunity.galactifun"
version = "1.0.0"
description = "Galactifun is a Slimefun addon that adds galaxies, star systems, planets, moons, rockets, and spacesuits."

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
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    "githubCompileOnly"("Slimefun5:Slimefun5:v5.0.3")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    implementation("com.github.Riley31415:InfinityLib:1.3.10") {
        isTransitive = false
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
        archiveFileName.set("Galactifun v${project.version}.jar")
        relocate("io.github.mooy1.infinitylib", "io.github.addoncommunity.galactifun.infinitylib")
        relocate("org.bstats", "io.github.addoncommunity.galactifun.bstats")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
    test {
        useJUnitPlatform()
    }
}
