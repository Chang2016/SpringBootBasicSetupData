package org.chang.springboot.course;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.chang.springboot.model.api.ErrorMessageDto;
import org.chang.springboot.student.Student;
import org.chang.springboot.student.StudentDto;
import org.chang.springboot.student.StudentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@RestController
public class CourseController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private StudentTransformer studentTransformer;

  @Autowired
  private CourseTransformer courseTransformer;

  //	automatische Serialisierung nach JSON
  @RequestMapping(method = RequestMethod.GET, value = "courses/", params = {"page", "size"})
  public List<Course> retrieveCourses(@RequestParam("page") int page,
      @RequestParam("size") int size) {
    Page<Course> resultPage = courseService.retrieveCourses(page, size);
    return resultPage.getContent();
  }

  @RequestMapping(method = RequestMethod.GET, value = "courses/topics/{topicName}")
  public List<Course> retrieveCoursesByTopicName(@PathVariable String topicName) {
    return courseService.retrieveCoursesByTopicName(topicName);
  }

  @RequestMapping(method = RequestMethod.GET, value = "courses/{id}")
  public ResponseEntity<Course> retrieveCourse(@PathVariable long id) {
    Optional<Course> maybeCourse = courseService.retrieveCourse(id);
    return maybeCourse.map(ResponseEntity::ok)
        .orElseGet(() -> new ResponseEntity<>(new Course(), HttpStatus.NOT_FOUND));
  }

//	@RequestMapping(method=RequestMethod.POST, value="topics/{id}/courses/", produces="application/json;charset=UTF-8")
//	public void createCourse(@PathVariable long id, @Valid @RequestBody Course course) {
//		courseService.createCourse(course);
//	}

  @RequestMapping(method = RequestMethod.POST, value = "courses/", produces = "application/json;charset=UTF-8")
  public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course,
      HttpServletRequest request) {
    Course result = courseService.createCourse(course);
    UriComponents uri = ServletUriComponentsBuilder.fromRequest(request)
        .pathSegment(String.valueOf(result.getId())).build();
    System.out.println(uri.toString());
    return ResponseEntity.created(uri.toUri()).build();
  }

  @RequestMapping(method = RequestMethod.POST, value = "courses/{id}/students/", produces = "application/json;charset=UTF-8")
  public ResponseEntity<CourseEnvelope> addStudent(@PathVariable long id,
      @Valid @RequestBody StudentDto studentDto, HttpServletRequest request) {
    CourseEnvelope courseEnvelope = new CourseEnvelope();
		Student student = studentTransformer.toStudent(studentDto);
		CourseResult courseResult = courseService.addStudent(id, student);
    if(courseResult.isError()) {
      ErrorMessageDto errorMessageDto = ErrorMessageDto
          .fromResponseEnum(courseResult.getCourseStatusEnum());
      courseEnvelope.setErrorMessageDto(errorMessageDto);
      return new ResponseEntity<>(courseEnvelope, HttpStatus.BAD_REQUEST);
    } else {
      CourseDto courseDto = courseTransformer.toCourseDto(courseResult.getCourse());
      courseEnvelope.setCourseDto(courseDto);
      return new ResponseEntity<>(courseEnvelope, HttpStatus.OK);
    }
  }

  @RequestMapping(method = RequestMethod.PUT, value = "courses/{id}", produces = "application/json;charset=UTF-8")
  public ResponseEntity<Course> updateCourse(@PathVariable long id,
      @Valid @RequestBody Course course, HttpServletRequest request) {
    course.setId(id);
    Course result = courseService.updateCourse(course);
    UriComponents uri = ServletUriComponentsBuilder.fromRequest(request).build();
    return ResponseEntity.created(uri.toUri()).build();
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "courses/{id}")
  public void deleteCourse(@PathVariable long id, HttpServletResponse response) {
    courseService.deleteCourse(id);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }
}