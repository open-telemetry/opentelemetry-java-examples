package otel;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import java.util.Collections;

public class CustomizerProvider implements AutoConfigurationCustomizerProvider {

  @Override
  public void customize(AutoConfigurationCustomizer customizer) {
    // Optionally customize TextMapPropagator.
    customizer.addPropagatorCustomizer((textMapPropagator, configProperties) -> textMapPropagator);
    // Optionally customize Resource.
    customizer.addResourceCustomizer((resource, configProperties) -> resource);
    // Optionally customize Sampler.
    customizer.addSamplerCustomizer((sampler, configProperties) -> sampler);
    // Optionally customize SpanExporter.
    customizer.addSpanExporterCustomizer((spanExporter, configProperties) -> spanExporter);
    // Optionally customize SpanProcessor.
    customizer.addSpanProcessorCustomizer((spanProcessor, configProperties) -> spanProcessor);
    // Optionally supply additional properties.
    customizer.addPropertiesSupplier(Collections::emptyMap);
    // Optionally customize ConfigProperties.
    customizer.addPropertiesCustomizer(configProperties -> Collections.emptyMap());
    // Optionally customize SdkTracerProviderBuilder.
    customizer.addTracerProviderCustomizer((builder, configProperties) -> builder);
    // Optionally customize SdkMeterProviderBuilder.
    customizer.addMeterProviderCustomizer((builder, configProperties) -> builder);
    // Optionally customize MetricExporter.
    customizer.addMetricExporterCustomizer((metricExporter, configProperties) -> metricExporter);
    // Optionally customize MetricReader.
    customizer.addMetricReaderCustomizer((metricReader, configProperties) -> metricReader);
    // Optionally customize SdkLoggerProviderBuilder.
    customizer.addLoggerProviderCustomizer((builder, configProperties) -> builder);
    // Optionally customize LogRecordExporter.
    customizer.addLogRecordExporterCustomizer((exporter, configProperties) -> exporter);
    // Optionally customize LogRecordProcessor.
    customizer.addLogRecordProcessorCustomizer((processor, configProperties) -> processor);
  }

  @Override
  public int order() {
    // Optionally influence the order of invocation.
    return 0;
  }
}
