package org.chang.springboot;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  public ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
        .withStatus(status)
        .withErrorCode(HttpStatus.BAD_REQUEST.name())
        .withMessage(ex.getLocalizedMessage()).build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
        .withStatus(status)
        .withErrorCode(HttpStatus.BAD_REQUEST.name())
        .withMessage(ex.getLocalizedMessage()).build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
