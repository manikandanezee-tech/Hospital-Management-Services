package com.ezeeinfo.hospitalmanagementservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?>handleServiceException(ServiceException serviceException){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceException.getMessage());
	}
}
