package io.opentelemetry.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReferenceApplicationTests {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testRollDice() throws Exception {
    ResponseEntity<String> response =
        restTemplate.getForEntity("http://localhost:" + port + "/rolldice", String.class);

    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    assertNotNull(json.get("result"));
    assertNotNull(json.get("player"));

    int result = json.get("result").asInt();
    assertTrue(result >= 1 && result <= 6);
  }

  @Test
  void testRollDiceWithPlayer() throws Exception {
    ResponseEntity<String> response =
        restTemplate.getForEntity(
            "http://localhost:" + port + "/rolldice?player=testplayer", String.class);

    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    assertEquals("testplayer", json.get("player").asText());
  }

  @Test
  void testRollMultipleDice() throws Exception {
    ResponseEntity<String> response =
        restTemplate.getForEntity("http://localhost:" + port + "/rolldice?rolls=3", String.class);

    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    assertNotNull(json.get("results"));
    assertNotNull(json.get("sum"));
    assertEquals(3, json.get("results").size());
  }

  @Test
  void testFibonacci() throws Exception {
    ResponseEntity<String> response =
        restTemplate.getForEntity("http://localhost:" + port + "/fibonacci?n=10", String.class);

    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    assertEquals(10, json.get("n").asInt());
    assertEquals("55", json.get("result").asText());
  }

  @Test
  void testHealth() throws Exception {
    ResponseEntity<String> response =
        restTemplate.getForEntity("http://localhost:" + port + "/health", String.class);

    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    assertEquals("UP", json.get("status").asText());
    assertEquals("dice-server", json.get("service").asText());
  }
}
