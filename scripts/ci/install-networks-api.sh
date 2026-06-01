#!/usr/bin/env bash
set -euo pipefail

readonly NETWORKS_VERSION="11.0.0-drake.4"
readonly NETWORKS_SHA256="b8b894a65d8e4d9ea0d9c10f90499aed00ecb9fddeb26cb26513564cf8f7d86c"
readonly NETWORKS_URL="https://github.com/DrakesCraft-Labs/NetworksV6-drake/releases/download/v${NETWORKS_VERSION}/NetworksV6-Drake-v${NETWORKS_VERSION}.jar"
readonly TARGET_DIR="${RUNNER_TEMP:-${TMPDIR:-/tmp}}/drakes-networks-api"
readonly TARGET_JAR="${TARGET_DIR}/NetworksV6-Drake-v${NETWORKS_VERSION}.jar"

mkdir -p "${TARGET_DIR}"
echo "[INFO] Descargando NetworksV6-drake ${NETWORKS_VERSION}"
curl --fail --silent --show-error --location --retry 3 --output "${TARGET_JAR}" "${NETWORKS_URL}"
echo "${NETWORKS_SHA256}  ${TARGET_JAR}" | sha256sum --check --status
echo "[INFO] Instalando API Networks verificada en el repositorio Maven local"
mvn -B -ntp install:install-file \
  -Dfile="${TARGET_JAR}" \
  -DgroupId=com.github.drakescraft_labs \
  -DartifactId=NetworksV6-drake \
  -Dversion="${NETWORKS_VERSION}" \
  -Dpackaging=jar \
  -DgeneratePom=true
echo "[SUCCESS] NetworksV6-drake ${NETWORKS_VERSION} lista para compilar integraciones."
