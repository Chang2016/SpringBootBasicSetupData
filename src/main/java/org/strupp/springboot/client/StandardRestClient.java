package org.strupp.springboot.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.strupp.springboot.topic.Topic;
import org.strupp.springboot.topic.TopicList;

public class StandardRestClient {

  private static final Logger log = LoggerFactory.getLogger(StandardRestClient.class);
  private final RestTemplate restTemplate;
  private final String baseUrl;

  public StandardRestClient(RestTemplate restTemplate, String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  public Optional<Topic> findTopicHavingId(long id) {
    try {
      Topic topic = restTemplate.getForObject(baseUrl + "/topics/" + id, Topic.class);
      if (null != topic) {
        log.info(topic.getName());
        return Optional.of(topic);
      }
    } catch (RestClientException e) {
      log.error("An exception occurred!", e);
    }
    return Optional.empty();
  }

  public TopicList findTopics() {
    try {
      TopicList topics = restTemplate.getForObject(baseUrl + "/topics/", TopicList.class);
      return topics;
    } catch (RestClientException e) {
      log.error("An exception occurred!", e);
    }
    return new TopicList();
  }

  public static void main(String args[]) {
    String baseUrl = "https://localhost:8443";
    String pathToKeystore = "file:///Users/michaelstrupp/projects/training/springboot/SpringBootBasicSetupData/src/main/resources/keystore.p12";
    SslRestTemplateInitializer sslRestTemplateInitializer = new SslRestTemplateInitializer();
    Optional<RestTemplate> maybeRestTemplate = sslRestTemplateInitializer
        .initClient(pathToKeystore);
    if (maybeRestTemplate.isPresent()) {
      StandardRestClient client = new StandardRestClient(maybeRestTemplate.get(), baseUrl);
//		client.findTopicHavingId(1).orElse(new Topic());
//      client.findTopicHavingId(1);
      client.findTopics().getTopics().stream().forEach(System.out::println);
    }
  }
}
