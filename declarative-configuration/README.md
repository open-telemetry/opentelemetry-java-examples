# Declarative Configuration Example

This example demonstrates how to use [declarative configuration](https://opentelemetry.io/docs/specs/otel/configuration/#declarative-configuration) to configure the OpenTelemetry SDK using a YAML configuration file as defined in [opentelemetry-configuration](https://github.com/open-telemetry/opentelemetry-configuration).

The configuration file is located at [otel-sdk-config.yaml](./otel-sdk-config.yaml).

# How to run

## Prerequisites

* Java 1.8

## Run

```shell script
export OTEL_EXPERIMENTAL_CONFIG_FILE=$(pwd)/otel-sdk-config.yaml
../gradlew run
```

Observe how data is printed to the console as configured in [otel-sdk-config.yaml](./otel-sdk-config.yaml).

For use with the OpenTelemetry Java, see [Java Agent declarative configuration](../javaagent/README.md#declarative-configuration).
