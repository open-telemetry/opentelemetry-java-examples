package otel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class RestClientController {

  private final RestClient restClient;

  public RestClientController(RestClient.Builder restClientBuilder) {
    restClient = restClientBuilder.baseUrl("http://localhost:8080").build();
  }
}
