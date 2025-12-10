package otel;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

public class GlobalOpenTelemetryManualInstrumentationUsage {

  public static void globalOpenTelemetryUsage() {
    // If GlobalOpenTelemetry is already set, e.g. by the java agent, use it.
    // Else, initialize an OpenTelemetry SDK instance and use it.
    OpenTelemetry openTelemetry =
        GlobalOpenTelemetry.isSet() ? GlobalOpenTelemetry.get() : initializeOpenTelemetry();

    // Install into manual instrumentation. This may involve setting as a singleton in the
    // application's dependency injection framework.
  }

  /** Initialize OpenTelemetry SDK using autoconfiguration. */
  public static OpenTelemetry initializeOpenTelemetry() {
    return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
  }
}
