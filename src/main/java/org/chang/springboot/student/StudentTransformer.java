package org.chang.springboot.student;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StudentTransformer {

  public Set<StudentDto> toSetOfStudentDto(Set<Student> students) {
    return students.stream().map(s -> new StudentDto(s.getId(), s.getBirthday(), s.getName())).collect(
        Collectors.toSet());
  }

  public StudentDto toStudentDto(Student student) {
    return StudentDto.builder().id(student.getId()).name(student.getName()).birthday(student.getBirthday()).build();
  }

  public Student toStudent(StudentDto studentDto) {
    Student student = new Student();
    if(null != studentDto.getId()) {
      student.setId(studentDto.getId());
    }
    if(null != studentDto.getBirthday()) {
      student.setBirthday(studentDto.getBirthday());
    }
    if(null != studentDto.getName()) {
      student.setName(studentDto.getName());
    }
    return student;
  }

  public Set<Student> toSetOfStudents(Set<StudentDto> studentDtos) {
    return studentDtos.stream().map(this::toStudent).collect(Collectors.toSet());
  }
}
