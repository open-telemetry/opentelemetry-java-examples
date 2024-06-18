package otel;

import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

public class ZipkinExporter {
  public static SdkTracerProvider create(Resource resource) {
    SdkTracerProvider sdkTracerProvider =
        SdkTracerProvider.builder()
            .addSpanProcessor(
                BatchSpanProcessor.builder(
                        ZipkinSpanExporter.builder()
                            .setEndpoint("http://localhost:9411/api/v2/spans")
                            .build())
                    .build())
            .setResource(resource)
            .build();

    return sdkTracerProvider;
  }
}
