pluginManagement {
    plugins {
        id("com.diffplug.spotless") version "8.1.0"
        id("com.gradleup.shadow") version "9.2.2"
        id("com.google.protobuf") version "0.9.5"
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
        id("com.google.cloud.tools.jib") version "3.5.1"
        id("com.gradle.develocity") version "4.2.2"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.gradle.develocity")
}

val develocityServer = "https://develocity.opentelemetry.io"
val isCI = System.getenv("CI") != null
val develocityAccessKey = System.getenv("DEVELOCITY_ACCESS_KEY") ?: ""

// if develocity access key is not given and we are in CI, then we publish to scans.gradle.com
val useScansGradleCom = isCI && develocityAccessKey.isEmpty()

develocity {
    if (useScansGradleCom) {
        buildScan {
            termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
            termsOfUseAgree = "yes"
        }
    } else {
        server = develocityServer
        buildScan {
            publishing.onlyIf { it.isAuthenticated }
        }
    }

    buildScan {
        uploadInBackground = !isCI

        capture {
            fileFingerprints = true
        }
    }
}

if (!useScansGradleCom) {
    buildCache {
        remote(develocity.buildCache) {
            isPush = isCI && develocityAccessKey.isNotEmpty()
        }
    }
}

rootProject.name = "opentelemetry-java-examples"
include(
    ":opentelemetry-examples-autoconfigure",
    ":opentelemetry-examples-declarative-configuration",
    ":opentelemetry-examples-javaagent-declarative-configuration",
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
    ":doc-snippets:extensions-minimal",
    ":doc-snippets:extensions-testapp",
)

rootProject.children.forEach {
    if (it.name != "doc-snippets") {
        it.projectDir = file(
          "$rootDir/${it.name}".replace("opentelemetry-examples-", "")
        )
    }
}
