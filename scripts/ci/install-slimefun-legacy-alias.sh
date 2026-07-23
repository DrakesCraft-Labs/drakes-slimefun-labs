#!/usr/bin/env bash
set -euo pipefail

# Algunos addons Gradle aun declaran el identificador 1.21.1. El bytecode es el
# mismo core Drake 1.21.11 ya verificado; este alias solo conserva su classpath.
SLIMEFUN_JAR="$(find slimefun-core/target -maxdepth 1 -type f -name 'Slimefun-*.jar' ! -name 'original-*' ! -name '*-sources.jar' | head -n 1)"
test -n "$SLIMEFUN_JAR"

mvn -B -ntp install:install-file \
  -Dfile="$SLIMEFUN_JAR" \
  -DgroupId=com.github.drakescraft_labs \
  -DartifactId=slimefun-core \
  -Dversion=11.0-Drake-1.21.1-SNAPSHOT \
  -Dpackaging=jar \
  -DgeneratePom=true
