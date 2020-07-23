package org.strupp.springboot.topic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TopicTransformer {

  public List<TopicDto> toListOfTopicDto(List<Topic> topics) {
    return topics.stream()
        .map(topic -> new TopicDto(topic.getId(), topic.getName()))
        .collect(Collectors.toList());
  }

  public TopicDto toTopicDto(Topic topic) {
    TopicDto dto = new TopicDto(topic.getId(), topic.getName());
    return dto;
  }
}
