package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.io.File;
import javax.swing.filechooser.FileSystemView;

/** Example of using {@link LongCounter} to count searched directories. */
public final class LongCounterExample {

  public static final String INSTRUMENTATION_SCOPE = "io.opentelemetry.example.metrics";
  private static final File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();

  private static final AttributeKey<String> ROOT_DIRECTORY_KEY = stringKey("root directory");
  // statically allocate the attributes, since they are known at init time.
  private static final Attributes HOME_DIRECTORY_ATTRIBUTES =
      Attributes.of(ROOT_DIRECTORY_KEY, homeDirectory.getName());
  private final Meter meter;
  private final Tracer tracer;

  public LongCounterExample(Tracer tracer, Meter meter) {
    this.tracer = tracer;
    this.meter = meter;
  }

  public static void main(String[] args) {
    OpenTelemetry sdk = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    Tracer tracer = sdk.getTracer(INSTRUMENTATION_SCOPE, "0.13.1");
    Meter meter = sdk.getMeter(INSTRUMENTATION_SCOPE);
    LongCounterExample example = new LongCounterExample(tracer, meter);
    example.run();
  }

  void run() {
    Span span = tracer.spanBuilder("workflow").setSpanKind(SpanKind.INTERNAL).startSpan();
    try (Scope scope = span.makeCurrent()) {
      LongCounter directoryCounter = findFile("file_to_find.txt", homeDirectory);
      directoryCounter.add(1, HOME_DIRECTORY_ATTRIBUTES); // count root directory
    } catch (Exception e) {
      span.setStatus(StatusCode.ERROR, "Error while finding file");
    } finally {
      span.end();
    }
  }

  /**
   * Uses the Meter instance to create a LongCounter with the given name, description, and units.
   * This is the counter that is used to count directories during filesystem traversal.
   */
  LongCounter createCounter() {
    return meter
        .counterBuilder("directories_search_count")
        .setDescription("Counts directories accessed while searching for files.")
        .setUnit("unit")
        .build();
  }

  LongCounter findFile(String name, File directory) {
    LongCounter directoryCounter = createCounter();
    File[] files = directory.listFiles();
    System.out.println("Currently looking at " + directory.getAbsolutePath());
    if (files == null) {
      return directoryCounter;
    }
    for (File file : files) {
      if (file.isDirectory()) {
        directoryCounter.add(1, HOME_DIRECTORY_ATTRIBUTES);
        findFile(name, file);
      } else if (name.equalsIgnoreCase(file.getName())) {
        System.out.println(file.getParentFile());
      }
    }
    return directoryCounter;
  }
}
