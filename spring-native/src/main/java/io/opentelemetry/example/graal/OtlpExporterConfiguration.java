package io.opentelemetry.example.graal;

import static io.opentelemetry.exporter.otlp.internal.OtlpConfigUtil.DATA_TYPE_TRACES;

import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporterBuilder;
import io.opentelemetry.exporter.otlp.internal.OtlpConfigUtil;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.internal.AutoConfigureListener;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSpanExporterProvider;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the OTLP exporter. This configuration will replace the default OTLP exporter,
 * and will add a custom header to the requests.
 */
@Configuration
public class OtlpExporterConfiguration {

  @Bean
  ConfigurableSpanExporterProvider otlpSpanExporterProvider() {
    return new OtlpSpanExporterProvider();
  }

  private static class OtlpSpanExporterProvider
      implements ConfigurableSpanExporterProvider, AutoConfigureListener {

    private final AtomicReference<MeterProvider> meterProviderRef =
        new AtomicReference<>(MeterProvider.noop());

    @Override
    public String getName() {
      return "otlp";
    }

    @Override
    public void afterAutoConfigure(OpenTelemetrySdk sdk) {
      meterProviderRef.set(sdk.getMeterProvider());
    }

    @Override
    public SpanExporter createExporter(ConfigProperties config) {
      OtlpHttpSpanExporterBuilder builder = OtlpHttpSpanExporter.builder();

      // Set configuration based on the provided properties - so you don't lose anything compared
      // to not having this bean
      // for Logs, see
      // https://github.com/open-telemetry/opentelemetry-java/blob/main/exporters/otlp/all/src/main/java/io/opentelemetry/exporter/otlp/internal/OtlpLogRecordExporterProvider.java
      // for Metrics, see
      // https://github.com/open-telemetry/opentelemetry-java/blob/main/exporters/otlp/all/src/main/java/io/opentelemetry/exporter/otlp/internal/OtlpMetricExporterProvider.java -
      // you have to add these lines, too:
      // https://github.com/open-telemetry/opentelemetry-java/blob/main/exporters/otlp/all/src/main/java/io/opentelemetry/exporter/otlp/internal/OtlpMetricExporterProvider.java#L48-L55
      OtlpConfigUtil.configureOtlpExporterBuilder(
          DATA_TYPE_TRACES,
          config,
          builder::setEndpoint,
          builder::addHeader,
          builder::setCompression,
          builder::setTimeout,
          builder::setTrustedCertificates,
          builder::setClientTls,
          builder::setRetryPolicy);

      return builder.setMeterProvider(meterProviderRef::get).setHeaders(this::headers).build();
    }

    private Map<String, String> headers() {
      return Collections.singletonMap("Authorization", "Bearer " + refreshToken());
    }

    private String refreshToken() {
      // e.g. read the token from a kubernetes secret
      return "token";
    }
  }
}
