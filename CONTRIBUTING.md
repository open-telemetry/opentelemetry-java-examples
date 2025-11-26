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

## Markdown linting

This repository uses [markdownlint](https://github.com/DavidAnson/markdownlint) via `markdownlint-cli2` managed by [mise](https://github.com/jdx/mise).

To check all Markdown files:

```bash
mise run lint:markdown
```

(note: Windows users may need to run `mise install` first)

To automatically fix fixable issues:

```bash
mise run lint:markdown --fix
```

## Style guide

This repository follows the OpenTelemetry Java
repository's [style guide](https://github.com/open-telemetry/opentelemetry-java/blob/main/CONTRIBUTING.md#style-guideline).

## Gradle conventions

- Use kotlin instead of groovy
- Plugin versions should be specified in `settings.gradle.kts`, not in individual modules
- All modules use `plugins { id("otel.java-conventions") }`
