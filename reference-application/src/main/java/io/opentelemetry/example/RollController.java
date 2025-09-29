package io.opentelemetry.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RollController {
  private static final Logger logger = LoggerFactory.getLogger(RollController.class);

  @Autowired private OpenTelemetry openTelemetry;

  private final Tracer tracer;
  private final LongCounter diceRollCounter;

  public RollController(@Autowired OpenTelemetry openTelemetry) {
    this.tracer = openTelemetry.getTracer("dice-server", "1.0.0");
    Meter meter = openTelemetry.getMeter("dice-server");
    this.diceRollCounter =
        meter
            .counterBuilder("dice_rolls_total")
            .setDescription("Total number of dice rolls")
            .build();
  }

  @GetMapping("/rolldice")
  public Map<String, Object> rollDice(
      @RequestParam("player") Optional<String> player,
      @RequestParam("rolls") Optional<Integer> rolls) {

    long startTime = System.nanoTime();

    Span span =
        tracer
            .spanBuilder("roll-dice")
            .setSpanKind(SpanKind.SERVER)
            .setAttribute("dice.player", player.orElse("anonymous"))
            .setAttribute("dice.rolls", rolls.orElse(1))
            .startSpan();

    try (Scope scope = span.makeCurrent()) {
      // Add baggage for cross-cutting concerns
      BaggageBuilder baggageBuilder = Baggage.current().toBuilder();
      if (player.isPresent()) {
        baggageBuilder.put("player.name", player.get());
      }
      baggageBuilder.put("request.type", "dice-roll");

      try (Scope baggageScope = baggageBuilder.build().makeCurrent()) {
        int numRolls = rolls.orElse(1);
        if (numRolls < 1 || numRolls > 10) {
          throw new IllegalArgumentException("Number of rolls must be between 1 and 10");
        }

        int[] results = new int[numRolls];
        for (int i = 0; i < numRolls; i++) {
          results[i] = rollSingleDie();
        }

        // Record metrics using OpenTelemetry
        diceRollCounter.add(1);
        long duration = System.nanoTime() - startTime;

        String playerName = player.orElse("Anonymous player");
        if (numRolls == 1) {
          logger.info("{} is rolling the dice: {}", playerName, results[0]);
        } else {
          logger.info(
              "{} is rolling {} dice: {}",
              playerName,
              numRolls,
              java.util.Arrays.toString(results));
        }

        span.addEvent(
            "dice-rolled",
            Attributes.builder()
                .put("dice.result", java.util.Arrays.toString(results))
                .put("dice.sum", java.util.Arrays.stream(results).sum())
                .put("dice.duration_ms", duration / 1_000_000)
                .build());

        Map<String, Object> response = new HashMap<>();
        if (numRolls == 1) {
          response.put("result", results[0]);
        } else {
          response.put("results", results);
          response.put("sum", java.util.Arrays.stream(results).sum());
        }
        response.put("player", playerName);

        return response;
      }
    } catch (Exception e) {
      span.recordException(e);
      span.setStatus(StatusCode.ERROR, e.getMessage());
      logger.error("Error rolling dice for player: {}", player.orElse("anonymous"), e);
      throw e;
    } finally {
      span.end();
    }
  }

  private int rollSingleDie() {
    Span span = tracer.spanBuilder("roll-single-die").setSpanKind(SpanKind.INTERNAL).startSpan();

    try (Scope scope = span.makeCurrent()) {
      // Simulate some work
      try {
        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 5));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }

      int result = ThreadLocalRandom.current().nextInt(1, 7);
      span.setAttribute("dice.value", result);

      // Simulate occasional errors (5% of the time)
      if (ThreadLocalRandom.current().nextDouble() < 0.05) {
        throw new RuntimeException("Simulated dice roll error");
      }

      return result;
    } catch (Exception e) {
      span.recordException(e);
      span.setStatus(StatusCode.ERROR, e.getMessage());
      throw e;
    } finally {
      span.end();
    }
  }

  @GetMapping("/health")
  public Map<String, String> health() {
    Span span = tracer.spanBuilder("health-check").setSpanKind(SpanKind.SERVER).startSpan();

    try (Scope scope = span.makeCurrent()) {
      logger.info("Health check requested");

      Map<String, String> health = new HashMap<>();
      health.put("status", "UP");
      health.put("service", "dice-server");
      health.put("version", "1.0.0");

      span.setAttribute("health.status", "UP");
      return health;
    } finally {
      span.end();
    }
  }
}
