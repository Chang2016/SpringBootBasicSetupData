package org.strupp.springboot.course;

import java.time.Instant;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.strupp.springboot.model.student.Student;
import org.strupp.springboot.model.student.StudentRepository;
import org.strupp.springboot.topic.Topic;

import static org.mockito.ArgumentMatchers.any;
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

	@Captor
	private ArgumentCaptor<Course> listCaptor;

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
	public void testFindCourseWithNonExistingTopic() {
		when(customRepositoryMock.getCoursesHavingTopic(anyString())).thenAnswer((Answer<List<Course>>) invocationOnMock -> {
    		List<Course> courses = new ArrayList<>();
    		return courses;
    	});
		List<Course> erg = courseService.retrieveCoursesByTopicName("blabla");
		verify(customRepositoryMock).getCoursesHavingTopic("blabla");
		assertThat(erg, is(notNullValue()));
		assertThat(Integer.valueOf(erg.size()), is(equalTo(0)));
	}

	@Test
	public void retrieveNonExistingCourse() {
		when(repositoryMock.findById(any())).thenReturn(Optional.empty());
		Optional<Course> maybeCourse = courseService.retrieveCourse(1);
		assertThat(maybeCourse.isPresent(), is(false));
	}

	@Test
	public void retrieveExistingCourse() {
		Course course = new Course(1, "eee", LocalDate.now());
		when(repositoryMock.findById(any())).thenReturn(Optional.of(course));
		Optional<Course> maybeCourse = courseService.retrieveCourse(1);
		assertThat(maybeCourse.isPresent(), is(true));
		Course course1 = maybeCourse.get();
		assertThat(course1, is(equalTo(course)));
	}

//	@Ignore
	@Test
	public void updateCourse() {
		//given
		Student newStudent = new Student();
		newStudent.setBirthday(LocalDate.of(1988, 2, 6));
		newStudent.setName("Neumann");
		Student oldStudent = new Student();
		oldStudent.setBirthday(LocalDate.of(1978, 2, 6));
		oldStudent.setName("Altmann");
		oldStudent.setCreated(Instant.now());
		ReflectionTestUtils.setField(oldStudent, "id", 1L);
		Course course = new Course(1L, "Mock", LocalDate.of(2021, 1, 1));
		course.addStudent(newStudent);
		course.addStudent(oldStudent);

		//when
		courseService.updateCourse(course);
		verify(repositoryMock).save(listCaptor.capture());
		Course value = listCaptor.getValue();
		assertThat(value, equalTo(course));
	}
}
