# Kotlin Extension examples

This is a simple example that demonstrates how to use the Kotlin extension for attaching a Span context to a Kotlin
coroutine.

## Prerequisites

* Java 17 or higher is required to build this example (to run Gradle)
* Java 8 or higher is required to run the compiled example

## Compile

Compile with

```shell script
../gradlew shadowJar
```

## Run

The following commands are used to run the examples.

```shell script
java -cp build/libs/kotlin-extension-0.1.0-SNAPSHOT-all.jar io.opentelemetry.example.kotlinextension.Application
```
