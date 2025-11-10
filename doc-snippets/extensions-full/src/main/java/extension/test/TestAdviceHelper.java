package extension.test;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;

/**
 * Helper class for debugging
 */
public class TestAdviceHelper {
  public static void logEntry() {
    System.out.println("=== TestAdviceHelper: logEntry called (breakpoint works here) ===");
    Span span = Java8BytecodeBridge.currentSpan();
    span.setAttribute("test.helper", "called");
  }
}