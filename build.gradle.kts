plugins {
    base
}

description = "Legacy source archive; standalone repositories own addon versions and builds."

tasks.register("legacyStatus") {
    group = "help"
    description = "Explains why this repository no longer builds addon subprojects."
    doLast {
        println("LEGACY: build and release each addon from its standalone repository.")
        println("See docs/CANONICAL_SOURCES.md and migration/addons.json.")
    }
}
