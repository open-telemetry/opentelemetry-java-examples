package otel;

import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.View;
import java.util.List;

public class OtelHistogramExplicitBucketView {
  static SdkMeterProvider createMeterProvider() {
    // Override the bucket boundaries advised on the instrument for a specific histogram.
    return SdkMeterProvider.builder()
        .registerView(
            InstrumentSelector.builder().setName("device.command.duration").build(),
            View.builder()
                .setAggregation(
                    Aggregation.explicitBucketHistogram(List.of(0.1, 0.25, 0.5, 1.0, 2.5, 5.0)))
                .build())
        .build();
  }
}
