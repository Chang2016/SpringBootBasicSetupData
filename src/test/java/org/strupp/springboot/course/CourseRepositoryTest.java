package org.strupp.springboot.course;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.strupp.springboot.topic.Topic;

import static org.assertj.core.api.Assertions.assertThat;
/*
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 * configuring H2, an in-memory database
 * setting Hibernate, Spring Data, and the DataSource
 * performing an @EntityScan
 * turning on SQL logging
 */
@RunWith(SpringRunner.class)
//@Profile("test")
//@Configuration
@ActiveProfiles(profiles = "test")
@DataJpaTest
public class CourseRepositoryTest {
	
//	@Configuration
//	class Conf {
//		public Conf() {}
//		@Bean
//		public Topic topic() {
//			Topic topic = new Topic();
//			topic.setName("Musik");
//			return topic;
//		}
//	}
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	private CourseRepository repository;
	
//	@Autowired 
//	private Topic topic;
	
	@Test
	public void testCustomRepository() {
		// given
	    Topic topic = new Topic();
	    topic.setName("Religion");
	    entityManager.persist(topic);
	    entityManager.flush();
	    Course c1 = new Course();
	    c1.setName("Christentum");
	    c1.setStartDate(LocalDate.of(1999, 3, 4));
	    c1.setTopic(topic);
	    Course c2 = new Course();
	    c2.setName("Judentum");
	    c2.setStartDate(LocalDate.of(1956, 12, 4));
	    c2.setTopic(topic);
	    Course c3 = new Course();
	    c3.setName("Islam");
	    c3.setStartDate(LocalDate.of(1922, 3, 4));
	    c3.setTopic(topic);
	    entityManager.persist(c1);
	    entityManager.persist(c2);
	    entityManager.persist(c3);
	    entityManager.flush();
	    
	    // when
	    List<Course> found = (List<Course>) repository.findAll();
	    found.stream().forEach(c -> System.out.println(c.getName()));
	    assertThat(found.size()).isEqualTo(3);
	}
 
//	@Bean
//	@Primary
//	public CourseService courseService() {
//		return Mockito.mock(CourseService.class);
//	}
	
//	@Bean
//	@Primary
//	public CourseRepository courseRepository() {
//		return Mockito.mock(CourseRepository.class);
//	}
}
