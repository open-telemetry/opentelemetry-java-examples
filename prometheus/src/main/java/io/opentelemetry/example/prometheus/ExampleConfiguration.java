/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.example.prometheus;

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

public final class ExampleConfiguration {

  /**
   * Initializes the OpenTelemetry SDK and configures the prometheus collector with all default
   * settings.
   *
   * @param prometheusPort the port to open up for scraping.
   * @return a ready-to-use {@link OpenTelemetry} instance.
   */
  static OpenTelemetry initOpenTelemetry(int prometheusPort) {
    // Include required service.name resource attribute on all spans and metrics
    Resource resource =
        Resource.getDefault()
            .merge(Resource.builder().put(SERVICE_NAME, "PrometheusExporterExample").build());

    OpenTelemetrySdk openTelemetrySdk =
        OpenTelemetrySdk.builder()
            .setTracerProvider(
                SdkTracerProvider.builder()
                    .setResource(resource)
                    .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
                    .build())
            .setMeterProvider(
                SdkMeterProvider.builder()
                    .setResource(resource)
                    .registerMetricReader(
                        PrometheusHttpServer.builder().setPort(prometheusPort).build())
                    .build())
            .buildAndRegisterGlobal();

    Runtime.getRuntime().addShutdownHook(new Thread(openTelemetrySdk::close));

    return openTelemetrySdk;
  }
}
