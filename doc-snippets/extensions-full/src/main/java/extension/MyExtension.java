package extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Main extension entry point using exact patterns from the documentation.
 * Demonstrates @AutoService, System.out.println debugging, and configuring components.
 */
@AutoService(AutoConfigurationCustomizerProvider.class)
public class MyExtension implements AutoConfigurationCustomizerProvider {

    @Override
    public void customize(AutoConfigurationCustomizer config) {
        System.out.println("MyExtension: customize() called");

        // From docs: Provide default configuration values
        config.addPropertiesSupplier(this::getDefaultProperties);

        config.addTracerProviderCustomizer(this::configureTracer);
    }

    private SdkTracerProviderBuilder configureTracer(
            SdkTracerProviderBuilder tracerProvider, ConfigProperties config) {
        System.out.println("MyExtension: configuring tracer provider");

        // From docs: Read configuration with defaults
        boolean customEnabled = config.getBoolean("otel.instrumentation.myext.enabled", true);
        System.out.println("MyExtension: custom instrumentation enabled = " + customEnabled);

        return tracerProvider
                .setSampler(new MySampler())
                .addSpanProcessor(new MySpanProcessor());
    }

    private Map<String, String> getDefaultProperties() {
        // From docs: Providing default configuration
        Map<String, String> props = new HashMap<>();
        props.put("otel.service.name", "extension-test-service");
        props.put("otel.instrumentation.myext.enabled", "true");
        props.put("otel.instrumentation.myext.threshold", "100");
        System.out.println("MyExtension: providing default configuration");
        return props;
    }
}