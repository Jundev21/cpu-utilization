package com.international.cpuutilization.exception;

import static com.international.cpuutilization.exception.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.international.cpuutilization.common.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse<String>> basicErrorHandler(BasicException basic) {
		log.error("에러 발생 {} ", basic.toString());
		ErrorResponse<String> response = ErrorResponse.responseBodyData(basic.errorCode.getHttpStatus(),
			basic.errorCode.getErrorMessage());
		return new ResponseEntity<>(response, basic.errorCode.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse<String>> handleTypeMismatchException(
		MethodArgumentTypeMismatchException basic) {
		log.error("에러 발생 {} ", basic.getMessage());
		ErrorResponse<String> response = ErrorResponse.responseBodyData(DATE_FORMAT_ERROR.getHttpStatus(),
			DATE_FORMAT_ERROR.getErrorMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse<String>> handleBindException(BindException basic) {
		log.error("에러 발생 {} ", basic.getMessage());
		ErrorResponse<String> response = ErrorResponse.responseBodyData(DATE_FORMAT_ERROR.getHttpStatus(),
			DATE_FORMAT_ERROR.getErrorMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse<String>> handleMissingParams(MissingServletRequestParameterException basic) {
		log.error("에러 발생 {} ", basic.getParameterName() + "없음");
		ErrorResponse<String> response = ErrorResponse.responseBodyData(MISS_PARAM_ERROR.getHttpStatus(),
			basic.getParameterName() + " " + MISS_PARAM_ERROR.getErrorMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
