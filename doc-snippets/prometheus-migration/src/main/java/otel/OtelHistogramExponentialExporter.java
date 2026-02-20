package otel;

import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentType;

public class OtelHistogramExponentialExporter {
  static OtlpHttpMetricExporter createExporter() {
    // Configure the exporter to use exponential histograms for all histogram instruments.
    // This is the preferred approach — it applies globally without modifying instrumentation code.
    return OtlpHttpMetricExporter.builder()
        .setEndpoint("http://localhost:4318")
        .setDefaultAggregationSelector(
            type ->
                type == InstrumentType.HISTOGRAM
                    ? Aggregation.base2ExponentialBucketHistogram()
                    : Aggregation.defaultAggregation())
        .build();
  }
}
