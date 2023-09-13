# File Configuration Example

This example demonstrates how to use a YAML configuration file as defined in [opentelemetry-configuration](https://github.com/open-telemetry/opentelemetry-configuration) to configure the OpenTelemetry SDK.

The configuration file is located at [otel-sdk-config.yaml](./otel-sdk-config.yaml).

# How to run

## Prerequisites

* Java 1.8

## Run

```shell script
export OTEL_CONFIG_FILE=$(pwd)/otel-sdk-config.yaml
../gradlew run
```

Observe how data is printed to the console as configured in [otel-sdk-config.yaml](./otel-sdk-config.yaml).
