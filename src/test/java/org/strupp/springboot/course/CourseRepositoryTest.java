package org.strupp.springboot.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.strupp.springboot.integration.DatabaseIntegrationTest;

/*
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 * configuring H2, an in-memory database
 * setting Hibernate, Spring Data, and the DataSource
 * performing an @EntityScan
 * turning on SQL logging
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@SpringBootTest
public class CourseRepositoryTest extends DatabaseIntegrationTest {

  @Autowired
  private CourseRepository repository;

  @Test
  public void testCustomRepository() {
    // when
    List<Course> found = repository.findAll();
    assertThat(found.size()).isEqualTo(3);
  }

  @Test
  public void findCourseByName() {
    // when
    List<Course> found = repository.findByName("Christentum");
    assertThat(found.size()).isEqualTo(1);
  }

  @Test
  public void findCoursesWithTopicId() {
    // when
    List<Course> found = repository.findByTopicId(1);
    assertThat(found.size()).isEqualTo(3);
  }
}
