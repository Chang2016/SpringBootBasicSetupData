package org.strupp.springboot.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessageDto {

  @JsonProperty("code")
  private Integer code;

  @JsonProperty("type")
  private String message;

  public ErrorMessageDto(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ErrorMessageDto fromResponseEnum(RestResponse restResponse) {
    return new ErrorMessageDto(restResponse.getCode(), restResponse.getMessage());
  }
}
