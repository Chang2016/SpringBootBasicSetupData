package org.strupp.springboot.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.strupp.springboot.SpringBootBasicDataMain;
import org.strupp.springboot.ssl.SslPropertyConfig;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = { "timezone = GMT", "server.port: 8443" }) 
@SpringBootTest(classes = {SpringBootBasicDataMain.class})
public class SslConfigurationTest {
	
//	@Autowired
//	private SslPropertyConfig.Ssl sslproperties;
	
	@Autowired
	private SslPropertyConfig properties;
		
	@Test
    public void should_Populate_MyConfigurationProperties() {
		assertThat(properties.getPort(), is("8443"));
//		assertThat(sslproperties.getKeystorePassword(), is("123456"));
//		assertThat(sslproperties.getKeystorePath(), is("classpath:mykeystore_old"));
//		assertThat(sslproperties.getKeystoretype(), is("PKS"));
//		assertThat(sslproperties.super.getPort(), is("8443"));
    }
}
