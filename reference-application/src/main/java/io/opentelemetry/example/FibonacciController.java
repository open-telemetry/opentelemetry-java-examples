package io.opentelemetry.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
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

  @Autowired private Counter fibonacciCounter;

  @Autowired private Timer fibonacciTimer;

  private final Tracer tracer;

  public FibonacciController(@Autowired OpenTelemetry openTelemetry) {
    this.tracer = openTelemetry.getTracer("dice-server", "1.0.0");
  }

  @GetMapping("/fibonacci")
  public Map<String, Object> fibonacci(@RequestParam("n") int n) {
    Timer.Sample sample = Timer.start();

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

      long startTime = System.nanoTime();
      BigInteger result = calculateFibonacci(n);
      long duration = System.nanoTime() - startTime;

      fibonacciCounter.increment();
      sample.stop(fibonacciTimer);

      span.addEvent(
          "fibonacci-calculated",
          Attributes.builder()
              .put("fibonacci.result_length", result.toString().length())
              .put("fibonacci.duration_ns", duration)
              .build());

      logger.info("Fibonacci({}) = {} (computed in {}ms)", n, result, duration / 1_000_000);

      Map<String, Object> response = new HashMap<>();
      response.put("n", n);
      response.put("result", result.toString());
      response.put("duration_ms", duration / 1_000_000);

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
