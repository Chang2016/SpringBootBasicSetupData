package org.strupp.springboot.integration;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class MySQLContainerTestJava {

  @Container
  private static final JMySQLContainer jMySQLContainer;
  static {
    MySQLContainer container = ((JMySQLContainer) (new JMySQLContainer("mysql:latest"))
    .withUsername("root")).withPassword("1234");
    jMySQLContainer = (JMySQLContainer) container;
  }

  public static final JMySQLContainer getjMySQLContainer() {
    return jMySQLContainer;
  }
}
