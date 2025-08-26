package otel;

import static otel.Util.WIDGET_COLOR;
import static otel.Util.WIDGET_SHAPE;
import static otel.Util.computeWidgetColor;
import static otel.Util.computeWidgetShape;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncGaugeUsage {
  private static final Attributes WIDGET_RED_CIRCLE = Util.WIDGET_RED_CIRCLE;

  public static void asyncGaugeUsage(Meter meter) {
    AtomicReference<Double> processingLineTemp = new AtomicReference<>(273.0);

    // Construct an async gauge to observe an existing gauge in a callback
    ObservableDoubleGauge asyncGauge =
        meter
            .gaugeBuilder("fully.qualified.gauge")
            .setDescription("The current temperature of the widget processing line")
            .setUnit("K")
            // Uncomment to optionally change the type to long
            // .ofLongs()
            .buildWithCallback(
                // the callback is invoked when a MetricReader reads metrics
                observableMeasurement -> {
                  double currentWidgetCount = processingLineTemp.get();

                  // Record a measurement with no attributes.
                  // Attributes defaults to Attributes.empty().
                  observableMeasurement.record(currentWidgetCount);

                  // Record a measurement with attributes, using pre-allocated attributes whenever
                  // possible.
                  observableMeasurement.record(currentWidgetCount, WIDGET_RED_CIRCLE);
                  // Sometimes, attributes must be computed using application context.
                  observableMeasurement.record(
                      currentWidgetCount,
                      Attributes.of(
                          WIDGET_SHAPE, computeWidgetShape(), WIDGET_COLOR, computeWidgetColor()));
                });

    // Optionally close the gauge to unregister the callback when required
    asyncGauge.close();
  }
}
