/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.example.prometheus;

import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricReaderFactory;
import java.io.IOException;

public final class ExampleConfiguration {

  /**
   * Initializes the Meter SDK and configures the prometheus collector with all default settings.
   *
   * @param prometheusPort the port to open up for scraping.
   * @return A MeterProvider for use in instrumentation.
   */
  static MeterProvider initializeOpenTelemetry(int prometheusPort) throws IOException {
    MetricReaderFactory prometheusReaderFactory =
        PrometheusHttpServer.builder().setPort(prometheusPort).newMetricReaderFactory();

    return SdkMeterProvider.builder().registerMetricReader(prometheusReaderFactory).build();
  }
}
