pluginManagement {
    plugins {
        id("com.diffplug.spotless") version "7.2.1"
        id("com.github.johnrengelman.shadow") version "8.1.1"
        id("com.google.protobuf") version "0.9.5"
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
        id("com.google.cloud.tools.jib") version "3.4.5"
        id("com.gradle.develocity") version "4.1"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.gradle.develocity")
}

develocity {
    buildScan {
        publishing.onlyIf { System.getenv("CI") != null }
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}

rootProject.name = "opentelemetry-java-examples"
include(
    ":opentelemetry-examples-autoconfigure",
    ":opentelemetry-examples-declarative-configuration",
    ":opentelemetry-examples-http",
    ":opentelemetry-examples-jaeger",
    ":opentelemetry-examples-javaagent",
    ":opentelemetry-examples-log-appender",
    ":opentelemetry-examples-logging",
    ":opentelemetry-examples-logging-k8s-stdout-otlp-json",
    ":opentelemetry-examples-manual-tracing",
    ":opentelemetry-examples-metrics",
    ":opentelemetry-examples-micrometer-shim",
    ":opentelemetry-examples-otlp",
    ":opentelemetry-examples-prometheus",
    ":opentelemetry-examples-sdk-usage",
    ":opentelemetry-examples-telemetry-testing",
    ":opentelemetry-examples-zipkin",
    ":opentelemetry-examples-spring-native",
    ":opentelemetry-examples-kotlin-extension",
    ":opentelemetry-examples-grpc",
    ":opentelemetry-examples-resource-detection-gcp",
    ":doc-snippets:api",
    ":doc-snippets:configuration",
    ":doc-snippets:getting-started",
    ":doc-snippets:exporters",
    ":doc-snippets:spring-starter",
)

rootProject.children.forEach {
    if (it.name != "doc-snippets") {
        it.projectDir = file(
          "$rootDir/${it.name}".replace("opentelemetry-examples-", "")
        )
    }
}
