package otel;

import io.prometheus.metrics.core.metrics.Gauge;

public class PrometheusUpDownCounter {
  public static void upDownCounterUsage() {
    // Prometheus uses Gauge for values that can increase or decrease.
    Gauge devicesConnected =
        Gauge.builder()
            .name("devices_connected")
            .help("Number of smart home devices currently connected")
            .labelNames("device_type")
            .register();

    // Increment when a device connects, decrement when it disconnects.
    devicesConnected.labelValues("thermostat").inc();
    devicesConnected.labelValues("thermostat").inc();
    devicesConnected.labelValues("lock").inc();
    devicesConnected.labelValues("lock").dec();
  }
}
