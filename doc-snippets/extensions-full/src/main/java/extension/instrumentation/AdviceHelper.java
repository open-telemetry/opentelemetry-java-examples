package extension.instrumentation;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;

/**
 * Helper class for debugging - breakpoints WILL work here.
 * This validates the documentation's claim about debugging.
 */
public class AdviceHelper {

    public static void processHandler(Object handler) {
        System.out.println("=== AdviceHelper.processHandler called ===");
        System.out.println("Handler class: " + handler.getClass().getName());

        System.out.println("You can debug this method with breakpoints!");

        Span span = Java8BytecodeBridge.currentSpan();
        span.setAttribute("custom.helper.called", true);
    }
}