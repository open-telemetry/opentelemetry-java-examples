#!/usr/bin/env bash
#MISE description="Run OATS tests for example projects"

set -euo pipefail

# Java is not managed by mise, but any java 17+ installation should work
./gradlew :logging-k8s-stdout-otlp-json:assemble
oats -timeout 5m logging-k8s-stdout-otlp-json/

./gradlew :javaagent-declarative-configuration:bootJar
oats -timeout 5m javaagent-declarative-configuration/oats/

./gradlew :doc-snippets:extensions-testapp:jar :doc-snippets:extensions-minimal:shadowJar
oats -timeout 5m doc-snippets/extensions-minimal/oats/

./gradlew :spring-declarative-configuration:bootJar
oats -timeout 5m spring-declarative-configuration/oats/
