package com.international.cpuutilization.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataResponse<T>{
	Integer statusCode;
	String message;
	private final T data;

public static <T> DataResponse<T> responseBodyData(HttpStatus httpStatus, T dataBody){
	return new DataResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), dataBody);
}
}
