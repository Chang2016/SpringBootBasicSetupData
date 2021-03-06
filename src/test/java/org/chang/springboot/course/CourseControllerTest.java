package org.chang.springboot.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.chang.springboot.student.Student;
import org.chang.springboot.student.StudentDto;
import org.chang.springboot.student.StudentTransformer;
import org.chang.springboot.topic.TopicDto;
import org.chang.springboot.topic.TopicTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class CourseControllerTest {

  private CourseJmsMessageSender courseJmsMessageSender;

  private CourseService courseService;

  private StudentTransformer studentTransformer;

  private CourseTransformer courseTransformer;

  private TopicTransformer topicTransformer;
  //SUT
  private CourseController courseController;

  @Before
  public void init() {
    this.topicTransformer = new TopicTransformer();
    this.studentTransformer = new StudentTransformer();
    this.courseTransformer = new CourseTransformer(studentTransformer, topicTransformer);
    this.courseJmsMessageSender = mock(CourseJmsMessageSender.class);
    this.courseService = mock(CourseService.class);
    this.courseController= new CourseController(courseJmsMessageSender,
        courseService, studentTransformer, courseTransformer);
  }

  private CourseDto createCourseDto() {
    StudentDto studentDto = StudentDto.builder().id(1L).birthday(LocalDate.of(1988, 2, 3)).name("Bla").build();
    Set<StudentDto> students = new HashSet<>();
    students.add(studentDto);
    TopicDto topicDto = TopicDto.builder().id(1L).name("Topic1").build();
    CourseDto courseDto = new CourseDto();
    courseDto.setId(10L);
    courseDto.setName("Name1");
    courseDto.setStartDate(LocalDate.now().plusDays(10));
    courseDto.setSize(10);
    courseDto.setStudents(students);
    courseDto.setTopicDto(topicDto);
    return courseDto;
  }

  @Test
  public void checkCourseTransformerToCourse() {
    CourseDto courseDto = createCourseDto();
    TopicDto topicDto = courseDto.getTopicDto();
    Set<StudentDto> students = courseDto.getStudents();
    StudentDto studentDto = students.iterator().next();
    Course course = courseTransformer.toCourse(courseDto);
    assertThat(course.getId()).isEqualTo(courseDto.getId());
    assertThat(course.getName()).isEqualTo(courseDto.getName());
    assertThat(course.getStartDate()).isEqualTo(courseDto.getStartDate());
    assertThat(course.getSize()).isEqualTo(courseDto.getSize());
    assertThat(course.getStudents().size()).isEqualTo(students.size());
    Student next = course.getStudents().iterator().next();
    assertThat(next.getId()).isEqualTo(studentDto.getId());
    assertThat(next.getBirthday()).isEqualTo(studentDto.getBirthday());
    assertThat(next.getName()).isEqualTo(studentDto.getName());
    assertThat(course.getTopic().getId()).isEqualTo(topicDto.getId());
    assertThat(course.getTopic().getName()).isEqualTo(topicDto.getName());
  }

  @Test
  public void createCorrectCourse() {
    // when
    CourseDto courseDto = createCourseDto();
    Course course = courseTransformer.toCourse(courseDto);
    CourseResult correct = CourseResult.builder().course(course).error(false).courseStatusEnum(CourseStatusEnum.OK).build();
    when(courseService.createCourse(any())).thenReturn(correct);
    ResponseEntity<CourseEnvelope> courseEnvelope = courseController.createCourse(courseDto, new MockHttpServletRequest());
    assertThat(courseEnvelope.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void createIncorrectCourse() {
    // when
    CourseDto courseDto = createCourseDto();
    courseDto.setTopicDto(null);
    Course course = courseTransformer.toCourse(courseDto);
    CourseResult incorrect = CourseResult.builder().course(course).error(true).courseStatusEnum(CourseStatusEnum.COURSE_HAS_NO_TOPIC).build();
    when(courseService.createCourse(any())).thenReturn(incorrect);
    ResponseEntity<CourseEnvelope> courseEnvelope = courseController.createCourse(courseDto, new MockHttpServletRequest());
    assertThat(courseEnvelope.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
