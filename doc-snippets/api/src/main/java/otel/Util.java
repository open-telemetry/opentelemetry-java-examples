package otel;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;
import java.util.Random;

class Util {

  private static final ContextKey<String> SHAPE_CONTEXT_KEY = ContextKey.named("shape");
  private static final Random random = new Random();

  static final AttributeKey<String> WIDGET_SHAPE = AttributeKey.stringKey("com.acme.widget.shape");
  static final AttributeKey<String> WIDGET_COLOR = AttributeKey.stringKey("com.acme.widget.color");

  static final Attributes WIDGET_RED_SQUARE =
      Attributes.of(WIDGET_SHAPE, "square", WIDGET_COLOR, "red");
  static final Attributes WIDGET_RED_CIRCLE =
      Attributes.of(WIDGET_SHAPE, "circle", WIDGET_COLOR, "circle");

  static String computeWidgetShape() {
    return random.nextBoolean() ? "circle" : "square";
  }

  static String computeWidgetColor() {
    return random.nextBoolean() ? "red" : "blue";
  }

  static Context customContext() {
    return Context.current().with(SHAPE_CONTEXT_KEY, computeWidgetShape());
  }

  private Util() {}
}
