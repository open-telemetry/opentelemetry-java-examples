package example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/** Simple HTTP server for testing OpenTelemetry Java agent extensions. */
public class SimpleServer {

  private static final Logger logger = Logger.getLogger(SimpleServer.class.getName());

  public static void main(String[] args) throws Exception {
    int port = 8080;
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/hello", new HelloHandler());
    server.setExecutor(null);

    logger.info("Starting server on port " + port);
    server.start();
  }

  static class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      String response = "Hello from OpenTelemetry test app!";
      byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

      exchange.sendResponseHeaders(200, responseBytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
      }

      logger.info("Handled request to /hello");
    }
  }
}
