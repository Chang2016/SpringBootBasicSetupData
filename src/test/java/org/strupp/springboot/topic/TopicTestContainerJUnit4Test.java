package org.strupp.springboot.topic;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;

public class TopicTestContainerJUnit4Test {

  @Rule
  public MySQLContainer mysql = new MySQLContainer();

  @Before
  public void init() {
    String host = mysql.getHost();
    Integer firstMappedPort = mysql.getFirstMappedPort();
  }

  @Test
  public void smoke() throws Exception {
    assertThat(1).isEqualTo(1);
  }
}
