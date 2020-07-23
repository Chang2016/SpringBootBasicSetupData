package org.strupp.springboot.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2431407691911700557L;
	
	public ResourceNotFoundException(Exception exception) {
		super(exception);
	}
	
	public ResourceNotFoundException(String exception) {
		super(exception);
	}
}
