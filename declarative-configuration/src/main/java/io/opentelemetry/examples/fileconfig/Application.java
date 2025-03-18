/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.examples.fileconfig;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.extension.incubator.fileconfig.DeclarativeConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/** Example code for setting up the SDK using file based configuration */
public final class Application {

  public static void main(String[] args) throws InterruptedException, IOException {
    // it is important to initialize your SDK as early as possible in your application's lifecycle
    InputStream is =
        Files.newInputStream(Paths.get(System.getenv("OTEL_EXPERIMENTAL_CONFIG_FILE")));
    OpenTelemetrySdk openTelemetrySdk = DeclarativeConfiguration.parseAndCreate(is);

    Tracer tracer = openTelemetrySdk.getTracer("io.opentelemetry.example");
    Meter meter = openTelemetrySdk.getMeter("io.opentelemetry.example");
    LongCounter counter = meter.counterBuilder("example_counter").build();
    // Filter out histogram with view defined in otel-sdk-config.yaml
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

    // shutdown to ensure data is flusehdd
    openTelemetrySdk.shutdown().join(10, TimeUnit.SECONDS);

    System.out.println("Bye");
  }
}
