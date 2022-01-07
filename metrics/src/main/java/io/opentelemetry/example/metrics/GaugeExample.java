package io.opentelemetry.example.metrics;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;

/**
 * Example of using a Long Gauge to measure execution time of method. The gauge callback will get
 * executed every collection interval. This is useful for expensive measurements that would be
 * wastefully to calculate each request.
 */
public final class GaugeExample {

  public static void main(String[] args) {
    Meter sampleMeter = GlobalOpenTelemetry.getMeter("io.opentelemetry.example.metrics");

    sampleMeter
        .gaugeBuilder("jvm.memory.total")
        .setDescription("Reports JVM memory usage.")
        .setUnit("byte")
        .buildWithCallback(
            result -> result.record(Runtime.getRuntime().totalMemory(), Attributes.empty()));
  }
}
