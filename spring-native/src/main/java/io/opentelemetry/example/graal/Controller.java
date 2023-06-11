package io.opentelemetry.example.graal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }
}
