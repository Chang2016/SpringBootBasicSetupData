package org.chang.springboot.course;

import java.time.LocalDate;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.chang.springboot.student.StudentDto;

@Builder
@Data
public class CourseDto {
  private Long id;
  private String name;
  private LocalDate startDate;
  private int size;
  private Set<StudentDto> students;
}
