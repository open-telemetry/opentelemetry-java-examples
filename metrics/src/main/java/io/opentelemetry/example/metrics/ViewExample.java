package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.View;
import java.util.Random;
import java.util.Set;

public final class ViewExample {

  public static final String INSTRUMENTATION_SCOPE = "io.opentelemetry.example.metrics";

  private final Meter meter;

  private ViewExample(Meter meter) {
    this.meter = meter;
  }

  public static void main(String[] args) {

    SdkMeterProvider sdkMeterProvider =
        SdkMeterProvider.builder()
            .registerView(
                // Filter out all attributes except `environment` for `customer_email_change`
                InstrumentSelector.builder().setName("customer_email_change").build(),
                View.builder().setAttributeFilter(Set.of("environment")).build())
            .registerView(
                // Rename metric
                InstrumentSelector.builder().setName("http_requests_active").build(),
                View.builder().setName("http.client.active_requests").build())
            .registerView(
                // Completely drop meters named "cpu.bad"
                InstrumentSelector.builder().setMeterName("cpu.bad").build(),
                View.builder().setAggregation(Aggregation.drop()).build())
            .build();

    OpenTelemetry sdk = OpenTelemetrySdk.builder().setMeterProvider(sdkMeterProvider).build();

    Meter meter = sdk.getMeter(INSTRUMENTATION_SCOPE);

    ViewExample example = new ViewExample(meter);
    example.run();
  }

  void run() {
    Random random = new Random();
    long min = 0;
    long max = 100;
    long randomLong = min + (long) (random.nextDouble() * (max - min + 1));

    // Metric for attribute filtering example
    LongCounter emailChangeCounter = meter.counterBuilder("customer_email_change").build();
    Attributes attributes =
        Attributes.of(
            stringKey("environment"), "production",
            stringKey("region"), "us-east-1",
            stringKey("service"), "dice-server");

    // Metric for renaming example
    LongHistogram httpRequests =
        meter
            .histogramBuilder("http_requests_active")
            .ofLongs()
            .setDescription("Active http requests.")
            .setUnit("requests")
            .build();

    // Metric that will be dropped
    LongUpDownCounter droppedMetric = meter.upDownCounterBuilder("cpu.bad").build();

    emailChangeCounter.add(1, attributes);
    httpRequests.record(randomLong, attributes);
    droppedMetric.add(1, attributes);
  }
}
