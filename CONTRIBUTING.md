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

## Linting

This repository uses [flint](https://github.com/grafana/flint) managed by [mise](https://github.com/jdx/mise) to run the configured lint checks, including Markdown and link validation.

To run all lint checks:

```bash
mise run lint
```

(note: Windows users may need to run `mise install` first)

To automatically fix fixable issues:

```bash
mise run lint:fix
```

## Style guide

This repository follows the OpenTelemetry Java
repository's [patterns](https://github.com/open-telemetry/opentelemetry-java/blob/main/docs/knowledge/general-patterns.md).

## Gradle conventions

- Use kotlin instead of groovy
- Plugin versions should be specified in `settings.gradle.kts`, not in individual modules
- All modules use `plugins { id("otel.java-conventions") }`
