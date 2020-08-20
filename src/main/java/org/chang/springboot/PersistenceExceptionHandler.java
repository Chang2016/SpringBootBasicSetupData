package org.chang.springboot;

import javax.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class PersistenceExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	public ResponseEntity<Object> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
		Throwable th = e.getCause();
		ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder().build();
		if (th instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) th;
			response = new ApiErrorResponse.ApiErrorResponseBuilder()
					.withStatus(HttpStatus.BAD_REQUEST)
					.withErrorCode(cve.getSQLException().getSQLState())
					.withMessage(cve.getMessage())
					.withDetail(cve.getSQLException().getMessage()).build();
		}
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
