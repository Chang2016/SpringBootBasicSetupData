package org.strupp.springboot.topic;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.strupp.springboot.authentication.OAuth2SecurityConfiguration;
import org.strupp.springboot.topic.Topic;

@TestConfiguration
//@EnableJpaRepositories("org.spring.springboot")
public class TopicTestConfiguration {

	@Bean
	public WebSecurityConfigurerAdapter topic() {
		WebSecurityConfigurerAdapter conf = new OAuth2SecurityConfiguration();
		ClientDetailsService service = new InMemoryClientDetailsService();
		
		return conf;
	}
	
//	@Bean
//	public Course courseWithoutTopic() {
//		Course course = new Course();
//		course.setName("Klassik");
//		return course;
//	}
}
