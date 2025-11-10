package extension.test;

import com.google.auto.service.AutoService;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider;
import io.opentelemetry.sdk.resources.Resource;

/**
 * Testing the ResourceProvider
 */
@AutoService(ResourceProvider.class)
public class TestResourceProvider implements ResourceProvider {

  @Override
  public Resource createResource(ConfigProperties config) {
    System.out.println("=== TestResourceProvider: createResource called ===");
    return Resource.create(
        Attributes.builder()
            .put("deployment.environment", "test")
            .put("service.version", getVersion())
            .put("custom.attribute", "test-value")
            .build()
    );
  }

  private String getVersion() {
    // Retrieve version from environment, file, etc.
    String version = System.getenv().getOrDefault("APP_VERSION", "unknown");
    System.out.println("=== TestResourceProvider: version = " + version + " ===");
    return version;
  }
}