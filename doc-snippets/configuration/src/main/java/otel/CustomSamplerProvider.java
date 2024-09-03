package otel;

import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;

public class CustomSamplerProvider implements ConfigurableSamplerProvider {

  @Override
  public Sampler createSampler(ConfigProperties config) {
    // Callback invoked when OTEL_TRACES_SAMPLER is set to the value from getName().
    return new CustomSampler();
  }

  @Override
  public String getName() {
    return "custom-sampler";
  }
}
