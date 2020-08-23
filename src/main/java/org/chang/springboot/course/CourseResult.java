package org.chang.springboot.course;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseResult {
  private CourseStatusEnum courseStatusEnum;
  private boolean error;
  private Course course;
}
