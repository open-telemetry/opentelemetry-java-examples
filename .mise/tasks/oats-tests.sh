#!/usr/bin/env bash
#MISE description="Run OATS tests for example projects"

set -euo pipefail

# Java is not managed by mise, but any java 17+ installation should work
./gradlew \
  :doc-snippets:extensions-testapp:jar :doc-snippets:extensions-minimal:shadowJar \
  :opentelemetry-examples-javaagent-declarative-configuration:bootJar \
  :opentelemetry-examples-logging-k8s-stdout-otlp-json:assemble \
  :opentelemetry-examples-spring-declarative-configuration:bootJar

workdir="$(mktemp -d)"
trap 'rm -rf "$workdir"' EXIT
export GOBIN="$workdir/bin"
mkdir -p "$GOBIN"

# renovate: datasource=github-releases depName=gcx packageName=grafana/gcx
export GCX_VERSION=v0.4.0
go install "github.com/grafana/gcx/cmd/gcx@${GCX_VERSION}"

git clone --depth 1 --branch v2 https://github.com/grafana/oats "$workdir/oats-src"
GOWORK=off go -C "$workdir/oats-src" build -o "$workdir/oats" ./cmd/v2

"$workdir/oats" \
  --config oats.toml \
  --gcx "$GOBIN/gcx" \
  --no-cache \
  --timeout=10m
