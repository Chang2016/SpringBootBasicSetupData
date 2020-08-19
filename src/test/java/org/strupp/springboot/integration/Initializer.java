package org.strupp.springboot.integration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public final class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
    MySQLContainerTestJava.getjMySQLContainer().start();
    TestPropertyValues values =
        TestPropertyValues.of(
            new String[] {
                "spring.datasource.url="
                  + MySQLContainerTestJava.getjMySQLContainer().getJdbcUrl()
            });
    values.applyTo(applicationContext);
  }
}
