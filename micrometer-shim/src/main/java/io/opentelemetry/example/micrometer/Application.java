package io.opentelemetry.example.micrometer;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.instrumentation.micrometer.v1_5.OpenTelemetryMeterRegistry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // Configure OpenTelemetry bean, registering prometheus metric reader
  @Bean
  public OpenTelemetry openTelemetry() {
    return OpenTelemetrySdk.builder()
        .setMeterProvider(
            SdkMeterProvider.builder().registerMetricReader(PrometheusHttpServer.create()).build())
        .build();
  }

  // Enable @Timed annotation
  @Bean
  public TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }

  // Configure OpenTelemetryMeterRegistry bean, overriding default autoconfigured MeterRegistry bean
  @Bean
  public MeterRegistry meterRegistry(OpenTelemetry openTelemetry) {
    return OpenTelemetryMeterRegistry.builder(openTelemetry)
        // Simulate behavior of micrometer's PrometheusMeterRegistry
        .setPrometheusMode(true)
        .build();
  }
}
