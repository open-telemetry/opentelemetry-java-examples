package io.opentelemetry.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReferenceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReferenceApplication.class, args);
  }

  @Bean
  public Counter diceRollCounter(MeterRegistry meterRegistry) {
    return Counter.builder("dice_rolls_total")
        .description("Total number of dice rolls")
        .register(meterRegistry);
  }

  @Bean
  public Timer diceRollTimer(MeterRegistry meterRegistry) {
    return Timer.builder("dice_roll_duration")
        .description("Time taken to roll dice")
        .register(meterRegistry);
  }

  @Bean
  public Counter fibonacciCounter(MeterRegistry meterRegistry) {
    return Counter.builder("fibonacci_calculations_total")
        .description("Total number of fibonacci calculations")
        .register(meterRegistry);
  }

  @Bean
  public Timer fibonacciTimer(MeterRegistry meterRegistry) {
    return Timer.builder("fibonacci_duration")
        .description("Time taken to calculate fibonacci")
        .register(meterRegistry);
  }
}
