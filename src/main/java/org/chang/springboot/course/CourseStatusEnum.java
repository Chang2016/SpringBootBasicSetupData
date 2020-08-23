package org.chang.springboot.course;

import org.chang.springboot.model.api.RestResponse;

public enum CourseStatusEnum implements RestResponse {

  OK(1000, "ok"),

  COURSE_DOES_NOT_EXIST(1001,"course does not exist"),

  COURSE_IS_FULL(1002,"course is full"),

  STUDENT_ALREADY_IN_COURSE(1003,"student is already in course"),

  STUDENT_DOES_NOT_EXIST(1004,"student does not exist");

  private String value;

  private int code;

  CourseStatusEnum(int code, String value) {
    this.code = code;
    this.value = value;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return value;
  }
}
