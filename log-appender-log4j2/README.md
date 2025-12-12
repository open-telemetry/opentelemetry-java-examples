# OpenTelemetry with Log4j2

This sample app demonstrates an application emitting logs through a variety of logging APIs (OpenTelemetry API, log4j1, log4j2, JUL, slf4j) and configuring those logs to be sent an OTLP receiver via the OpenTelemetry SDK, and to the console via log4j2.

* Install and run a OTLP receiver listening at `localhost:4318`. [otel-tui](https://github.com/ymtdzzz/otel-tui) is a good choice for local development.

* Run the application:

```shell
../gradlew run

> Task :opentelemetry-examples-log-appender-log4j2:run
log4j2: 08:49:54.249 [main] INFO  log4j2-logger - A log4j2 log message.
log4j2: 08:49:54.263 [main] INFO  log4j1-logger - A log4j1 log message.
log4j2: 08:49:54.263 [main] INFO  slf4j-logger - A slf4j log message.
log4j2: 08:49:54.263 [main] INFO  jul-logger - A JUL log message.
log4j2: 08:49:54.265 [main] INFO  otel-logger - An OTEL log message.
```

* Or run the application with the javaagent:

```shell
../gradlew run -PrunWithAgent

> Task :opentelemetry-examples-log-appender-log4j2:run
[otel.javaagent 2025-12-08 08:54:48:330 -0600] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.23.0-SNAPSHOT
log4j2: 08:54:55.599 [main] INFO  log4j2-logger - A log4j2 log message.
log4j2: 08:54:55.625 [main] INFO  log4j1-logger - A log4j1 log message.
log4j2: 08:54:55.626 [main] INFO  slf4j-logger - A slf4j log message.
log4j2: 08:54:55.626 [main] INFO  jul-logger - A JUL log message.
log4j2: 08:54:55.629 [main] INFO  otel-logger - An OTEL log message.
```

* A log message from each logging API appears exactly once in the console and OTLP receiver
