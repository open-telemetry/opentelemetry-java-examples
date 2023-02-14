/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.example.otlp;

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
 * Example code for setting up the OTLP exporters.
 *
 * <p>If you wish to use this code, you'll need to run a copy of the collector locally, on the
 * default port. There is a docker-compose configuration for doing this in the docker subdirectory
 * of this module.
 */
public final class OtlpExporterExample {

  public static void main(String[] args) throws InterruptedException {
    // it is important to initialize your SDK as early as possible in your application's lifecycle
    OpenTelemetry openTelemetry = ExampleConfiguration.initOpenTelemetry();

    Tracer tracer = openTelemetry.getTracer("io.opentelemetry.example");
    Meter meter = openTelemetry.getMeter("io.opentelemetry.example");
    LongCounter counter = meter.counterBuilder("example_counter").build();
    LongHistogram histogram = meter.histogramBuilder("super_timer").ofLongs().setUnit("ms").build();

    for (int i = 0; i < 100; i++) {
      long startTime = System.currentTimeMillis();
      Span exampleSpan = tracer.spanBuilder("exampleSpan").startSpan();
      Context exampleContext = Context.current().with(exampleSpan);
      try (Scope scope = exampleContext.makeCurrent()) {
        counter.add(1);
        exampleSpan.setAttribute("good", true);
        exampleSpan.setAttribute("exampleNumber", i);
        Thread.sleep(100);
      } finally {
        histogram.record(
            System.currentTimeMillis() - startTime, Attributes.empty(), exampleContext);
        exampleSpan.end();
      }
    }

    // sleep for a bit to let everything settle
    Thread.sleep(2000);

    System.out.println("Bye");
  }
}
