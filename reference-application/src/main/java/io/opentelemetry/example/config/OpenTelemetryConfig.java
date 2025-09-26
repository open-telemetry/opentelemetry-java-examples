package io.opentelemetry.example.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.ServiceAttributes;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

  @Bean
  public OpenTelemetry openTelemetry() {
    // Create resource
    Resource resource =
        Resource.getDefault()
            .merge(
                Resource.create(
                    Attributes.of(
                        ServiceAttributes.SERVICE_NAME, "dice-server",
                        ServiceAttributes.SERVICE_VERSION, "1.0.0")));

    // Configure span exporter based on environment
    SpanExporter spanExporter = createSpanExporter();

    // Configure tracer provider
    SdkTracerProvider tracerProvider =
        SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
            .setResource(resource)
            .build();

    // Configure metric exporter
    MetricExporter metricExporter = createMetricExporter();

    // Configure meter provider
    SdkMeterProvider meterProvider =
        SdkMeterProvider.builder()
            .registerMetricReader(
                PeriodicMetricReader.builder(metricExporter)
                    .setInterval(Duration.ofSeconds(30))
                    .build())
            .setResource(resource)
            .build();

    // Configure log exporter
    LogRecordExporter logExporter = createLogExporter();

    // Configure logger provider
    SdkLoggerProvider loggerProvider =
        SdkLoggerProvider.builder()
            .addLogRecordProcessor(BatchLogRecordProcessor.builder(logExporter).build())
            .setResource(resource)
            .build();

    // Build and register global OpenTelemetry
    return OpenTelemetrySdk.builder()
        .setTracerProvider(tracerProvider)
        .setMeterProvider(meterProvider)
        .setLoggerProvider(loggerProvider)
        .buildAndRegisterGlobal();
  }

  private SpanExporter createSpanExporter() {
    String otlpEndpoint = System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
    String traceExporter = System.getenv("OTEL_TRACES_EXPORTER");

    if ("otlp".equals(traceExporter) && otlpEndpoint != null) {
      return OtlpHttpSpanExporter.builder().setEndpoint(otlpEndpoint + "/v1/traces").build();
    } else {
      return LoggingSpanExporter.create();
    }
  }

  private MetricExporter createMetricExporter() {
    String otlpEndpoint = System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
    String metricExporter = System.getenv("OTEL_METRICS_EXPORTER");

    if ("otlp".equals(metricExporter) && otlpEndpoint != null) {
      return OtlpHttpMetricExporter.builder().setEndpoint(otlpEndpoint + "/v1/metrics").build();
    } else {
      return LoggingMetricExporter.create();
    }
  }

  private LogRecordExporter createLogExporter() {
    String otlpEndpoint = System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
    String logExporter = System.getenv("OTEL_LOGS_EXPORTER");

    if ("otlp".equals(logExporter) && otlpEndpoint != null) {
      return OtlpHttpLogRecordExporter.builder().setEndpoint(otlpEndpoint + "/v1/logs").build();
    } else {
      // For console logging, return a no-op exporter since we handle logs via logback
      return LogRecordExporter.composite();
    }
  }
}
