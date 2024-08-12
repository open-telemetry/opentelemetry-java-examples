package otel;

import io.opentelemetry.sdk.logs.LogLimits;

public class LogLimitsConfig {
  public static LogLimits logLimits() {
    return LogLimits.builder()
        .setMaxNumberOfAttributes(128)
        .setMaxAttributeValueLength(1024)
        .build();
  }
}
