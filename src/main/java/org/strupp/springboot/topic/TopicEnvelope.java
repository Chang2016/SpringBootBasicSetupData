package org.strupp.springboot.topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.strupp.springboot.model.api.ErrorMessageDto;

public class TopicEnvelope {

  public TopicEnvelope() {}

  public TopicEnvelope(TopicDto topicDto, ErrorMessageDto error) {
    this.topicDto = topicDto;
    this.error = error;
  }

  @JsonProperty("topicDto")
  private TopicDto topicDto;

  @JsonProperty("error")
  private ErrorMessageDto error;

  public void setTopicDto(TopicDto topicDto) {
    this.topicDto = topicDto;
  }

  public TopicDto getTopicDto() {
    return topicDto;
  }
}
