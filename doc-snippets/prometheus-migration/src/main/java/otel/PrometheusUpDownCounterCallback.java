package otel;

import io.prometheus.metrics.core.metrics.GaugeWithCallback;

public class PrometheusUpDownCounterCallback {
  public static void upDownCounterCallbackUsage() {
    // The command queue maintains its own depth counter.
    // Use a callback gauge to report that value at scrape time.
    GaugeWithCallback.builder()
        .name("command_queue_depth")
        .help("Number of device commands waiting to be processed")
        .callback(callback -> callback.call(SmartHomeDevices.pendingCommandCount()))
        .register();
  }
}
