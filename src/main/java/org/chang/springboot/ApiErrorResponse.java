package org.chang.springboot;

import org.springframework.http.HttpStatus;

//siehe https://www.javadevjournal.com/spring/exception-handling-for-rest-with-spring/

public class ApiErrorResponse {

  private HttpStatus status;
  private String errorCode;
  private String message;
  private String detail;

  // getter and setters
  // Builder
  public static final class ApiErrorResponseBuilder {

    private HttpStatus status;
    private String errorCode;
    private String message;
    private String detail;

    ApiErrorResponseBuilder() {
    }

    public static ApiErrorResponseBuilder anApiErrorResponse() {
      return new ApiErrorResponseBuilder();
    }

    public ApiErrorResponseBuilder withStatus(HttpStatus status) {
      this.status = status;
      return this;
    }

    public ApiErrorResponseBuilder withErrorCode(String errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public ApiErrorResponseBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public ApiErrorResponseBuilder withDetail(String detail) {
      this.detail = detail;
      return this;
    }

    public ApiErrorResponse build() {
      ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
      apiErrorResponse.status = this.status;
      apiErrorResponse.errorCode = this.errorCode;
      apiErrorResponse.detail = this.detail;
      apiErrorResponse.message = this.message;
      return apiErrorResponse;
    }
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }

  public String getDetail() {
    return detail;
  }
}
