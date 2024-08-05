package otel;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogRecordExporter implements LogRecordExporter {

  private static final Logger logger = Logger.getLogger(CustomLogRecordExporter.class.getName());

  @Override
  public CompletableResultCode export(Collection<LogRecordData> logs) {
    // Export the records. Typically, records are sent out of process via some network protocol, but
    // we simply log for illustrative purposes.
    System.out.println("Exporting logs");
    logs.forEach(log -> System.out.println("log record: " + log));
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode flush() {
    // Export any records which have been queued up but not yet exported.
    logger.log(Level.INFO, "flushing");
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode shutdown() {
    // Shutdown the exporter and cleanup any resources.
    logger.log(Level.INFO, "shutting down");
    return CompletableResultCode.ofSuccess();
  }
}
