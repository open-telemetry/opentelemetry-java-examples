package extension;

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


@AutoService(ConfigurableSamplerProvider.class)
public class ConfigurableDemoSampler implements ConfigurableSamplerProvider {

    @Override
    public Sampler createSampler(ConfigProperties config) {
        double ratio = config.getDouble("otel.instrumentation.demosampler.ratio", 1.0);
        boolean debug = config.getBoolean("otel.instrumentation.demosampler.debug", false);
        int threshold = config.getInt("otel.instrumentation.demosampler.threshold", 100);

        if (debug) {
            System.out.println("ConfigurableDemoSampler: ratio=" + ratio + ", debug=" + debug + ", threshold=" + threshold);
        }

        return new ConfiguredSampler(ratio, debug, threshold);
    }

    @Override
    public String getName() {
        return "demosampler";
    }

    private static class ConfiguredSampler implements Sampler {
        private final Sampler delegate;
        private final boolean debug;
        private final int threshold;

        public ConfiguredSampler(double ratio, boolean debug, int threshold) {
            this.delegate = Sampler.traceIdRatioBased(ratio);
            this.debug = debug;
            this.threshold = threshold;
        }

        @Override
        public SamplingResult shouldSample(
                Context parentContext,
                String traceId,
                String name,
                SpanKind spanKind,
                Attributes attributes,
                List<LinkData> parentLinks) {

            SamplingResult result = delegate.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);

            if (debug) {
                System.out.printf("ConfiguredSampler: span='%s', decision=%s, threshold=%d%n",
                    name, result.getDecision(), threshold);
            }

            return result;
        }

        @Override
        public String getDescription() {
            return String.format("ConfiguredSampler{ratio-based, debug=%s, threshold=%d}", debug, threshold);
        }
    }
}
