package extension.instrumentation;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

import java.util.Collections;
import java.util.List;

/**
 * InstrumentationModule exactly as documented - validates the example.
 */
@AutoService(InstrumentationModule.class)
public class HttpServerInstrumentationModule extends InstrumentationModule {

    public HttpServerInstrumentationModule() {
        super("http-server-custom");
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return Collections.singletonList(new HttpHandlerInstrumentation());
    }

    @Override
    public int order() {
        return 1; // Run after default instrumentation
    }
}