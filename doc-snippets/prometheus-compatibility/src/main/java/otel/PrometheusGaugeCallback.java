package otel;

import io.prometheus.metrics.core.metrics.GaugeWithCallback;

public class PrometheusGaugeCallback {
  public static void gaugeCallbackUsage() {
    // Temperature sensors maintain their own readings in firmware.
    // Use a callback gauge to report those values at scrape time without
    // maintaining a separate gauge in application code.
    GaugeWithCallback.builder()
        .name("room_temperature_celsius")
        .help("Current temperature in the room")
        .labelNames("room")
        .callback(
            callback -> {
              callback.call(SmartHomeDevices.livingRoomTemperatureCelsius(), "living_room");
              callback.call(SmartHomeDevices.bedroomTemperatureCelsius(), "bedroom");
            })
        .register();
  }
}
