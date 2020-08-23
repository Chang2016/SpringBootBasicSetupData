package org.chang.springboot.course;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.chang.springboot.student.Student;
import org.chang.springboot.student.StudentDto;
import org.chang.springboot.student.StudentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseTransformer {
  @Autowired
  private StudentTransformer studentTransformer;

  public List<CourseDto> toListOfCourseDto(List<Course> courses) {
    return courses.stream().map(this::toCourseDto).collect(Collectors.toList());
  }

  public CourseDto toCourseDto(Course course) {
    Set<StudentDto> studentDtos = studentTransformer.toSetOfStudentDto(course.getStudents());
    return CourseDto.builder().id(course.getId()).name(course.getName()).startDate(course.getStartDate()).size(course.getSize()).students(studentDtos).build();
  }

  public Course toCourse(CourseDto courseDto) {
    Course course = new Course();
    if(null != courseDto.getId()) {
      course.setId(courseDto.getId());
    }
    if(null != courseDto.getName()) {
      course.setName(courseDto.getName());
    }
    if(null != courseDto.getStartDate()) {
      course.setStartDate(courseDto.getStartDate());
    }
    if(null != courseDto.getStudents()) {
      Set<Student> students = studentTransformer.toSetOfStudents(courseDto.getStudents());
      for(Student s : students) {
        course.addStudent(s);
      }
    }
    course.setSize(courseDto.getSize());
    return course;
  }
}
