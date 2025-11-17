#!/usr/bin/env bash

set -euo pipefail

pushd logging-k8s-stdout-otlp-json
../gradlew assemble
popd

pushd javaagent-declarative-configuration
../gradlew bootJar
popd

# renovate: datasource=github-releases depName=k3d-io/k3d
K3D_VERSION=v5.8.3
wget -q -O - https://raw.githubusercontent.com/k3d-io/k3d/${K3D_VERSION}/install.sh | TAG=${K3D_VERSION} bash

# renovate: datasource=go depName=github.com/grafana/oats
OATS_VERSION=v0.4.1
go install github.com/grafana/oats@${OATS_VERSION}
oats -timeout 5m logging-k8s-stdout-otlp-json/
oats -timeout 5m javaagent-declarative-configuration/oats/
