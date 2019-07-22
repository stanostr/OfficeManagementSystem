package com.stanostrovskii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.model.SingleMessageResponse;

@ControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(RequestException.class)
	public ResponseEntity<SingleMessageResponse> handleException(RequestException e) {
		return ResponseEntity.status(e.getHttpStatus())
				.body(new SingleMessageResponse(e.getMessage()));
	}
}
