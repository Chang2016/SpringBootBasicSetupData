package org.strupp.springboot.topic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.strupp.springboot.integration.DatabaseIntegrationTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TopicTestContainerJUnit5IT extends DatabaseIntegrationTest {

  @Autowired
  TopicRepository topicRepository;

  @Test
  void findExistingTopicUsingTopicRepository() {
    List<Topic> all = topicRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    assertThat(all.size()).isEqualTo(2);
  }
}
