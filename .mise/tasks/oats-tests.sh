#!/usr/bin/env bash
#MISE description="Run OATS tests for example projects"

set -euo pipefail

# Java is not managed by mise, but any java 17+ installation should work
./gradlew \
  :doc-snippets:extensions-testapp:jar :doc-snippets:extensions-minimal:shadowJar \
  :opentelemetry-examples-javaagent-declarative-configuration:bootJar \
  :opentelemetry-examples-logging-k8s-stdout-otlp-json:assemble \
  :opentelemetry-examples-spring-declarative-configuration:bootJar

oats -timeout 5m doc-snippets/extensions-minimal/oats/
oats -timeout 5m javaagent-declarative-configuration/oats/
oats -timeout 5m logging-k8s-stdout-otlp-json/
oats -timeout 5m spring-declarative-configuration/oats/
