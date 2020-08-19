package org.strupp.springboot.topic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.strupp.springboot.integration.FullIntegrationTest;


@RunWith(SpringRunner.class)
@DirtiesContext
//startet den Webserver zum Test
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class TopicControllerWithoutMockingIT extends FullIntegrationTest {

  @LocalServerPort
  private int port;
  @Autowired
  TopicController topicController;
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void smokeTest() {
    assertThat(topicController).isNotNull();
  }

  //	Der Nachteil bei diesem Test ist, dass der tatsächliche Wert aus der DB abgerufen wird, also auch der DAO-Layer genutzt wird.
  @Test
  public void testTopicControllerGetWithoutMocking() {
    Topic topic = this.restTemplate.getForObject("https://localhost:" + port + "/topics/1",
        Topic.class);
    assertThat(topic.getName()).isEqualTo("Religion");
  }
}
