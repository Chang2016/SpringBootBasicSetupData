package org.chang.springboot.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentDto {
  @JsonProperty("id")
  private Long id;
  @JsonProperty("birthday")
  private LocalDate birthday;
  @JsonProperty("name")
  private String name;
}
