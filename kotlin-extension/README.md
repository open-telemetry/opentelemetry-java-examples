# Kotlin Extension Examples

This is a simple example that demonstrates how to use the Kotlin extension for attaching a span context to a Kotlin
coroutine.

## Prerequisites

* Java 17 or higher is required to run Gradle and build this example
* Java 8 or higher may be used to run the example once it has been built

## Compile

Compile with:

```shell script
../gradlew shadowJar
```

## Run

Use the following command to run the example:

```shell script
java -cp build/libs/kotlin-extension-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.kotlinextension.Application
```
