package org.strupp.springboot.integration;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class MySQLContainerTestJava {

  @Container
  private static final JMySQLContainer jMySQLContainer;
  static {
    MySQLContainer container = ((JMySQLContainer) (new JMySQLContainer("mysql:8.0"))
    .withDatabaseName("topic_db2").withUsername("root")).withPassword("1234");
    jMySQLContainer = (JMySQLContainer) container;
  }

  public static final JMySQLContainer getjMySQLContainer() {
    return jMySQLContainer;
  }
}
