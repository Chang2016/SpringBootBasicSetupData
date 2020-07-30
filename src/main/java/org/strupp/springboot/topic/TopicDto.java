package org.strupp.springboot.topic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicDto {
  @JsonProperty("id")
  private long id;

  @JsonProperty("name")
  private String name;

  public TopicDto() {}

  public TopicDto(long id, String name) {
    this.id = id;
    this.name = name;
  }

  @JsonProperty("id")
  public long getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(long id) {
    this.id = id;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "TopicDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
