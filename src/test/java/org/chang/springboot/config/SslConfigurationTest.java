package org.chang.springboot.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.chang.springboot.SpringBootBasicDataMain;
import org.chang.springboot.integration.FullIntegrationTest;
import org.chang.springboot.ssl.SslPropertyConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@TestPropertySource(properties = {"timezone = GMT", "server.port: 8443"})
@SpringBootTest(classes = {SpringBootBasicDataMain.class})
public class SslConfigurationTest extends FullIntegrationTest {

  @Autowired
  private SslPropertyConfig properties;

  @Test
  public void should_Populate_MyConfigurationProperties() {
    assertThat(properties.getPort(), is("8443"));
    assertThat(properties.getSsl().getKeystoretype(), is("PKCS12"));
    assertThat(properties.getSsl().getKeystorePassword(), is("123456"));
    assertThat(properties.getSsl().getKeystore(), is("classpath:keystore.p12"));
		assertThat(properties.getSsl().getKeyAlias(), is("tomcat"));
  }
}
