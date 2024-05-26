package com.international.cpuutilization.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	LIMIT_MINUTES_ERROR(HttpStatus.BAD_REQUEST,"최근 1주일 데이터만 제공됩니다."),
	LIMIT_HOUR_ERROR(HttpStatus.BAD_REQUEST,"최근 3달 데이터만 제공됩니다."),
	LIMIT_DAY_ERROR(HttpStatus.BAD_REQUEST,"최근 1년 데이터만 제공됩니다."),
	MISS_PARAM_ERROR(HttpStatus.BAD_REQUEST,"날짜를 입력해주세요"),
	DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST,"날짜 형식이 올바르지 않습니다."),
	INVALID_DATE(HttpStatus.BAD_REQUEST,"종료날짜와 시작날짜가 올바르지 않습니다.");

	private final HttpStatus httpStatus;
	private final String ErrorMessage;
}
