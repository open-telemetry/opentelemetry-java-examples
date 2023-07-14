package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import java.util.Random;
import java.util.stream.IntStream;

public class HistogramExample {

  private final OpenTelemetry otel;

  public HistogramExample(OpenTelemetry otel) {
    this.otel = otel;
  }

  private void run() {
    Meter meter = otel.getMeter("io.opentelemetry.example.metrics");

    LongHistogram histogram =
        meter
            .histogramBuilder("people.ages")
            .setDescription("A distribution of people's ages")
            .setUnit("years")
            .ofLongs()
            .build();

    Random rand = new Random();
    Attributes attrs =
        Attributes.of(
            stringKey("decade"), "1990s",
            stringKey("source"), "someAlmanac");
    IntStream.range(0, 1000).forEach(x -> histogram.record(rand.nextLong(115), attrs));
  }

  public static void main(String[] args) {
    System.setProperty("otel.metric.export.interval", "10000");
    OpenTelemetry sdk = GlobalOpenTelemetry.get();
    new HistogramExample(sdk).run();
  }
}
