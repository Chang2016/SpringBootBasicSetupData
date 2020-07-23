package org.strupp.springboot.topic;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class TopicList {

  @JsonProperty("topics")
  private List<TopicDto> topics;

  public TopicList() {}

  @JsonProperty("topics")
  public List<TopicDto> getTopics() {
    return topics;
  }

  public TopicList(List<TopicDto> topics) {
    this.topics = topics;
  }
}
