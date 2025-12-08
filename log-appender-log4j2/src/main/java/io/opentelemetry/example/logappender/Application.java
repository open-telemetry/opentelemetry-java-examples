package io.opentelemetry.example.logappender;

import static io.opentelemetry.semconv.ServiceAttributes.SERVICE_NAME;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.extension.incubator.slf4j.Slf4jBridgeProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Application {

  public static void main(String[] args) {
    // If javaagent isn't installed, initialize opentelemetry SDK and install it in the log4j2
    // OpenTelemetryAppender
    OpenTelemetry openTelemetry;
    if (!GlobalOpenTelemetry.isSet()) {
      openTelemetry = initializeOpenTelemetry();
      OpenTelemetryAppender.install(openTelemetry);
    } else {
      openTelemetry = GlobalOpenTelemetry.get();
    }

    // Install JUL to SLF4J bridging
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    // Initialize loggers and log from all APIs

    // log4j2
    org.apache.logging.log4j.Logger log4j2Logger = LogManager.getLogger("log4j2-logger");
    log4j2Logger.info("A log4j2 log message.");

    // log4j1
    org.apache.log4j.Logger log4j1Logger = org.apache.log4j.Logger.getLogger("log4j1-logger");
    log4j1Logger.info("A log4j1 log message.");

    // slf4j
    org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("slf4j-logger");
    slf4jLogger.info("A slf4j log message.");

    // jul
    java.util.logging.Logger julLogger = Logger.getLogger("jul-logger");
    julLogger.info("A JUL log message.");

    // otel
    io.opentelemetry.api.logs.Logger otelLogger = openTelemetry.getLogsBridge().get("otel-logger");
    otelLogger.logRecordBuilder().setSeverity(Severity.INFO).setBody("An OTEL log message.").emit();
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
                    .addLogRecordProcessor(Slf4jBridgeProcessor.create())
                    .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(OtlpHttpLogRecordExporter.builder().build())
                            .build())
                    .build())
            .build();

    // Add hook to close SDK, which flushes logs
    Runtime.getRuntime().addShutdownHook(new Thread(sdk::close));

    return sdk;
  }
}
