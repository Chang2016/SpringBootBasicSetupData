package org.strupp.springboot.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 * configuring H2, an in-memory database
 * setting Hibernate, Spring Data, and the DataSource
 * performing an @EntityScan
 * turning on SQL logging
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@DataJpaTest
public class CourseRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CourseRepository repository;


  @Test
  public void testCustomRepository() {
    // when
    List<Course> found = (List<Course>) repository.findAll();
    assertThat(found.size()).isEqualTo(3);
  }

}
