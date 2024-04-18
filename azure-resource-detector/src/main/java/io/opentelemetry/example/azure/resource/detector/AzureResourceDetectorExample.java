package io.opentelemetry.example.azure.resource.detector;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.contrib.azure.resource.AzureAppServiceResourceProvider;
import io.opentelemetry.contrib.azure.resource.AzureContainersResourceProvider;
import io.opentelemetry.contrib.azure.resource.AzureEnvVarPlatform;
import io.opentelemetry.contrib.azure.resource.AzureFunctionsResourceProvider;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.sdk.common.CompletableResultCode;
import java.util.concurrent.TimeUnit;

public class AzureResourceDetectorExample {
  private static final String INSTRUMENTATION_SCOPE_NAME =
      AzureResourceDetectorExample.class.getName();

  public static void main(String[] args) {
    // Get the autoconfigured OpenTelemetry SDK
    OpenTelemetrySdk openTelemetrySdk =
        AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();

    AzureEnvVarPlatform detect = AzureEnvVarPlatform.detect(System.getenv());
    if (detect == AzureEnvVarPlatform.APP_SERVICE) {
      // Shows the resource attributes detected by the Azure App Service Resource Provider
      System.out.println("Detecting Azure App Service resource");
      AzureAppServiceResourceProvider resourceProvider = new AzureAppServiceResourceProvider();
      System.out.println(resourceProvider.getAttributes() + "\n");
    } else if (detect == AzureEnvVarPlatform.FUNCTIONS) {
      // Shows the resource attributes detected by the Azure Function Resource Provider
      System.out.println("Detecting Azure Function resource");
      AzureFunctionsResourceProvider resourceProvider = new AzureFunctionsResourceProvider();
      System.out.println(resourceProvider.getAttributes() + "\n");
    } else if (detect == AzureEnvVarPlatform.CONTAINER_APP) {
      // Shows the resource attributes detected by the Azure Container Insights Resource Provider
      System.out.println("Detecting Azure Container App resource");
      AzureContainersResourceProvider resourceProvider = new AzureContainersResourceProvider();
      System.out.println(resourceProvider.getAttributes() + "\n");
    }

    // Shows the attributes attached to the Resource that was set for TracerProvider
    // via the autoconfiguration SPI.
    System.out.println("Detecting resource: Azure resources");
    Span span =
        openTelemetrySdk
            .getTracer(INSTRUMENTATION_SCOPE_NAME)
            .spanBuilder("azure-resource-detector")
            .startSpan();
    try (Scope ignored = span.makeCurrent()) {
      // Simulate work: this could be simulating a network request or an expensive disk operation
      Thread.sleep(500);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      span.end();
    }
    // Flush all buffered traces
    CompletableResultCode completableResultCode =
        openTelemetrySdk.getSdkTracerProvider().shutdown();
    // wait till export finishes
    completableResultCode.join(10000, TimeUnit.MILLISECONDS);
  }
}
