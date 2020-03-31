package com.marvastsi.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends NullPointerException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String exception) {
		super(exception);
	}
}
