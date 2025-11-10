package extension.test;

import com.google.auto.service.AutoService;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigurablePropagatorProvider;

import java.util.Collection;
import java.util.Collections;


@AutoService(ConfigurablePropagatorProvider.class)
public class TestPropagatorProvider implements ConfigurablePropagatorProvider {

  @Override
  public TextMapPropagator getPropagator(ConfigProperties config) {
    System.out.println("=== TestPropagatorProvider: getPropagator called ===");
    return new TestCustomPropagator();
  }

  @Override
  public String getName() {
    // Use with: -Dotel.propagators=test-propagator
    return "test-propagator";
  }

  private static class TestCustomPropagator implements TextMapPropagator {
    @Override
    public Collection<String> fields() {
      return Collections.singletonList("test-trace-id");
    }

    @Override
    public <C> void inject(Context context, C carrier, TextMapSetter<C> setter) {
      System.out.println("=== TestCustomPropagator: inject called ===");
      // Simple test implementation
      setter.set(carrier, "test-trace-id", "test-value");
    }

    @Override
    public <C> Context extract(Context context, C carrier, TextMapGetter<C> getter) {
      System.out.println("=== TestCustomPropagator: extract called ===");
      return context;
    }
  }
}