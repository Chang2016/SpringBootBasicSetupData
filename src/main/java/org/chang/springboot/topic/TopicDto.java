package org.chang.springboot.topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
  @JsonProperty("id")
  private long id;
  @JsonProperty("name")
  @Size(min=2, max=255)
  private String name;
}
