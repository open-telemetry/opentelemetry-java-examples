package otel;

import static otel.Util.WIDGET_COLOR;
import static otel.Util.WIDGET_RED_CIRCLE;
import static otel.Util.WIDGET_SHAPE;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.api.metrics.DoubleGauge;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;

public class NoopUsage {
  private static final String SCOPE_NAME = "fully.qualified.name";

  public static void noopUsage() {
    // Access the noop OpenTelemetry instance
    OpenTelemetry noopOpenTelemetry = OpenTelemetry.noop();

    // Noop tracing
    Tracer noopTracer = OpenTelemetry.noop().getTracer(SCOPE_NAME);
    noopTracer
        .spanBuilder("span name")
        .startSpan()
        .setAttribute(WIDGET_SHAPE, "square")
        .setStatus(StatusCode.OK)
        .addEvent("event-name", Attributes.builder().put(WIDGET_COLOR, "red").build())
        .end();

    // Noop metrics
    Attributes attributes = WIDGET_RED_CIRCLE;
    Meter noopMeter = OpenTelemetry.noop().getMeter(SCOPE_NAME);
    DoubleHistogram histogram = noopMeter.histogramBuilder("fully.qualified.histogram").build();
    histogram.record(1.0, attributes);
    // counter
    LongCounter counter = noopMeter.counterBuilder("fully.qualified.counter").build();
    counter.add(1, attributes);
    // async counter
    noopMeter
        .counterBuilder("fully.qualified.counter")
        .buildWithCallback(observable -> observable.record(10, attributes));
    // updowncounter
    LongUpDownCounter upDownCounter =
        noopMeter.upDownCounterBuilder("fully.qualified.updowncounter").build();
    // async updowncounter
    noopMeter
        .upDownCounterBuilder("fully.qualified.updowncounter")
        .buildWithCallback(observable -> observable.record(10, attributes));
    upDownCounter.add(-1, attributes);
    // gauge
    DoubleGauge gauge = noopMeter.gaugeBuilder("fully.qualified.gauge").build();
    gauge.set(1.1, attributes);
    // async gauge
    noopMeter
        .gaugeBuilder("fully.qualified.gauge")
        .buildWithCallback(observable -> observable.record(10, attributes));

    // Noop logs
    Logger noopLogger = OpenTelemetry.noop().getLogsBridge().get(SCOPE_NAME);
    noopLogger
        .logRecordBuilder()
        .setBody("log message")
        .setAttribute(WIDGET_SHAPE, "square")
        .setSeverity(Severity.INFO)
        .emit();
  }
}
