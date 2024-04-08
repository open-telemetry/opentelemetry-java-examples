package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.incubator.metrics.ExtendedLongHistogramBuilder;
import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class HistogramExample {

  private final OpenTelemetry otel;

  public static void main(String[] args) {
    // The default export interval is 30s, but can be customized to something shorter.
    // Typically, this would be done with an external property or env var.
    System.setProperty("otel.metric.export.interval", "10000");
    OpenTelemetry sdk = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    new HistogramExample(sdk).run();
  }

  public HistogramExample(OpenTelemetry otel) {
    this.otel = otel;
  }

  void run() {
    Meter meter = otel.getMeter("io.opentelemetry.example.metrics");
    exampleWithDefaultBuckets(meter);
    exampleWithCustomBuckets(meter);
  }

  /**
   * This example shows how to create a LongHistogram with a description and a unit. It then calls a
   * method to populate the histogram with some randomly generated data. This method uses the
   * default bucket boundaries <a
   * href="https://opentelemetry.io/docs/specs/otel/metrics/sdk/#explicit-bucket-histogram-aggregation">documented
   * here</a>
   */
  void exampleWithDefaultBuckets(Meter meter) {
    LongHistogram histogram =
        meter
            .histogramBuilder("people.ages")
            .ofLongs() // Required to get a LongHistogram, default is DoubleHistogram
            .setDescription("A distribution of people's ages")
            .setUnit("years")
            .build();
    addDataToHistogram(histogram);
  }

  /**
   * This example is just like the above, except that instead of using the default bucket
   * boundaries, we pass a list of custom bucket boundaries.
   */
  void exampleWithCustomBuckets(Meter meter) {
    DoubleHistogramBuilder originalBuilder = meter.histogramBuilder("people.ages");
    ExtendedLongHistogramBuilder builder = (ExtendedLongHistogramBuilder) originalBuilder.ofLongs();
    List<Long> bucketBoundaries = Arrays.asList(0L, 5L, 12L, 18L, 24L, 40L, 50L, 80L, 115L);
    LongHistogram histogram =
        builder
            .setExplicitBucketBoundariesAdvice(bucketBoundaries)
            .setDescription("A distribution of people's ages")
            .setUnit("years")
            .build();
    addDataToHistogram(histogram);
  }

  void addDataToHistogram(LongHistogram histogram) {
    Random rand = new Random();
    Attributes attrs =
        Attributes.of(
            stringKey("decade"), "1990s",
            stringKey("source"), "someAlmanac");
    IntStream.range(0, 1000).forEach(x -> histogram.record(rand.nextLong(115), attrs));
  }
}
