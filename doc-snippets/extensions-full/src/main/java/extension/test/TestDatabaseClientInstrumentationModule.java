package extension.test;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

import java.util.Collections;
import java.util.List;


@AutoService(InstrumentationModule.class)
public class TestDatabaseClientInstrumentationModule extends InstrumentationModule {

  public TestDatabaseClientInstrumentationModule() {
    // First arg: instrumentation name (for enabling/disabling)
    // Additional args: aliases for this instrumentation
    super("test-database-client-custom");
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    System.out.println("=== TestDatabaseClientInstrumentationModule: typeInstrumentations called ===");
    return Collections.singletonList(new TestDatabaseClientInstrumentation());
  }

  // Optional: Control execution order relative to other instrumentations
  // Higher numbers run later. Use this to run after upstream instrumentation.
  @Override
  public int order() {
    return 1; // Run after default instrumentation (which uses order 0)
  }
}