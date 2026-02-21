package otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.Meter;

public class OtelUpDownCounterCallback {
  public static void upDownCounterCallbackUsage(OpenTelemetry openTelemetry) {
    Meter meter = openTelemetry.getMeter("smart.home");
    // The command queue maintains its own depth counter.
    // Use an asynchronous up-down counter to report that value when a MetricReader
    // collects metrics.
    meter
        .upDownCounterBuilder("command.queue.depth")
        .setDescription("Number of device commands waiting to be processed")
        .buildWithCallback(
            measurement -> measurement.record(SmartHomeDevices.pendingCommandCount()));
  }
}
