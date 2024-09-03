package otel;

import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.util.Collections;

public class CustomizedAutoConfiguredSdk {
  public static OpenTelemetrySdk autoconfiguredSdk() {
    return AutoConfiguredOpenTelemetrySdk.builder()
        // Optionally customize TextMapPropagator.
        .addPropagatorCustomizer((textMapPropagator, configProperties) -> textMapPropagator)
        // Optionally customize Resource.
        .addResourceCustomizer((resource, configProperties) -> resource)
        // Optionally customize Sampler.
        .addSamplerCustomizer((sampler, configProperties) -> sampler)
        // Optionally customize SpanExporter.
        .addSpanExporterCustomizer((spanExporter, configProperties) -> spanExporter)
        // Optionally customize SpanProcessor.
        .addSpanProcessorCustomizer((spanProcessor, configProperties) -> spanProcessor)
        // Optionally supply additional properties.
        .addPropertiesSupplier(Collections::emptyMap)
        // Optionally customize ConfigProperties.
        .addPropertiesCustomizer(configProperties -> Collections.emptyMap())
        // Optionally customize SdkTracerProviderBuilder.
        .addTracerProviderCustomizer((builder, configProperties) -> builder)
        // Optionally customize SdkMeterProviderBuilder.
        .addMeterProviderCustomizer((builder, configProperties) -> builder)
        // Optionally customize MetricExporter.
        .addMetricExporterCustomizer((metricExporter, configProperties) -> metricExporter)
        // Optionally customize MetricReader.
        .addMetricReaderCustomizer((metricReader, configProperties) -> metricReader)
        // Optionally customize SdkLoggerProviderBuilder.
        .addLoggerProviderCustomizer((builder, configProperties) -> builder)
        // Optionally customize LogRecordExporter.
        .addLogRecordExporterCustomizer((logRecordExporter, configProperties) -> logRecordExporter)
        // Optionally customize LogRecordProcessor.
        .addLogRecordProcessorCustomizer((processor, configProperties) -> processor)
        .build()
        .getOpenTelemetrySdk();
  }
}
