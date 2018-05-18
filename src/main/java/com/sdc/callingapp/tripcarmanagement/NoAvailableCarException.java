package com.sdc.callingapp.tripcarmanagement;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class NoAvailableCarException extends RuntimeException {
	public NoAvailableCarException() {
		super("No available cars found.");
	}
}