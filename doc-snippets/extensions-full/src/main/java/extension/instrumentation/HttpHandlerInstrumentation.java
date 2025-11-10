package extension.instrumentation;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * TypeInstrumentation following the documented pattern.
 */
public class HttpHandlerInstrumentation implements TypeInstrumentation {

    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        // Target our SimpleServer inner classes
        return named("example.SimpleServer$HelloHandler")
            .or(named("example.SimpleServer$GreetHandler"));
    }

    @Override
    public void transform(TypeTransformer transformer) {
        transformer.applyAdviceToMethod(
            named("handle")
                .and(takesArgument(0, named("com.sun.net.httpserver.HttpExchange")))
                .and(isPublic()),
            this.getClass().getName() + "$HandleAdvice"
        );
    }

    public static class HandleAdvice {

        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This Object handler) {
            System.out.println("=== Advice.OnMethodEnter: Handler.handle() ===");

            System.out.println("Call stack:");
            Thread.dumpStack();

            AdviceHelper.processHandler(handler);

            Span span = Java8BytecodeBridge.currentSpan();
            span.setAttribute("custom.handler.class", handler.getClass().getSimpleName());
            span.setAttribute("custom.instrumentation", "http-server-custom");
        }

        @Advice.OnMethodExit(suppress = Throwable.class, onThrowable = Throwable.class)
        public static void onExit(@Advice.Thrown Throwable throwable) {
            if (throwable != null) {
                System.out.println("Handler threw exception: " + throwable.getMessage());
                Span span = Java8BytecodeBridge.currentSpan();
                span.setAttribute("custom.handler.failed", true);
            }
        }
    }
}