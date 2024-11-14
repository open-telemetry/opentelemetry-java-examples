package otel;

import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.View;
import java.util.List;
import java.util.Set;

public class ViewConfig {
  public static SdkMeterProviderBuilder dropMetricView(
      SdkMeterProviderBuilder builder, String metricName) {
    return builder.registerView(
        InstrumentSelector.builder().setName(metricName).build(),
        View.builder().setAggregation(Aggregation.drop()).build());
  }

  public static SdkMeterProviderBuilder histogramBucketBoundariesView(
      SdkMeterProviderBuilder builder, String metricName, List<Double> bucketBoundaries) {
    return builder.registerView(
        InstrumentSelector.builder().setName(metricName).build(),
        View.builder()
            .setAggregation(Aggregation.explicitBucketHistogram(bucketBoundaries))
            .build());
  }

  public static SdkMeterProviderBuilder attributeFilterView(
      SdkMeterProviderBuilder builder, String metricName, Set<String> keysToRetain) {
    return builder.registerView(
        InstrumentSelector.builder().setName(metricName).build(),
        View.builder().setAttributeFilter(keysToRetain).build());
  }

  public static SdkMeterProviderBuilder cardinalityLimitsView(
      SdkMeterProviderBuilder builder, String metricName, int cardinalityLimit) {
    return builder.registerView(
        InstrumentSelector.builder().setName(metricName).build(),
        View.builder().setCardinalityLimit(cardinalityLimit).build());
  }
}
