package otel;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;

public class GlobalOpenTelemetryUsage {

  public static void openTelemetryUsage(OpenTelemetry openTelemetry) {
    // Set the GlobalOpenTelemetry instance as early in the application lifecycle as possible
    // Set must only be called once. Calling multiple times raises an exception.
    GlobalOpenTelemetry.set(openTelemetry);

    // Get the GlobalOpenTelemetry instance.
    openTelemetry = GlobalOpenTelemetry.get();
  }
}
