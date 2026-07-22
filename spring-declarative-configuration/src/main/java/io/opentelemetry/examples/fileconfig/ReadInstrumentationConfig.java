/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.examples.fileconfig;

import io.opentelemetry.api.incubator.config.ConfigProvider;
import io.opentelemetry.api.incubator.config.DeclarativeConfigProperties;
import org.springframework.stereotype.Component;

/** Example of reading instrumentation configuration from application code. */
@Component
public class ReadInstrumentationConfig {

  private final ConfigProvider configProvider;

  public ReadInstrumentationConfig(ConfigProvider configProvider) {
    this.configProvider = configProvider;
  }

  public boolean isDbQuerySanitizationEnabled() {
    DeclarativeConfigProperties dbConfig =
        configProvider
            .getInstrumentationConfig()
            .get("java")
            .get("common")
            .get("db")
            .get("query_sanitization");
    return dbConfig.getBoolean("enabled", true);
  }
}
