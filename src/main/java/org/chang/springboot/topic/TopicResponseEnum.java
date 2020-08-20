package org.chang.springboot.topic;

import org.chang.springboot.model.api.RestResponse;

public enum TopicResponseEnum implements RestResponse {
  WRONG_LENGTH_OF_TOPICNAME(1000, "Wrong topic name size");

  private int code;
  private String message;

  TopicResponseEnum(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
