package extension.test;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.SpanLimits;

import java.util.HashMap;
import java.util.Map;

/**
 * Testing the AutoConfigurationCustomizerProvider
 */
@AutoService(AutoConfigurationCustomizerProvider.class)
public class TestAutoConfigurationCustomizer implements AutoConfigurationCustomizerProvider {

  @Override
  public void customize(AutoConfigurationCustomizer config) {
    System.out.println("=== TestAutoConfigurationCustomizer loaded ===");
    config
        .addTracerProviderCustomizer(this::configureTracer)
        .addPropertiesSupplier(this::getDefaultProperties);
  }

  private SdkTracerProviderBuilder configureTracer(
      SdkTracerProviderBuilder tracerProvider, ConfigProperties config) {
    System.out.println("=== TestAutoConfigurationCustomizer: configureTracer called ===");
    return tracerProvider
        .setSpanLimits(SpanLimits.builder().setMaxNumberOfAttributes(1024).build());
  }

  private Map<String, String> getDefaultProperties() {
    System.out.println("=== TestAutoConfigurationCustomizer: getDefaultProperties called ===");
    Map<String, String> properties = new HashMap<>();
    properties.put("otel.exporter.otlp.endpoint", "http://test-backend:8080");
    properties.put("otel.service.name", "test-service");
    return properties;
  }
}