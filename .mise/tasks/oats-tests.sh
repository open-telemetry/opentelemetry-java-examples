#!/usr/bin/env bash
#MISE description="Run OATS tests for example projects"

set -euo pipefail

pushd logging-k8s-stdout-otlp-json
../gradlew clean assemble
popd

pushd javaagent-declarative-configuration
../gradlew clean bootJar
popd

pushd spring-declarative-configuration
../gradlew clean bootJar
popd

# timeout for each test suite is 5 minutes
oats -timeout 5m .
