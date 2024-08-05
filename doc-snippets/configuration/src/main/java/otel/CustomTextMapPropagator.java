package otel;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import java.util.Collection;
import java.util.Collections;

public class CustomTextMapPropagator implements TextMapPropagator {

  @Override
  public Collection<String> fields() {
    // Return fields used for propagation. See W3CTraceContextPropagator for reference
    // implementation.
    return Collections.emptyList();
  }

  @Override
  public <C> void inject(Context context, C carrier, TextMapSetter<C> setter) {
    // Inject context. See W3CTraceContextPropagator for reference implementation.
  }

  @Override
  public <C> Context extract(Context context, C carrier, TextMapGetter<C> getter) {
    // Extract context. See W3CTraceContextPropagator for reference implementation.
    return context;
  }
}
