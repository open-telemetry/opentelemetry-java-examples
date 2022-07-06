package io.opentelemetry.example.micrometer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @Autowired private Service service;

  @GetMapping("/ping")
  public String ping() throws InterruptedException {
    service.doWork();
    return "pong";
  }
}
