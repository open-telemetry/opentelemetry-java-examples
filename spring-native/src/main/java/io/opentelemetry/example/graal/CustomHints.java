package io.opentelemetry.example.graal;

import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporterBuilder;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporterBuilder;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporterBuilder;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class CustomHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        hints.reflection().registerType(TypeReference.of("io.opentelemetry.instrumentation.spring.autoconfigure.aspects.InstrumentationWithSpanAspect"), MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_METHODS);
        hints.reflection().registerType(OtlpGrpcMetricExporterBuilder.class, MemberCategory.DECLARED_FIELDS);
        hints.reflection().registerType(OtlpGrpcSpanExporterBuilder.class, MemberCategory.DECLARED_FIELDS);
        hints.reflection().registerType(OtlpGrpcLogRecordExporterBuilder.class, MemberCategory.DECLARED_FIELDS);
    }
}
