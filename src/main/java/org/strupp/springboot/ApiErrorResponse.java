package org.strupp.springboot;

import org.springframework.http.HttpStatus;

//siehe https://www.javadevjournal.com/spring/exception-handling-for-rest-with-spring/

public class ApiErrorResponse {

	private HttpStatus status;
	private String error_code;
	private String message;
	private String detail;

	// getter and setters
	// Builder
	public static final class ApiErrorResponseBuilder {
		private HttpStatus status;
		private String error_code;
		private String message;
		private String detail;

		ApiErrorResponseBuilder() {}

		public static ApiErrorResponseBuilder anApiErrorResponse() {
			return new ApiErrorResponseBuilder();
		}

		public ApiErrorResponseBuilder withStatus(HttpStatus status) {
			this.status = status;
			return this;
		}

		public ApiErrorResponseBuilder withError_code(String error_code) {
			this.error_code = error_code;
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
			apiErrorResponse.error_code = this.error_code;
			apiErrorResponse.detail = this.detail;
			apiErrorResponse.message = this.message;
			return apiErrorResponse;
		}
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getError_code() {
		return error_code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}
}