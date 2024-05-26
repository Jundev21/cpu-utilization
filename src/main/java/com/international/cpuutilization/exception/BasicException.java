package com.international.cpuutilization.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BasicException extends RuntimeException {

	protected final ErrorCode errorCode;

	public BasicException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
