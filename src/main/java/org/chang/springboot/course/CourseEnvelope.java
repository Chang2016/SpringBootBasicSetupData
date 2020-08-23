package org.chang.springboot.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.chang.springboot.model.api.ErrorMessageDto;

@Data
public class CourseEnvelope {
  @JsonProperty("courseDto")
  private CourseDto courseDto;
  @JsonProperty("errorMessageDto")
  private ErrorMessageDto errorMessageDto;
}
