package org.chang.springboot.course;

import org.chang.springboot.model.api.RestResponse;

public enum CourseStatusEnum implements RestResponse {

  OK(1000, "ok"),

  COURSE_DOES_NOT_EXIST(1001,"course does not exist"),

  COURSE_IS_FULL(1002,"course is full"),

  STUDENT_ALREADY_IN_COURSE(1003,"student is already in course"),

  STUDENT_DOES_NOT_EXIST(1004,"student does not exist"),

  COURSE_HAS_NO_TOPIC(1005, "course has no topic"),

  TOO_MANY_STUDENTS(1006, "too many students for course size");

  private final String value;

  private final int code;

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
