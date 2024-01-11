package io.opentelemetry.example.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.extension.incubator.trace.ExtendedTracer;

/**
 * An example of using {@link io.opentelemetry.exporter.logging.LoggingSpanExporter} and {@link
 * io.opentelemetry.exporter.logging.LoggingMetricExporter}.
 */
public final class ManualTracingExample {
  private static final String INSTRUMENTATION_NAME = ManualTracingExample.class.getName();

  private final ExtendedTracer tracer;

  public ManualTracingExample(OpenTelemetry openTelemetry) {
    tracer = ExtendedTracer.create(openTelemetry.getTracer(INSTRUMENTATION_NAME));
  }

  public void myWonderfulUseCase() {
    tracer.spanBuilder("calculate LLVM").startAndCall(this::calculateLlvm);
  }

  private String calculateLlvm() {
      Span.current().setAttribute("type", "smarter");
    //noinspection DataFlowIssue
    Span.current().setAttribute("foo", Baggage.current().getEntryValue("foo"));
    return "OK";
  }

  public void setBaggage() {
    try {
      Baggage.current().toBuilder()
          .put("foo", "bar")
          .build()
          .storeInContext(Context.current())
          .wrap(() -> tracer.spanBuilder("span with baggage").startAndCall(this::calculateLlvm))
          .call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    // it is important to initialize your SDK as early as possible in your application's lifecycle
    OpenTelemetry oTel = ExampleConfiguration.initOpenTelemetry();

    // Start the example
    ManualTracingExample example = new ManualTracingExample(oTel);
    // Generate a few sample spans
    for (int i = 0; i < 5; i++) {
      example.myWonderfulUseCase();
    }

    example.setBaggage();
  }
}
