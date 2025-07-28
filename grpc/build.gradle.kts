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

var grpcVersion = "1.74.0" // CURRENT_GRPC_VERSION
var protobufVersion = "4.31.1"
var protocVersion = protobufVersion

dependencies {
    runtimeOnly ("io.grpc:grpc-netty-shaded:${grpcVersion}")
    implementation ("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation ("io.grpc:grpc-services:${grpcVersion}")
    implementation ("io.grpc:grpc-stub:${grpcVersion}")
    implementation ("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    implementation ("io.opentelemetry.instrumentation:opentelemetry-grpc-1.6:2.18.1-alpha")
    implementation ("com.google.guava:guava:33.4.8-jre")

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
