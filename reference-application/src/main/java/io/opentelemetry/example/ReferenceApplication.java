package io.opentelemetry.example;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReferenceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReferenceApplication.class, args);
  }

  @Bean
  public OpenTelemetry openTelemetry() {
    return GlobalOpenTelemetry.get();
  }
}
