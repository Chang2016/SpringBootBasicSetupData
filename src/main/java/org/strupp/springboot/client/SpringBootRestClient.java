package org.strupp.springboot.client;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.strupp.springboot.topic.Topic;

//wegen SmokeTest auskommentiert, da @SpringBootTest nach @SpringBootApplication sucht um den Textcontext zu starten
//mehr als eine SpringBootApplication pro Projekt scheint nicht zu funktionieren
//@SpringBootApplication
public class SpringBootRestClient {
	private static final Logger log = LoggerFactory.getLogger(SpringBootRestClient.class);

	public static void main(String args[]) {
		/*
		 * Der Port muss geändert werden, da der Client sonst auch versucht auf port 8080 zu laufen
		 */
		Properties props = System.getProperties();
		props.setProperty("server.port", "8090");
		SpringApplication.run(SpringBootRestClient.class);
	}
	
	/*
	 * The RestTemplateBuilder is injected by Spring, and if you use it to create a RestTemplate then you will benefit from all 
	 * the autoconfiguration that happens in Spring Boot with message converters and request factories. We also extract the 
	 * RestTemplate into a @Bean to make it easier to test (it can be mocked more easily that way).
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Topic quote = restTemplate.getForObject("https://127.0.0.1:8443/topics/1", Topic.class);
			log.info(quote.getName().toString());
		};
	}
}
