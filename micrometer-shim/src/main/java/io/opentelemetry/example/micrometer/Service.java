package io.opentelemetry.example.micrometer;

import io.micrometer.core.annotation.Timed;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class Service {

  @Timed("dowork.time")
  void doWork() throws InterruptedException {
    Thread.sleep(new Random().nextInt(1000));
  }
}
