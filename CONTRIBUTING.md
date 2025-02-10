# Contributing

Welcome to the OpenTelemetry Java Examples repository!

## Building

Java 17 or higher is required to build the projects in this repository.
To check your Java version, run:

```bash
java -version
```

To build the project, run:

```bash
./gradlew assemble
```

## Style guide

This repository follows the OpenTelemetry Java
repository's [style guide](https://github.com/open-telemetry/opentelemetry-java/blob/main/CONTRIBUTING.md#style-guideline).

## Gradle conventions

- Use kotlin instead of groovy
- Plugin versions should be specified in `settings.gradle.kts`, not in individual modules
- All modules use `plugins { id("otel.java-conventions") }`
