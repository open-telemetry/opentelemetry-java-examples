package io.opentelemetry.resource.gcp;

import io.opentelemetry.contrib.gcp.resource.GCPResourceProvider;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.ResourceConfiguration;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;

public class GCPResourceExample {
  public static void main(String[] args) {
    // Get the autoconfigured OpenTelemetry SDK
    OpenTelemetrySdk openTelemetrySdk =
        AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();

    // Shows the resource attributes detected from the environment variables
    // and system properties.
    System.out.println("Detecting resource: Environment");
    Resource autoResource = ResourceConfiguration.createEnvironmentResource();
    System.out.println(autoResource.getAttributes() + "\n");

    // Shows the resource attributes detected by the GCP Resource Provider
    System.out.println("Detecting resource: hardcoded");
    GCPResourceProvider resourceProvider = new GCPResourceProvider();
    System.out.println(resourceProvider.getAttributes() + "\n");

    // Shows the attributes attached to the Resource that was set for TracerProvider
    // via the autoconfiguration SPI.
    // This works similarly for MeterProvider and LoggerProvider.
    System.out.println("Detecting resource: Autoconfigure");
    SdkTracerProvider autoConfTracerProvider = openTelemetrySdk.getSdkTracerProvider();
    System.out.println(autoConfTracerProvider.toString() + "\n");
  }
}
