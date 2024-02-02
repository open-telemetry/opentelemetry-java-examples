import com.google.protobuf.gradle.*

plugins {
    id("java")
    id ("com.google.protobuf")
    id ("idea")
}

description = "OpenTelemetry Examples for GRPC"
val moduleName by extra { "io.opentelemetry.examples.grpc" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

var grpcVersion = "1.61.1" // CURRENT_GRPC_VERSION
var protobufVersion = "3.25.2"
var protocVersion = protobufVersion

dependencies {
    runtimeOnly ("io.grpc:grpc-netty-shaded:${grpcVersion}")
    implementation ("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation ("io.grpc:grpc-services:${grpcVersion}")
    implementation ("io.grpc:grpc-stub:${grpcVersion}")
    implementation ("com.google.protobuf:protobuf-java-util:3.25.2")
    implementation ("io.opentelemetry.instrumentation:opentelemetry-grpc-1.6:1.32.0-alpha")
    implementation ("com.google.guava:guava:33.0.0-jre")

    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
    implementation("io.opentelemetry.instrumentation:opentelemetry-grpc-1.6")

    //alpha modules
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }
    generateProtoTasks {
        all().configureEach {
            plugins {
                id("grpc")
            }
        }
    }
}

tasks.shadowJar {
    mergeServiceFiles()
}
