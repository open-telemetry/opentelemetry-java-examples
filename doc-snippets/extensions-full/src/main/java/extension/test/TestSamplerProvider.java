package extension.test;

import com.google.auto.service.AutoService;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;

/**
 * Testing the ConfigurableSamplerProvider
 */
@AutoService(ConfigurableSamplerProvider.class)
public class TestSamplerProvider implements ConfigurableSamplerProvider {

  @Override
  public Sampler createSampler(ConfigProperties config) {
    System.out.println("=== TestSamplerProvider: createSampler called ===");
    // Access configuration
    double samplingRate = config.getDouble("otel.traces.sampler.arg", 1.0);
    System.out.println("=== TestSamplerProvider: samplingRate = " + samplingRate + " ===");
    return new TestCustomSampler(samplingRate);
  }

  @Override
  public String getName() {
    // Use with: -Dotel.traces.sampler=test-sampler
    return "test-sampler";
  }

  private static class TestCustomSampler implements Sampler {
    private final double samplingRate;

    TestCustomSampler(double samplingRate) {
      this.samplingRate = samplingRate;
    }

    @Override
    public SamplingResult shouldSample(
        Context parentContext,
        String traceId,
        String name,
        SpanKind spanKind,
        Attributes attributes,
        List<LinkData> parentLinks) {
      System.out.println("=== TestCustomSampler: shouldSample called for span: " + name + " ===");
      // Simple implementation - sample everything
      return SamplingResult.recordAndSample();
    }

    @Override
    public String getDescription() {
      return "TestCustomSampler{samplingRate=" + samplingRate + "}";
    }
  }
}