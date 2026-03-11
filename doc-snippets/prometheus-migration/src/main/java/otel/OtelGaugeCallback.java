package otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;

public class OtelGaugeCallback {
  private static final AttributeKey<String> ROOM = AttributeKey.stringKey("room");
  private static final Attributes LIVING_ROOM = Attributes.of(ROOM, "living_room");
  private static final Attributes BEDROOM = Attributes.of(ROOM, "bedroom");

  public static void gaugeCallbackUsage(OpenTelemetry openTelemetry) {
    Meter meter = openTelemetry.getMeter("smart.home");
    // Temperature sensors maintain their own readings in firmware.
    // Use an asynchronous gauge to report those values when a MetricReader
    // collects metrics, without maintaining separate gauges in application code.
    meter
        .gaugeBuilder("room.temperature")
        .setDescription("Current temperature in the room")
        .setUnit("Cel")
        .buildWithCallback(
            measurement -> {
              measurement.record(SmartHomeDevices.livingRoomTemperatureCelsius(), LIVING_ROOM);
              measurement.record(SmartHomeDevices.bedroomTemperatureCelsius(), BEDROOM);
            });
  }
}
