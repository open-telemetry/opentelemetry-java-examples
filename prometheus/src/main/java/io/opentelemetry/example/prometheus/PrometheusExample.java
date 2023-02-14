package io.opentelemetry.example.prometheus;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

/**
 * Example of using the PrometheusHttpServer to convert OTel metrics to Prometheus format and expose
 * these to a Prometheus instance via a HttpServer exporter.
 *
 * <p>A Gauge is used to periodically measure how many incoming messages are awaiting processing.
 * The Gauge callback gets executed every collection interval.
 */
public final class PrometheusExample {
  public static void main(String[] args) throws InterruptedException {
    int prometheusPort = 0;
    try {
      prometheusPort = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Port not set, or is invalid. Exiting");
      System.exit(1);
    }
    // it is important to initialize your SDK as early as possible in your application's lifecycle
    OpenTelemetry openTelemetry = ExampleConfiguration.initOpenTelemetry(prometheusPort);

    Tracer tracer = openTelemetry.getTracer("io.opentelemetry.example.prometheus");
    Meter meter = openTelemetry.getMeter("io.opentelemetry.example.prometheus");
    LongCounter counter = meter.counterBuilder("example.counter").build();
    LongHistogram histogram = meter.histogramBuilder("super.timer").ofLongs().setUnit("ms").build();

    for (int i = 0; i < 500; i++) {
      long startTime = System.currentTimeMillis();
      Span exampleSpan = tracer.spanBuilder("exampleSpan").startSpan();
      Context exampleContext = Context.current().with(exampleSpan);
      try (Scope scope = exampleContext.makeCurrent()) {
        counter.add(1);
        exampleSpan.setAttribute("good", true);
        exampleSpan.setAttribute("exampleNumber", i);
        Thread.sleep(1000);
      } finally {
        histogram.record(
            System.currentTimeMillis() - startTime, Attributes.empty(), exampleContext);
        exampleSpan.end();
      }
    }

    System.out.println("Exiting");
  }
}
