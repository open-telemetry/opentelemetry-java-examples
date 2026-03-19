package otel;

import io.prometheus.metrics.core.metrics.CounterWithCallback;

public class PrometheusCounterCallback {
  public static void counterCallbackUsage() {
    // Each zone has its own smart energy meter tracking cumulative joule totals.
    // Use a callback counter to report those values at scrape time without
    // maintaining separate counters in application code.
    CounterWithCallback.builder()
        .name("energy_consumed_joules_total")
        .help("Total energy consumed in joules")
        .labelNames("zone")
        .callback(
            callback -> {
              callback.call(SmartHomeDevices.totalEnergyJoules("upstairs"), "upstairs");
              callback.call(SmartHomeDevices.totalEnergyJoules("downstairs"), "downstairs");
            })
        .register();
  }
}
