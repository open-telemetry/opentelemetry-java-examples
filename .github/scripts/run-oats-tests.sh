#!/usr/bin/env bash

set -euo pipefail

pushd logging-k8s-stdout-otlp-json
../gradlew assemble
popd

pushd javaagent-declarative-configuration
../gradlew bootJar
popd

mise install k3d

# renovate: datasource=go depName=github.com/grafana/oats
OATS_VERSION=v0.4.1
go install github.com/grafana/oats@${OATS_VERSION}
oats -timeout 5m logging-k8s-stdout-otlp-json/
oats -timeout 5m javaagent-declarative-configuration/oats/
