package org.chang.springboot.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chang.springboot.jms.LocalDateDeserializer;
import org.chang.springboot.jms.LocalDateSerializer;
import org.chang.springboot.student.StudentDto;
import org.chang.springboot.topic.TopicDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
  @JsonProperty("id")
  private Long id;
  @JsonProperty("name")
  private String name;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonProperty("startDate")
  private LocalDate startDate;
  @JsonProperty("size")
  private int size;
  @JsonProperty("students")
  private Set<StudentDto> students;
  @JsonProperty("topicDto")
  private TopicDto topicDto;
}
