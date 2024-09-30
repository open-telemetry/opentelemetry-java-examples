package otel;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExtractContextUsage {
  private static final TextMapGetter<HttpExchange> TEXT_MAP_GETTER = new HttpRequestGetter();

  public static void extractContextUsage() throws Exception {
    // Create a ContextPropagators instance which propagates w3c trace context and w3c baggage
    ContextPropagators propagators =
        ContextPropagators.create(
            TextMapPropagator.composite(
                W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance()));

    // Create a server, which uses the propagators to extract context from requests
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/path", new Handler(propagators));
    server.setExecutor(null);
    server.start();
  }

  private static class Handler implements HttpHandler {
    private final ContextPropagators contextPropagators;

    private Handler(ContextPropagators contextPropagators) {
      this.contextPropagators = contextPropagators;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      // Extract the context from the request and make the context current
      Context extractedContext =
          contextPropagators
              .getTextMapPropagator()
              .extract(Context.current(), exchange, TEXT_MAP_GETTER);
      try (Scope scope = extractedContext.makeCurrent()) {
        // Do work with the extracted context
      } finally {
        String response = "success";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
      }
    }
  }

  /** {@link TextMapSetter} with a {@link HttpExchange} carrier. */
  private static class HttpRequestGetter implements TextMapGetter<HttpExchange> {
    @Override
    public Iterable<String> keys(HttpExchange carrier) {
      return carrier.getRequestHeaders().keySet();
    }

    @Override
    public String get(HttpExchange carrier, String key) {
      if (carrier == null) {
        return null;
      }
      List<String> headers = carrier.getRequestHeaders().get(key);
      if (headers == null || headers.isEmpty()) {
        return null;
      }
      return headers.get(0);
    }
  }
}
