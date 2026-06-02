/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.examples.fileconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api")
public class ApiController {

  private final RestClient restClient = RestClient.create();
  private final String remoteTargetUrl;

  public ApiController(
      @Value("${remote.target.url:http://localhost:8080/api/example}") String remoteTargetUrl) {
    this.remoteTargetUrl = remoteTargetUrl;
  }

  @GetMapping("/example")
  public ResponseEntity<String> example() {
    return ResponseEntity.ok("Hello from OpenTelemetry example API!");
  }

  // Makes an outgoing HTTP (CLIENT) call so the peer service mapping in
  // otel-agent-config.yaml can tag the client span with a "peer.service" attribute.
  @GetMapping("/remote")
  public ResponseEntity<String> remote() {
    String body = restClient.get().uri(remoteTargetUrl).retrieve().body(String.class);
    return ResponseEntity.ok("Remote call returned: " + body);
  }
}
