package io.opentelemetry.example.micrometer;

import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/ping")
  @Timed("ping.time")
  public String ping() {
    return "pong";
  }
}
