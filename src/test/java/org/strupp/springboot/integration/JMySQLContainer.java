package org.strupp.springboot.integration;

import org.testcontainers.containers.MySQLContainer;

public class JMySQLContainer extends MySQLContainer {
  public JMySQLContainer(String imageName) {
    super(imageName);
  }
}
