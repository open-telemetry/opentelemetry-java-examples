# OpenTelemetry with Logback

This sample app demonstrates an application emitting logs through a variety of logging APIs (OpenTelemetry API, log4j1, log4j2, JUL, slf4j) and configuring those logs to be sent an OTLP receiver via the OpenTelemetry SDK, and to the console via logback.

* Install and run a OTLP receiver listening at `localhost:4318`. [otel-tui](https://github.com/ymtdzzz/otel-tui) is a good choice for local development.

* Run the application:

```shell
../gradlew run

> Task :opentelemetry-examples-log-appender-logback:run
logback: 08:53:12.625 [main] INFO  log4j2-logger - A log4j2 log message.
logback: 08:53:12.633 [main] INFO  log4j1-logger - A log4j1 log message.
logback: 08:53:12.633 [main] INFO  slf4j-logger - A slf4j log message.
logback: 08:53:12.633 [main] INFO  jul-logger - A JUL log message.
logback: 08:53:12.634 [main] INFO  otel-logger - An OTEL log message.
```

* Or run the application with the javaagent:

```shell
../gradlew run -PrunWithAgent

> Task :opentelemetry-examples-log-appender-logback:run
logback: 08:47:33.317 [main] INFO  i.o.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.23.0-SNAPSHOT
logback: 08:47:33.337 [main] INFO  log4j2-logger - A log4j2 log message.
logback: 08:47:33.340 [main] INFO  log4j1-logger - A log4j1 log message.
logback: 08:47:33.340 [main] INFO  slf4j-logger - A slf4j log message.
logback: 08:47:33.340 [main] INFO  jul-logger - A JUL log message.
logback: 08:47:33.343 [main] INFO  otel-logger - An OTEL log message.
```

* A log message from each logging API appears exactly once in the console and OTLP receiver
