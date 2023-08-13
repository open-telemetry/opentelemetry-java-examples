package io.opentelemetry.example.graal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints({CustomHints.class})
public class Application {

    @Bean
    public OpenTelemetry openTelemetry() {
        OpenTelemetry openTelemetry = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
        return openTelemetry;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
