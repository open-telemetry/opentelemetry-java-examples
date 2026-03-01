package otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.Meter;

public class OtelCounterCallback {
  public static void counterCallbackUsage(OpenTelemetry openTelemetry) {
    Meter meter = openTelemetry.getMeter("smart.home");
    // The smart energy meter maintains its own cumulative joule total in firmware.
    // Use an asynchronous counter to report that value when a MetricReader
    // collects metrics, without maintaining a separate counter in application code.
    meter
        .counterBuilder("energy.consumed")
        .setDescription("Total energy consumed")
        .setUnit("J")
        .ofDoubles()
        .buildWithCallback(measurement -> measurement.record(SmartHomeDevices.totalEnergyJoules()));
  }
}
