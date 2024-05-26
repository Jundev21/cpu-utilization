package com.international.cpuutilization.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
	Integer statusCode;
	String message;
	private final T error_message;

	public static <T> ErrorResponse<T> responseBodyData(HttpStatus httpStatus, T dataBody) {
		return new ErrorResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), dataBody);
	}
}


