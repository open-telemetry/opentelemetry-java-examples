/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.example.prometheus;

import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricReader;

public final class ExampleConfiguration {

  /**
   * Initializes the Meter SDK and configures the prometheus collector with all default settings.
   *
   * @param prometheusPort the port to open up for scraping.
   * @return A MeterProvider for use in instrumentation.
   */
  static MeterProvider initializeOpenTelemetry(int prometheusPort) {
    MetricReader prometheusReader = PrometheusHttpServer.builder().setPort(prometheusPort).build();

    return SdkMeterProvider.builder().registerMetricReader(prometheusReader).build();
  }
}
