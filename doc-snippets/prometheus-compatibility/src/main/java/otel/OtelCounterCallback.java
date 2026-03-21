package otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;

public class OtelCounterCallback {
  private static final AttributeKey<String> ZONE = AttributeKey.stringKey("zone");
  private static final Attributes UPSTAIRS = Attributes.of(ZONE, "upstairs");
  private static final Attributes DOWNSTAIRS = Attributes.of(ZONE, "downstairs");

  public static void counterCallbackUsage(OpenTelemetry openTelemetry) {
    Meter meter = openTelemetry.getMeter("smart.home");
    // Each zone has its own smart energy meter tracking cumulative joule totals.
    // Use an asynchronous counter to report those values when a MetricReader
    // collects metrics, without maintaining separate counters in application code.
    meter
        .counterBuilder("energy.consumed")
        .setDescription("Total energy consumed")
        .setUnit("J")
        .ofDoubles()
        .buildWithCallback(
            measurement -> {
              measurement.record(SmartHomeDevices.totalEnergyJoules("upstairs"), UPSTAIRS);
              measurement.record(SmartHomeDevices.totalEnergyJoules("downstairs"), DOWNSTAIRS);
            });
  }
}
