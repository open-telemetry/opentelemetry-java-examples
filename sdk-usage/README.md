# SDK Usage Examples

This is a simple example that demonstrates how to use and configure the OpenTelemetry SDK.

## Prerequisites

* Java 17 or higher is required to run Gradle and build this example
* Java 8 or higher may be used to run the example once it has been built

## Compile

Compile with:

```shell script
../gradlew shadowJar
```

## Run

Use the following commands to run the examples:

```shell script
java -cp build/libs/opentelemetry-examples-sdk-usage-0.1.0-SNAPSHOT-all.jar io.opentelemetry.sdk.example.ConfigureTraceExample
```

```shell script
java -cp build/libs/opentelemetry-examples-sdk-usage-0.1.0-SNAPSHOT-all.jar io.opentelemetry.sdk.example.ConfigureSpanProcessorExample
```
