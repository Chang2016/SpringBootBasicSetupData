package org.strupp.springboot;

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
	public ResponseEntity<String> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
		Throwable th = e.getCause();
		StringBuilder res = new StringBuilder();
		if (th instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) th;
			res.append(cve.getMessage());
			res.append("\nSQLState: " + cve.getSQLState());
			res.append("\nSQL: " + cve.getSQL());
			res.append("\nErrorCode: " + cve.getErrorCode());
		}
		return new ResponseEntity<String>(res.toString(), HttpStatus.BAD_REQUEST);
	}
}
