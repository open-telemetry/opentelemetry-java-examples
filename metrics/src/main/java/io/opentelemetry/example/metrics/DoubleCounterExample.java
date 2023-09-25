package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 * Example of using {@link DoubleCounter} to count disk space used by files with specific
 * extensions.
 */
public final class DoubleCounterExample {

  public static final List<String> FILE_EXTENSIONS = Arrays.asList("dll", "png", "exe");
  private static final AttributeKey<String> FILE_EXTENSION_KEY = stringKey("file_extension");
  public static final String INSTRUMENTATION_SCOPE = "io.opentelemetry.example.metrics";
  private final File directoryToCountIn = FileSystemView.getFileSystemView().getHomeDirectory();
  private final Tracer tracer;
  private final Meter meter;

  public static void main(String[] args) {
    OpenTelemetry sdk = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    Tracer tracer = sdk.getTracer(INSTRUMENTATION_SCOPE);
    Meter meter = sdk.getMeter(INSTRUMENTATION_SCOPE);
    DoubleCounterExample example = new DoubleCounterExample(tracer, meter);
    example.run();
  }

  public DoubleCounterExample(Tracer tracer, Meter meter) {
    this.tracer = tracer;
    this.meter = meter;
  }

  void run() {
    Span span = tracer.spanBuilder("calculate space").setSpanKind(SpanKind.INTERNAL).startSpan();
    try (Scope scope = span.makeCurrent()) {
      calculateSpaceUsedByFilesWithExtension(directoryToCountIn);
    } catch (Exception e) {
      span.setStatus(StatusCode.ERROR, "Error while calculating used space");
    } finally {
      span.end();
    }
  }

  /**
   * Uses the Meter instance to create a DoubleCounter with the specified name, description, and
   * units (megabytes). This is the meter that will accumulate the number of megabytes used by files
   * in the filesystem.
   */
  DoubleCounter createCounter() {
    return meter
        .counterBuilder("calculated_used_space")
        .setDescription("Counts disk space used by file extension.")
        .setUnit("MB")
        .ofDoubles()
        .build();
  }

  void calculateSpaceUsedByFilesWithExtension(File directory) {
    DoubleCounter diskSpaceCounter = createCounter();
    File[] files = directory.listFiles();
    if (files == null) {
      return;
    }
    for (File file : files) {
      for (String extension : FILE_EXTENSIONS) {
        if (file.getName().endsWith("." + extension)) {
          // we can add values to the counter for specific labels
          // the label key is "file_extension", its value is the name of the extension
          double fileSize = (double) file.length() / 1_000_000;
          diskSpaceCounter.add(fileSize, Attributes.of(FILE_EXTENSION_KEY, extension));
        }
      }
    }
  }
}
