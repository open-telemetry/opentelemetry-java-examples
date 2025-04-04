#!/usr/bin/env bash

set -euo pipefail

pushd logging-k8s-stdout-otlp-json
../gradlew assemble
popd

wget -q -O - https://raw.githubusercontent.com/k3d-io/k3d/main/install.sh | bash

go install github.com/grafana/oats@v0.2.0
oats -timeout 5m logging-k8s-stdout-otlp-json/
