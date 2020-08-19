package org.strupp.springboot.integration;

import org.testcontainers.junit.jupiter.Container;

public class MySQLContainerTestJava {

  @Container
  private static final JMySQLContainer jMySQLContainer;

  static {
    jMySQLContainer = new JMySQLContainer("mysql:8.0");
  }

  public static JMySQLContainer getJMySQLContainer() {
    return jMySQLContainer;
  }
}
