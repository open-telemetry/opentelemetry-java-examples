package example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Simple HTTP server for testing OpenTelemetry Java agent extensions.
 */
public class SimpleServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/hello", new HelloHandler());
        server.createContext("/greet", new GreetHandler());
        server.setExecutor(null);

        System.out.println("Starting server on port " + port);
        server.start();
        System.out.println("Server started. Try: http://localhost:8080/hello");
        System.out.println("Press Ctrl+C to stop");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello from OpenTelemetry test app!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            System.out.println("Handled request to /hello");
        }
    }

    static class GreetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String name = exchange.getRequestURI().getQuery();
            String response = "Greetings " + (name != null ? name.replace("name=", "") : "stranger") + "!";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            System.out.println("Handled request to /greet with query: " + name);
        }
    }
}