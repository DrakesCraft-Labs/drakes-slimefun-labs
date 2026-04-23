dependencies {
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
