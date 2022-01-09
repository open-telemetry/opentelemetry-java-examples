package io.opentelemetry.example.metrics;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.io.File;
import javax.swing.filechooser.FileSystemView;

/** Example of using {@link LongCounter} to count searched directories. */
public final class LongCounterExample {

  private static final Tracer tracer =
      GlobalOpenTelemetry.getTracer("io.opentelemetry.example.metrics", "0.13.1");

  private static final Meter sampleMeter =
      GlobalOpenTelemetry.getMeter("io.opentelemetry.example.metrics");
  private static final LongCounter directoryCounter =
      sampleMeter
          .counterBuilder("directories_search_count")
          .setDescription("Counts directories accessed while searching for files.")
          .setUnit("unit")
          .build();
  private static final File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();

  private static final AttributeKey<String> ROOT_DIRECTORY_KEY = stringKey("root directory");
  // statically allocate the attributes, since they are known at init time.
  private static final Attributes HOME_DIRECTORY_ATTRIBUTES =
      Attributes.of(ROOT_DIRECTORY_KEY, homeDirectory.getName());

  public static void main(String[] args) {
    Span span = tracer.spanBuilder("workflow").setSpanKind(SpanKind.INTERNAL).startSpan();
    LongCounterExample example = new LongCounterExample();
    try (Scope scope = span.makeCurrent()) {
      directoryCounter.add(1, HOME_DIRECTORY_ATTRIBUTES); // count root directory
      example.findFile("file_to_find.txt", homeDirectory);
    } catch (Exception e) {
      span.setStatus(StatusCode.ERROR, "Error while finding file");
    } finally {
      span.end();
    }
  }

  public void findFile(String name, File directory) {
    File[] files = directory.listFiles();
    System.out.println("Currently looking at " + directory.getAbsolutePath());
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          directoryCounter.add(1, HOME_DIRECTORY_ATTRIBUTES);
          findFile(name, file);
        } else if (name.equalsIgnoreCase(file.getName())) {
          System.out.println(file.getParentFile());
        }
      }
    }
  }
}
