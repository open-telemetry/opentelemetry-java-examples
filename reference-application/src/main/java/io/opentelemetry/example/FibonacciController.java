package io.opentelemetry.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FibonacciController {
  private static final Logger logger = LoggerFactory.getLogger(FibonacciController.class);

  private final Tracer tracer;
  private final LongCounter fibonacciCounter;

  public FibonacciController(@Autowired OpenTelemetry openTelemetry) {
    this.tracer = openTelemetry.getTracer("dice-server", "1.0.0");
    Meter meter = openTelemetry.getMeter("dice-server");
    this.fibonacciCounter =
        meter
            .counterBuilder("fibonacci_calculations_total")
            .setDescription("Total number of fibonacci calculations")
            .build();
  }

  @GetMapping("/fibonacci")
  public Map<String, Object> fibonacci(@RequestParam("n") int n) {
    long startTime = System.nanoTime();

    Span span =
        tracer
            .spanBuilder("calculate-fibonacci")
            .setSpanKind(SpanKind.SERVER)
            .setAttribute("fibonacci.n", n)
            .startSpan();

    try (Scope scope = span.makeCurrent()) {
      if (n < 0) {
        throw new IllegalArgumentException("n must be non-negative");
      }

      if (n > 100) {
        throw new IllegalArgumentException("n must be <= 100 to prevent excessive computation");
      }

      logger.info("Calculating fibonacci for n={}", n);

      long computationStartTime = System.nanoTime();
      BigInteger result = calculateFibonacci(n);
      long computationDuration = System.nanoTime() - computationStartTime;

      fibonacciCounter.add(1);
      long totalDuration = System.nanoTime() - startTime;

      span.addEvent(
          "fibonacci-calculated",
          Attributes.builder()
              .put("fibonacci.result_length", result.toString().length())
              .put("fibonacci.computation_duration_ns", computationDuration)
              .put("fibonacci.total_duration_ns", totalDuration)
              .build());

      logger.info(
          "Fibonacci({}) = {} (computed in {}ms)", n, result, computationDuration / 1_000_000);

      Map<String, Object> response = new HashMap<>();
      response.put("n", n);
      response.put("result", result.toString());
      response.put("duration_ms", computationDuration / 1_000_000);

      return response;
    } catch (Exception e) {
      span.recordException(e);
      span.setStatus(StatusCode.ERROR, e.getMessage());
      logger.error("Error calculating fibonacci for n={}", n, e);
      throw e;
    } finally {
      span.end();
    }
  }

  private BigInteger calculateFibonacci(int n) {
    Span span =
        tracer
            .spanBuilder("fibonacci-calculation")
            .setSpanKind(SpanKind.INTERNAL)
            .setAttribute("fibonacci.n", n)
            .startSpan();

    try (Scope scope = span.makeCurrent()) {
      if (n <= 1) {
        return BigInteger.valueOf(n);
      }

      // Use iterative approach to avoid deep recursion
      BigInteger a = BigInteger.ZERO;
      BigInteger b = BigInteger.ONE;

      for (int i = 2; i <= n; i++) {
        if (i % 10 == 0) {
          // Add events for progress tracking on larger numbers
          span.addEvent(
              "fibonacci-progress",
              Attributes.builder()
                  .put("fibonacci.progress", i)
                  .put("fibonacci.percent", (i * 100) / n)
                  .build());
        }

        BigInteger temp = a.add(b);
        a = b;
        b = temp;
      }

      span.setAttribute("fibonacci.result_length", b.toString().length());
      return b;
    } finally {
      span.end();
    }
  }
}
