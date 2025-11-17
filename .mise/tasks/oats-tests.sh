#!/usr/bin/env bash
#MISE description="Run OATS tests for example projects"

set -euo pipefail

pushd logging-k8s-stdout-otlp-json
../gradlew clean assemble
popd

pushd javaagent-declarative-configuration
../gradlew clean bootJar
popd

oats -timeout 5m logging-k8s-stdout-otlp-json/
oats -timeout 5m javaagent-declarative-configuration/oats/
