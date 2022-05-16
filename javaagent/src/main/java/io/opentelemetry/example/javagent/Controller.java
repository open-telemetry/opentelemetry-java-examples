package io.opentelemetry.example.javagent;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private static final Logger LOGGER = LogManager.getLogger(Controller.class);
  private final AttributeKey<String> ATTR_METHOD = AttributeKey.stringKey("method");

  private final Random random = new Random();
  private final Tracer tracer;
  private final LongHistogram doWorkHistogram;

  @Autowired
  Controller(OpenTelemetry openTelemetry) {
    tracer = openTelemetry.getTracer(Application.class.getName());
    Meter meter = openTelemetry.getMeter(Application.class.getName());
    doWorkHistogram = meter.histogramBuilder("do-work").ofLongs().build();
  }

  @GetMapping("/ping")
  public String ping() throws InterruptedException {
    int sleepTime = random.nextInt(200);
    doWork(sleepTime);
    doWorkHistogram.record(sleepTime, Attributes.of(ATTR_METHOD, "ping"));
    return "pong";
  }

  private void doWork(int sleepTime) throws InterruptedException {
    var span = tracer.spanBuilder("doWork").startSpan();
    try (var scope = span.makeCurrent()) {
      Thread.sleep(sleepTime);
      LOGGER.info("A sample log message!");
    } finally {
      span.end();
    }
  }
}
