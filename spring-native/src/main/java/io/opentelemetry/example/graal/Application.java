package io.opentelemetry.example.graal;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.contrib.sampler.RuleBasedRoutingSampler;
import io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryInjector;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.semconv.SemanticAttributes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public AutoConfigurationCustomizerProvider otelCustomizer() {
    return p ->
        p.addSamplerCustomizer(
            (fallback, c) ->
                RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallback)
                    .drop(SemanticAttributes.URL_PATH, "^/actuator")
                    .build());
  }

  @Bean
  OpenTelemetryInjector registerGlobalOpenTelemetry() {
    // not recommended, since you can just auto-wire OpenTelemetry where you need it
    return GlobalOpenTelemetry::set;
  }
}
