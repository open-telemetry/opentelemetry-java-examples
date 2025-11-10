package extension;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

/**
 * Custom SpanProcessor example from the documentation.
 * Uses System.out.println for debugging as documented.
 */
public class MySpanProcessor implements SpanProcessor {

    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {
        System.out.printf("Processing span: %s%n", span.getName());

        span.setAttribute("custom.processor", "MySpanProcessor");
        span.setAttribute("custom.timestamp", System.currentTimeMillis());
    }

    @Override
    public boolean isStartRequired() {
        return true;
    }

    @Override
    public void onEnd(ReadableSpan span) {
        System.out.printf("Span ended: %s (duration: %d ns)%n",
            span.getName(),
            span.getLatencyNanos());
    }

    @Override
    public boolean isEndRequired() {
        return true;
    }
}