#!/usr/bin/env bash

set -euo pipefail

pushd logging-k8s-stdout-otlp-json
../gradlew assemble
popd

pushd javaagent-declarative-configuration
../gradlew bootJar
popd

./gradlew :doc-snippets:extensions-testapp:jar :doc-snippets:extensions-minimal:shadowJar
./gradlew :doc-snippets:extensions-testapp:jar

wget -q -O - https://raw.githubusercontent.com/k3d-io/k3d/main/install.sh | bash

go install github.com/grafana/oats@v0.3.1
oats -timeout 5m logging-k8s-stdout-otlp-json/
oats -timeout 5m javaagent-declarative-configuration/oats/
oats -timeout 5m doc-snippets/extensions-minimal/oats/
