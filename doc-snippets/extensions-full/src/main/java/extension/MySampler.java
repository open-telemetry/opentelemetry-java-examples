package extension;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;

/**
 * Custom Sampler that demonstrates logging during initialization.
 */
public class MySampler implements Sampler {

    public MySampler() {
        // From docs: Use System.out.println() for debugging during initialization
        System.out.println("MySampler: constructor called");
    }

    @Override
    public SamplingResult shouldSample(
            Context parentContext,
            String traceId,
            String name,
            SpanKind spanKind,
            Attributes attributes,
            List<LinkData> parentLinks) {

        // Sample all spans for this demo
        System.out.printf("MySampler: sampling span '%s' - RECORD_AND_SAMPLE%n", name);
        return SamplingResult.recordAndSample();
    }

    @Override
    public String getDescription() {
        return "MySampler{alwaysSample}";
    }
}