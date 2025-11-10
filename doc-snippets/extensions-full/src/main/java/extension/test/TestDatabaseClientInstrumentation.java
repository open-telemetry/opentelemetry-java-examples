package extension.test;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class TestDatabaseClientInstrumentation implements TypeInstrumentation {

  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    System.out.println("=== TestDatabaseClientInstrumentation: typeMatcher called ===");
    // Match a test class - we'll use SimpleHttpServer as our target
    return named("com.example.app.SimpleHttpServer");
  }

  @Override
  public void transform(TypeTransformer transformer) {
    System.out.println("=== TestDatabaseClientInstrumentation: transform called ===");
    // Apply advice to the main method
    transformer.applyAdviceToMethod(
      named("main")
        .and(isPublic())
        .and(isStatic()),
      this.getClass().getName() + "$TestAdvice"
    );
  }

  // Advice class
  public static class TestAdvice {

    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void onEnter() {
      System.out.println("=== TestAdvice: onEnter called ===");

      Span span = Java8BytecodeBridge.currentSpan();
      span.setAttribute("test.instrumentation", "active");

      TestAdviceHelper.logEntry();
    }

    @Advice.OnMethodExit(suppress = Throwable.class, onThrowable = Throwable.class)
    public static void onExit(@Advice.Thrown Throwable throwable) {
      System.out.println("=== TestAdvice: onExit called ===");
      if (throwable != null) {
        Span span = Java8BytecodeBridge.currentSpan();
        span.setAttribute("test.error", true);
      }
    }
  }
}