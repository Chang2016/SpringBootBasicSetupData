package org.chang.springboot.course;

import java.util.Optional;
import org.chang.springboot.student.Student;
import org.springframework.stereotype.Component;

@Component
public class CourseStatusValidator {

  public CourseStatusEnum checkCourse(Optional<Course> maybeCourse, Student student) {
    if (maybeCourse.isPresent()) {
      return isSpaceForStudentsInCourse(maybeCourse.get(), student);
    } else {
      return CourseStatusEnum.COURSE_DOES_NOT_EXIST;
    }
  }

  private CourseStatusEnum isSpaceForStudentsInCourse(Course course, Student student) {
    if (course.getSize() <= course.getStudents().size()) {
      return CourseStatusEnum.COURSE_IS_FULL;
    } else if (student.getId() != null) {
      boolean b = course.getStudents().stream().map(Student::getId)
          .anyMatch(id -> id.equals(student.getId()));
      if (b) {
        return CourseStatusEnum.STUDENT_ALREADY_IN_COURSE;
      }
    }
    return CourseStatusEnum.OK;
  }
}
