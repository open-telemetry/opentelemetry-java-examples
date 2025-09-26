package io.opentelemetry.example;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.stop.Stop.stopQuietly;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.google.protobuf.InvalidProtocolBufferException;
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.metrics.v1.Metric;
import io.opentelemetry.proto.trace.v1.Span;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Body;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Test class to validate that the reference application properly exports telemetry data.
 * This test uses MockServer to capture OTLP requests and verify that spans and metrics
 * are being generated and exported correctly.
 */
@SpringBootTest(
    classes = {ReferenceApplication.class},
    webEnvironment = RANDOM_PORT)
class TelemetryTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate template;

    // Port of endpoint to export the telemetry data. 4318 is the default port when protocol is
    // http/protobuf.
    static final int EXPORTER_ENDPOINT_PORT = 4318;
    
    // Server running to collect traces and metrics. The OpenTelemetry Java agent is configured 
    // to export telemetry with the http/protobuf protocol.
    static ClientAndServer collectorServer;

    @BeforeAll
    public static void setUp() {
        collectorServer = startClientAndServer(EXPORTER_ENDPOINT_PORT);
        collectorServer.when(request()).respond(response().withStatusCode(200));
    }

    @AfterAll
    public static void tearDown() {
        stopQuietly(collectorServer);
    }

    @Test
    public void testDiceRollTelemetry() {
        // Call the dice roll endpoint
        template.getForEntity(URI.create("http://localhost:" + port + "/rolldice"), String.class);

        // Verify telemetry data was exported
        await()
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
                var requests = collectorServer.retrieveRecordedRequests(request());

                // Verify traces - should have HTTP spans and custom spans
                var spans = extractSpansFromRequests(requests);
                assertThat(spans)
                    .extracting(Span::getName)
                    .contains("GET /rolldice", "roll-dice", "roll-single-die");

                // Verify metrics - should have custom dice roll metrics
                var metrics = extractMetricsFromRequests(requests);
                assertThat(metrics)
                    .extracting(Metric::getName)
                    .contains("dice_rolls_total", "dice_roll_duration_seconds");
            });
    }

    @Test 
    public void testFibonacciTelemetry() {
        // Call the fibonacci endpoint
        template.getForEntity(URI.create("http://localhost:" + port + "/fibonacci?n=8"), String.class);

        // Verify telemetry data was exported
        await()
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
                var requests = collectorServer.retrieveRecordedRequests(request());

                // Verify traces - should have HTTP spans and custom fibonacci spans
                var spans = extractSpansFromRequests(requests);
                assertThat(spans)
                    .extracting(Span::getName)
                    .contains("GET /fibonacci", "calculate-fibonacci", "fibonacci-calculation");

                // Verify metrics - should have custom fibonacci metrics  
                var metrics = extractMetricsFromRequests(requests);
                assertThat(metrics)
                    .extracting(Metric::getName)
                    .contains("fibonacci_calculations_total", "fibonacci_duration_seconds");
            });
    }

    @Test
    public void testMultipleDiceRollTelemetry() {
        // Call the multiple dice roll endpoint
        template.getForEntity(URI.create("http://localhost:" + port + "/rolldice?rolls=3"), String.class);

        // Verify telemetry data was exported
        await()
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
                var requests = collectorServer.retrieveRecordedRequests(request());

                // Verify traces - should have HTTP spans and custom spans for multiple rolls
                var spans = extractSpansFromRequests(requests);
                assertThat(spans)
                    .extracting(Span::getName)
                    .contains("GET /rolldice", "roll-dice", "roll-single-die");

                // Check that there are multiple roll-single-die spans (one for each roll)
                var singleDieSpans = spans.stream()
                    .filter(span -> "roll-single-die".equals(span.getName()))
                    .collect(Collectors.toList());

                assertThat(singleDieSpans).hasSizeGreaterThanOrEqualTo(3);
                
                // Verify span attributes contain dice information
                var rollDiceSpans = spans.stream()
                    .filter(span -> "roll-dice".equals(span.getName()))
                    .collect(Collectors.toList());

                assertThat(rollDiceSpans).isNotEmpty();
                
                // Verify span attributes contain roll information
                rollDiceSpans.forEach(span -> {
                    var attributes = span.getAttributesList();
                    assertThat(attributes).isNotEmpty();
                });
            });
    }

    @Test
    public void testHealthEndpointTelemetry() {
        // Call the health endpoint
        template.getForEntity(URI.create("http://localhost:" + port + "/health"), String.class);

        // Verify telemetry data was exported
        await()
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
                var requests = collectorServer.retrieveRecordedRequests(request());

                // Verify traces - should have HTTP spans for health check
                var spans = extractSpansFromRequests(requests);
                assertThat(spans)
                    .extracting(Span::getName)
                    .contains("GET /health", "health-check");
            });
    }

    @Test
    public void testSpanEventsAndAttributes() {
        // Call the fibonacci endpoint with a larger number to trigger progress events
        template.getForEntity(URI.create("http://localhost:" + port + "/fibonacci?n=25"), String.class);

        // Verify telemetry data includes events and detailed attributes
        await()
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
                var requests = collectorServer.retrieveRecordedRequests(request());

                var spans = extractSpansFromRequests(requests);
                
                // Find the fibonacci calculation span
                var fibonacciSpans = spans.stream()
                    .filter(span -> "fibonacci-calculation".equals(span.getName()))
                    .collect(Collectors.toList());

                assertThat(fibonacciSpans).isNotEmpty();
                
                // Check that the span has attributes
                fibonacciSpans.forEach(span -> {
                    var attributes = span.getAttributesList();
                    var hasNAttribute = attributes.stream()
                        .anyMatch(attr -> "fibonacci.n".equals(attr.getKey()) && 
                                         attr.getValue().getIntValue() == 25);
                    assertThat(hasNAttribute).isTrue();
                });
                
                // Find the calculate-fibonacci span and check for events
                var calculateSpans = spans.stream()
                    .filter(span -> "calculate-fibonacci".equals(span.getName()))
                    .collect(Collectors.toList());

                assertThat(calculateSpans).isNotEmpty();
                
                calculateSpans.forEach(span -> {
                    // Should have fibonacci-calculated event
                    var events = span.getEventsList();
                    var hasFibonacciEvent = events.stream()
                        .anyMatch(event -> "fibonacci-calculated".equals(event.getName()));
                    assertThat(hasFibonacciEvent).isTrue();
                });
            });
    }

    @Test
    public void testBaggagePropagation() {
        // Call an endpoint that should propagate baggage
        template.getForEntity(
            URI.create("http://localhost:" + port + "/rolldice?player=testuser"), 
            String.class);

        // Verify telemetry data was exported with baggage
        await()
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
                var requests = collectorServer.retrieveRecordedRequests(request());

                // Verify traces contain baggage information
                var spans = extractSpansFromRequests(requests);
                assertThat(spans)
                    .extracting(Span::getName)
                    .contains("roll-dice");

                // Check for spans with player information in attributes
                var rollDiceSpans = spans.stream()
                    .filter(span -> "roll-dice".equals(span.getName()))
                    .collect(Collectors.toList());

                assertThat(rollDiceSpans).isNotEmpty();
                
                // Verify span has player attribute
                rollDiceSpans.forEach(span -> {
                    var attributes = span.getAttributesList();
                    var playerAttr = attributes.stream()
                        .anyMatch(attr -> "dice.player".equals(attr.getKey()) && 
                                         "testuser".equals(attr.getValue().getStringValue()));
                    assertThat(playerAttr).isTrue();
                });
            });
    }

    /**
     * Extract spans from http requests received by a telemetry collector.
     *
     * @param requests Request received by a http server trace collector
     * @return spans extracted from the request body
     */
    private List<Span> extractSpansFromRequests(HttpRequest[] requests) {
        return Arrays.stream(requests)
            .map(HttpRequest::getBody)
            .flatMap(body -> getExportTraceServiceRequest(body).stream())
            .flatMap(r -> r.getResourceSpansList().stream())
            .flatMap(r -> r.getScopeSpansList().stream())
            .flatMap(r -> r.getSpansList().stream())
            .collect(Collectors.toList());
    }

    private Optional<ExportTraceServiceRequest> getExportTraceServiceRequest(Body body) {
        try {
            return Optional.ofNullable(ExportTraceServiceRequest.parseFrom(body.getRawBytes()));
        } catch (InvalidProtocolBufferException e) {
            return Optional.empty();
        }
    }

    /**
     * Extract metrics from http requests received by a telemetry collector.
     *
     * @param requests Request received by an http server telemetry collector
     * @return metrics extracted from the request body
     */
    private List<Metric> extractMetricsFromRequests(HttpRequest[] requests) {
        return Arrays.stream(requests)
            .map(HttpRequest::getBody)
            .flatMap(body -> getExportMetricsServiceRequest(body).stream())
            .flatMap(r -> r.getResourceMetricsList().stream())
            .flatMap(r -> r.getScopeMetricsList().stream())
            .flatMap(r -> r.getMetricsList().stream())
            .collect(Collectors.toList());
    }

    private Optional<ExportMetricsServiceRequest> getExportMetricsServiceRequest(Body body) {
        try {
            return Optional.ofNullable(ExportMetricsServiceRequest.parseFrom(body.getRawBytes()));
        } catch (InvalidProtocolBufferException e) {
            return Optional.empty();
        }
    }
}