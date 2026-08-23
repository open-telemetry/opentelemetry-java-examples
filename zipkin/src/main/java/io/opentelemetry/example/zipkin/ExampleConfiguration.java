/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.example.zipkin;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.ServiceAttributes;

/**
 * All SDK management takes place here, away from the instrumentation code, which should only access
 * the OpenTelemetry APIs.
 */
public final class ExampleConfiguration {

  // Name of the service
  private static final String SERVICE_NAME = "myExampleService";

  /** Adds a SimpleSpanProcessor initialized with OtlpHttpSpanExporter to the TracerSdkProvider */
  static OpenTelemetry initializeOpenTelemetry(String ip, int port) {
    // Zipkin's OTLP endpoints are bound to its regular server port
    String endpoint = String.format("http://%s:%s/v1/traces", ip, port);
    SpanExporter spanExporter = OtlpHttpSpanExporter.builder().setEndpoint(endpoint).build();

    Resource serviceNameResource =
        Resource.create(Attributes.of(ServiceAttributes.SERVICE_NAME, SERVICE_NAME));

    // Set to process the spans by the OTLP exporter
    SdkTracerProvider tracerProvider =
        SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
            .setResource(Resource.getDefault().merge(serviceNameResource))
            .build();
    OpenTelemetrySdk openTelemetry =
        OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).buildAndRegisterGlobal();

    // add a shutdown hook to shut down the SDK
    Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::close));

    // return the configured instance so it can be used for instrumentation.
    return openTelemetry;
  }
}
