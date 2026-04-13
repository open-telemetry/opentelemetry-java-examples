package otel;

import io.prometheus.metrics.core.metrics.Histogram;

public class PrometheusHistogramNative {
  public static void nativeHistogramUsage() {
    Histogram deviceCommandDuration =
        Histogram.builder()
            .name("device_command_duration_seconds")
            .help("Time to receive acknowledgment from a smart home device")
            .labelNames("device_type")
            .nativeOnly()
            .register();

    deviceCommandDuration.labelValues("thermostat").observe(0.35);
    deviceCommandDuration.labelValues("lock").observe(0.85);
  }
}
