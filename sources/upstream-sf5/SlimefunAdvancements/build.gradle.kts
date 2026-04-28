plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.2.1"
    id("jacoco")
}

group = "me.char321"
version = "MODIFIED"
description = "Slimefun Advancements is a Slimefun addon adding an advancement system."

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
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    "githubCompileOnly"("Slimefun5:Slimefun5:v5.0.3")

    // Shaded libraries
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.github.qwertyuioplkjhgfd:AdvancementAPI:f243bdaf75") {
        isTransitive = false
    }
    implementation("com.github.baked-libs.dough:dough-api:1108163a49") {
        isTransitive = false
    }

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
        archiveFileName.set("sfadvancements-${project.version}.jar")
        relocate("org.bstats", "me.char321.sfadvancements.libs.bstats")
        relocate("net.roxeez.advancement", "me.char321.sfadvancements.libs.advancementapi")
        relocate("io.github.bakedlibs.dough", "me.char321.sfadvancements.libs.dough")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
        }
    }
}
