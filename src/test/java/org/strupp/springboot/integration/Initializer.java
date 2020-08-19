package org.strupp.springboot.integration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public final class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
    MySQLContainerTestJava.getJMySQLContainer().start();
    TestPropertyValues values =
        TestPropertyValues.of(
            "spring.datasource.url="
              + MySQLContainerTestJava.getJMySQLContainer().getJdbcUrl());
    values.applyTo(applicationContext);
  }
}
