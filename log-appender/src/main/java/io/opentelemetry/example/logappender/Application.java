package io.opentelemetry.example.logappender;

import static io.opentelemetry.semconv.ServiceAttributes.SERVICE_NAME;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.MapMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Application {

  private static final org.apache.logging.log4j.Logger log4jLogger =
      LogManager.getLogger("log4j-logger");
  private static final org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("slf4j-logger");
  private static final java.util.logging.Logger julLogger = Logger.getLogger("jul-logger");

  public static void main(String[] args) {
    // Initialize OpenTelemetry as early as possible
    OpenTelemetry openTelemetry = initializeOpenTelemetry();
    // Install OpenTelemetry in log4j appender
    io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender.install(
        openTelemetry);
    // Install OpenTelemetry in logback appender
    io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender.install(
        openTelemetry);

    // Route JUL logs to slf4j
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    // Log using log4j API
    maybeRunWithSpan(() -> log4jLogger.info("A log4j log message without a span"), false);
    maybeRunWithSpan(() -> log4jLogger.info("A log4j log message with a span"), true);
    Map<String, Object> mapMessage = new HashMap<>();
    mapMessage.put("key", "value");
    mapMessage.put("message", "A log4j structured message");
    maybeRunWithSpan(() -> log4jLogger.info(new MapMessage<>(mapMessage)), false);
    ThreadContext.clearAll();
    maybeRunWithSpan(
        () -> log4jLogger.info("A log4j log message with an exception", new Exception("error!")),
        false);

    // Log using slf4j API w/ logback backend
    maybeRunWithSpan(() -> slf4jLogger.info("A slf4j log message without a span"), false);
    maybeRunWithSpan(() -> slf4jLogger.info("A slf4j log message with a span"), true);
    maybeRunWithSpan(
        () ->
            slf4jLogger
                .atInfo()
                .setMessage("A slf4j structured message")
                .addKeyValue("key", "value")
                .log(),
        false);
    maybeRunWithSpan(
        () -> slf4jLogger.info("A slf4j log message with an exception", new Exception("error!")),
        false);

    // Log using JUL API, bridged to slf4j, w/ logback backend
    maybeRunWithSpan(() -> julLogger.info("A JUL log message without a span"), false);
    maybeRunWithSpan(() -> julLogger.info("A JUL log message with a span"), true);
    maybeRunWithSpan(
        () ->
            julLogger.log(
                Level.INFO, "A JUL log message with an exception", new Exception("error!")),
        false);

    // Log using OpenTelemetry Log Bridge API
    // WARNING: This illustrates how to write appenders which bridge logs from
    // existing frameworks into the OpenTelemetry Log Bridge API. These APIs
    // SHOULD NOT be used by end users in place of existing log APIs (i.e. Log4j, Slf4, JUL).
    io.opentelemetry.api.logs.Logger customAppenderLogger =
        openTelemetry.getLogsBridge().get("custom-log-appender");
    maybeRunWithSpan(
        () ->
            customAppenderLogger
                .logRecordBuilder()
                .setSeverity(Severity.INFO)
                .setBody("A log message from a custom appender without a span")
                .setAttribute(AttributeKey.stringKey("key"), "value")
                .emit(),
        false);
    maybeRunWithSpan(
        () ->
            customAppenderLogger
                .logRecordBuilder()
                .setSeverity(Severity.INFO)
                .setBody("A log message from a custom appender with a span")
                .setAttribute(AttributeKey.stringKey("key"), "value")
                .emit(),
        true);
  }

  private static OpenTelemetry initializeOpenTelemetry() {
    OpenTelemetrySdk sdk =
        OpenTelemetrySdk.builder()
            .setTracerProvider(SdkTracerProvider.builder().setSampler(Sampler.alwaysOn()).build())
            .setLoggerProvider(
                SdkLoggerProvider.builder()
                    .setResource(
                        Resource.getDefault().toBuilder()
                            .put(SERVICE_NAME, "log4j-example")
                            .build())
                    .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(
                                OtlpGrpcLogRecordExporter.builder()
                                    .setEndpoint("http://localhost:4317")
                                    .build())
                            .build())
                    .build())
            .build();

    // Add hook to close SDK, which flushes logs
    Runtime.getRuntime().addShutdownHook(new Thread(sdk::close));

    return sdk;
  }

  private static void maybeRunWithSpan(Runnable runnable, boolean withSpan) {
    if (!withSpan) {
      runnable.run();
      return;
    }
    Span span = GlobalOpenTelemetry.getTracer("my-tracer").spanBuilder("my-span").startSpan();
    try (Scope unused = span.makeCurrent()) {
      runnable.run();
    } finally {
      span.end();
    }
  }
}
