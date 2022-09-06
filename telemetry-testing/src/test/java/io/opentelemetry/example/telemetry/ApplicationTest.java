package io.opentelemetry.example.telemetry;


import com.google.protobuf.InvalidProtocolBufferException;
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.metrics.v1.Metric;
import io.opentelemetry.proto.trace.v1.Span;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Body;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.stop.Stop.stopQuietly;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

/**
 * The role of this class is to test the opentelemetry traces. It only works if the opentelemetry java agent is
 * attached. The java agent trace exporter is configured to use the otlp exporter with http/protobuf protocol with the
 * otel.exporter.otlp.protocol jvm argument.
 */
@SpringBootTest(classes = { Application.class},
        webEnvironment = RANDOM_PORT)
class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate template;

    // Port of endpoint to export the traces. 4318 is the default port when protocol is http/protobuf.
    // https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#otlp-exporter-span-metric-and-log-exporters
    static final int EXPORTER_ENDPOINT_PORT = 4318;
    // Server running to collect traces. The opentelemetry java agent is configured to export traces with the
    // http/protobuf protocol.
    static ClientAndServer collectorServer;

    @BeforeAll
    public static void setUp() {
        collectorServer = startClientAndServer(EXPORTER_ENDPOINT_PORT);
    }

    @AfterAll
    public static void tearDown() {
        stopQuietly(collectorServer);
    }

    @Test
    public void testTraces() {
        collectorServer.when(request()).respond(response().withStatusCode(200));

        template.getForEntity(URI.create("http://localhost:" + port + "/ping"), String.class);

        // Assert
        await().atMost(30, SECONDS).untilAsserted(() -> {
                    var requests = collectorServer.retrieveRecordedRequests(request());

                    var spans = extractSpansFromRequests(requests);
                    assertThat(spans).extracting(Span::getName).contains("Controller.doWork", "Controller.ping");

                    var metrics = extractMetricsFromRequests(requests);
                    assertThat(metrics).extracting(Metric::getName).contains("do-work");
                }
        );
    }

    /**
     * Extract spans from http requests received by a trace collector.
     *
     * @param requests Request received by an http server trace collector
     * @return spans extracted from the request body
     */
    private List<Span> extractSpansFromRequests(HttpRequest[] requests) {
        return Arrays.stream(requests)
                .map(HttpRequest::getBody)
                .map(Body::getRawBytes)
                .map(body -> {
                    try {
                        return ExportTraceServiceRequest.parseFrom(body);
                    } catch (InvalidProtocolBufferException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(r -> r.getResourceSpansList().stream())
                .flatMap(r -> r.getInstrumentationLibrarySpansList().stream())
                .flatMap(r -> r.getSpansList().stream())
                .collect(Collectors.toList());
    }

    /**
     * Extract metrics from http requests received by a trace collector.
     *
     * @param requests Request received by an http server trace collector
     * @return spans extracted from the request body
     */
    private List<Metric> extractMetricsFromRequests(HttpRequest[] requests) {
        return Arrays.stream(requests).map(HttpRequest::getBody)
                .map(Body::getRawBytes)
                .map(body -> {
                    try {
                        return ExportMetricsServiceRequest.parseFrom(body);
                    } catch (InvalidProtocolBufferException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(r -> r.getResourceMetricsList().stream())
                .flatMap(r -> r.getInstrumentationLibraryMetricsList().stream())
                .flatMap(r -> r.getMetricsList().stream())
                .collect(Collectors.toList());
    }
}