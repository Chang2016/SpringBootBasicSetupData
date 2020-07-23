package org.strupp.springboot.course;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.strupp.springboot.model.student.StudentRepository;
import org.strupp.springboot.topic.Topic;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class CourseServiceTest {
	
	@TestConfiguration
	static class CourseServiceTestContextConfiguration {
		@Bean
		public CourseService courseService() {
			return new CourseService();
		}
	}
	
	@Autowired
	CourseService courseService;
	
	@MockBean
	private CourseRepository repositoryMock;
	
	@MockBean
	private CustomCourseRepository customRepositoryMock;
	
	@MockBean
	private StudentRepository studentRepository;
	private final String TOPICNAME = "Religion";
	private Course courseWithoutDateAndTopic;
	private Course courseWithoutTopic;
	private Course course;
	
	@Before
	public void init() {
		courseWithoutDateAndTopic = new Course();
		courseWithoutDateAndTopic.setName("Christentum");
	    
		courseWithoutTopic = new Course();
		courseWithoutTopic.setName("Judentum");
		courseWithoutTopic.setStartDate(LocalDate.of(1999, 3, 4));
	    
		Topic topic = new Topic();
		topic.setName("Religion");
		course = new Course();
		course.setName("Islam");
		course.setStartDate(LocalDate.of(1999, 1, 4));
		course.setTopic(topic);
	    
	    when(customRepositoryMock.getCoursesHavingTopic(TOPICNAME)).thenAnswer((Answer<List<Course>>) invocationOnMock -> {
    		List<Course> courses = new ArrayList<>();
    		courses.add(new Course());
    		return courses;
    	});
	}
	
	@Test
	public void testWriteCourseWithoutDateAndTopic() {
		courseService.createCourse(courseWithoutDateAndTopic);
		verify(repositoryMock).save(courseWithoutDateAndTopic);
	}
	
	@Test
	public void testWriteCourseWithoutTopic() {
		courseService.createCourse(courseWithoutTopic);
		verify(repositoryMock).save(courseWithoutTopic);
	}
	
	@Test
	public void testWriteCourse() {
		courseService.createCourse(course);
		verify(repositoryMock).save(course);
	}
	
	@Test
	public void testFindCourseByTopicName() {
		List<Course> erg = courseService.retrieveCoursesByTopicName(TOPICNAME);
		verify(customRepositoryMock).getCoursesHavingTopic(TOPICNAME);
		assertThat(erg, is(notNullValue()));
		assertThat(Integer.valueOf(erg.size()), is(equalTo(1)));
	}
	
	@Test
	public void testFindCourseWithNonexistingTopic() {
		when(customRepositoryMock.getCoursesHavingTopic(anyString())).thenAnswer((Answer<List<Course>>) invocationOnMock -> {
    		List<Course> courses = new ArrayList<>();
    		return courses;
    	});
		List<Course> erg = courseService.retrieveCoursesByTopicName("blabla");
		verify(customRepositoryMock).getCoursesHavingTopic("blabla");
		assertThat(erg, is(notNullValue()));
		assertThat(Integer.valueOf(erg.size()), is(equalTo(0)));
	}
}
