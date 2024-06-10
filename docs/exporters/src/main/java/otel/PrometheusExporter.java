package otel;

import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.resources.Resource;

public class PrometheusExporter {
  public static SdkMeterProvider create(Resource resource) {
    int prometheusPort = 9464;
    SdkMeterProvider sdkMeterProvider =
        SdkMeterProvider.builder()
            .registerMetricReader(PrometheusHttpServer.builder().setPort(prometheusPort).build())
            .setResource(resource)
            .build();

    return sdkMeterProvider;
  }
}
