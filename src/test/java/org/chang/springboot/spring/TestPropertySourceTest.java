package org.chang.springboot.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test der TestPropertySource von Spring testet
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = {"lala=blabla"})
public class TestPropertySourceTest {

  @Value("${spring.datasource.url}")
  private String url;

  @Autowired
  private Environment env;

  @Test
  public void testStagingProperty() {
    assertThat(env.getProperty("lala"), is("blabla"));
  }
}
