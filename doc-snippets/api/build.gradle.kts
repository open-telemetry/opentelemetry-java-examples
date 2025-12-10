plugins {
    id("java")
}

val moduleName by extra { "io.opentelemetry.examples.docs.configuration" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    // TODO: revert version after 2.23.0 instrumentation release
    implementation("io.opentelemetry:opentelemetry-api:1.57.0")

    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv-incubating:1.37.0-alpha")
}
