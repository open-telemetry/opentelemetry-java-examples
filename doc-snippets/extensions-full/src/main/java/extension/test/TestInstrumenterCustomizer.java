package extension.test;

import com.google.auto.service.AutoService;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.incubator.instrumenter.InstrumenterCustomizer;
import io.opentelemetry.instrumentation.api.incubator.instrumenter.InstrumenterCustomizerProvider;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;

/**
 * Testing the InstrumenterCustomizerProvider example from the documentation
 * Lines 501-548 of extensions.md
 */
@AutoService(InstrumenterCustomizerProvider.class)
public class TestInstrumenterCustomizer implements InstrumenterCustomizerProvider {

  @Override
  public void customize(InstrumenterCustomizer customizer) {
    String instrumentationName = customizer.getInstrumentationName();
    System.out.println("=== TestInstrumenterCustomizer: instrumentationName = " + instrumentationName + " ===");

    // Target specific instrumentation
    if (instrumentationName.contains("http-server")) {
      System.out.println("=== TestInstrumenterCustomizer: adding custom attributes extractor ===");
      customizer.addAttributesExtractor(new TestAttributesExtractor());
    }
  }

  private static class TestAttributesExtractor implements AttributesExtractor<Object, Object> {
    private static final AttributeKey<String> CUSTOM_ATTR =
        AttributeKey.stringKey("custom.attribute");

    @Override
    public void onStart(AttributesBuilder attributes, Context context, Object request) {
      attributes.put(CUSTOM_ATTR, "custom-value");
    }

    @Override
    public void onEnd(AttributesBuilder attributes, Context context,
                     Object request, Object response, Throwable error) {
      if (error != null) {
        attributes.put(AttributeKey.stringKey("error.type"),
                      error.getClass().getSimpleName());
      }
    }
  }
}