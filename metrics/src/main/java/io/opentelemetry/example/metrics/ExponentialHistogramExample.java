package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.View;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import java.time.Duration;
import java.util.Random;

/**
 * Configures two histograms to use the base2ExponentialBucketHistogram aggregation, one that uses
 * default configurations, and another that sets a custom maxScale.
 */
public class ExponentialHistogramExample {
  private final OpenTelemetry otel;

  public ExponentialHistogramExample(OpenTelemetry otel) {
    this.otel = otel;
  }

  public static void main(String[] args) throws InterruptedException {
    SdkMeterProvider sdkMeterProvider =
        SdkMeterProvider.builder()
            .registerView(
                // Target histograms matching this name and apply a custom maxScale
                InstrumentSelector.builder().setName("*custom_scale*").build(),
                View.builder()
                    .setAggregation(Aggregation.base2ExponentialBucketHistogram(160, 4))
                    .build())
            .registerView(
                // Target this one histogram and use defaults (maxBuckets: 160 maxScale: 20)
                InstrumentSelector.builder().setName("job.duration").build(),
                View.builder()
                    .setAggregation(Aggregation.base2ExponentialBucketHistogram())
                    .build())
            .registerMetricReader(
                PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().build())
                    // Default is 60000ms (60 seconds). Set to 10 seconds for demonstrative purposes
                    // only.
                    .setInterval(Duration.ofSeconds(10))
                    .build())
            .build();

    OpenTelemetry sdk = OpenTelemetrySdk.builder().setMeterProvider(sdkMeterProvider).build();

    new ExponentialHistogramExample(sdk).run();
  }

  void run() throws InterruptedException {
    Meter meter = otel.getMeter("io.opentelemetry.example.metrics");

    LongHistogram histogram =
        meter
            .histogramBuilder("job.duration")
            .ofLongs()
            .setDescription("A distribution of job execution time")
            .setUnit("seconds")
            .build();

    LongHistogram customScaleHistogram =
        meter
            .histogramBuilder("job2.custom_scale.duration")
            .ofLongs()
            .setDescription("A distribution of job2's execution time using a custom scale value.")
            .setUnit("seconds")
            .build();

    Random rand = new Random();
    Attributes attrs = Attributes.of(stringKey("job"), "update_database");
    long metricPoint;

    while (true) {
      metricPoint = rand.nextLong(1000);
      histogram.record(metricPoint, attrs);
      customScaleHistogram.record(metricPoint, attrs);
      Thread.sleep(1000);
    }
  }
}
