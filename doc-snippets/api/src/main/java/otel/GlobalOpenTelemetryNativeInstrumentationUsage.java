package otel;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;

public class GlobalOpenTelemetryNativeInstrumentationUsage {

  public static void globalOpenTelemetryUsage(OpenTelemetry openTelemetry) {
    // Initialized with OpenTelemetry from java agent if present, otherwise no-op implementation.
    MyClient client1 = new MyClientBuilder().build();

    // Initialized with an explicit OpenTelemetry instance, overriding the java agent instance.
    MyClient client2 = new MyClientBuilder().setOpenTelemetry(openTelemetry).build();
  }

  /**
   * An example library with native OpenTelemetry instrumentation, initialized via {@link
   * MyClientBuilder}.
   */
  public static class MyClient {
    private final OpenTelemetry openTelemetry;

    private MyClient(OpenTelemetry openTelemetry) {
      this.openTelemetry = openTelemetry;
    }

    // ... library methods omitted
  }

  /** Builder for {@link MyClient}. */
  public static class MyClientBuilder {
    // OpenTelemetry defaults to the GlobalOpenTelemetry instance if set, e.g. by the java agent or
    // by the application, else to a no-op implementation.
    private OpenTelemetry openTelemetry = GlobalOpenTelemetry.getOrNoop();

    /** Explicitly set the OpenTelemetry instance to use. */
    public MyClientBuilder setOpenTelemetry(OpenTelemetry openTelemetry) {
      this.openTelemetry = openTelemetry;
      return this;
    }

    /** Build the client. */
    public MyClient build() {
      return new MyClient(openTelemetry);
    }
  }
}
