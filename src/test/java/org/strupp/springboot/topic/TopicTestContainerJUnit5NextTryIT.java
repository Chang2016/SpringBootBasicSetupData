package org.strupp.springboot.topic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ActiveProfiles(profiles="test")
@Testcontainers
class TopicTestContainerJUnit5NextTryIT {

  @Container
  private static final MySQLContainer<?> database = new MySQLContainer<>();

  @Autowired
  private TopicRepository topicRepository;

  @Test
  void smoke() {
    assertThat(1).isEqualTo(1);
  }

  @Test
  void findAllTopics() {
    List<Topic> all = topicRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
  }

  @Test
  void deleteTopicReferencingCourses() {
    assertThrows(DataIntegrityViolationException.class, () -> topicRepository.deleteById(1L));
  }

  @DynamicPropertySource
  static void databaseProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", database::getJdbcUrl);
    registry.add("spring.datasource.username", database::getUsername);
    registry.add("spring.datasource.password", database::getPassword);
  }
}
