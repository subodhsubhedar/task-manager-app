package com.myapp.taskmanager.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Admin
 *
 */
@RestControllerAdvice
public class TaskManagerCustomExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger mylogger = LoggerFactory.getLogger(TaskManagerCustomExceptionHandler.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		mylogger.error("handleMethodArgumentNotValid", ex);

		TaskManagerErrorResponse errRespObj = buildErrorResponse(ex, status);

		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		errRespObj.setErrors(errors);
		mylogger.error("handleMethodArgumentNotValid errors {}", errors);

		return new ResponseEntity<>(errRespObj, headers, status);
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(HttpServletResponse response, HttpHeaders headers,
			Exception ex, HttpStatus status) throws IOException {
		mylogger.error("handleConstraintViolationException", ex);

		TaskManagerErrorResponse errRespObj = buildErrorResponse(ex, status);

		mylogger.error("handleMethodArgumentNotValid errRespObj {}", errRespObj);

		return new ResponseEntity<>(errRespObj, headers, status);

	}

	private TaskManagerErrorResponse buildErrorResponse(Exception ex, HttpStatus status) {

		TaskManagerErrorResponse errRespObj = new TaskManagerErrorResponse();

		errRespObj.setDateTime(LocalDateTime.now());
		errRespObj.setHttpStatus(status);
		/*
		 * Optional attribute for sending detailed message.
		 */
		errRespObj.setDetailedException(ex.getMessage());
		return errRespObj;
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		mylogger.error("handleMissingPathVariable", ex);
		TaskManagerErrorResponse errRespObj = buildErrorResponse(ex, status);

		errRespObj
				.setErrors(Arrays.asList("errors", "Missing path variable : " + ex.getParameter().getParameterName()));

		mylogger.error("handleMissingPathVariable errRespObj {}", errRespObj);
		return new ResponseEntity<>(errRespObj, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		mylogger.error("handleNoHandlerFoundException", ex);

		TaskManagerErrorResponse errRespObj = buildErrorResponse(ex, status);

		mylogger.error("handleNoHandlerFoundException errRespObj {}", errRespObj);
		return new ResponseEntity<>(errRespObj, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		TaskManagerErrorResponse errRespObj = buildErrorResponse(ex, status);
		mylogger.error("handleHttpRequestMethodNotSupported errRespObj{}", errRespObj);

		return new ResponseEntity<>(errRespObj, headers, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Throwable ex, HttpServletResponse response)
			throws IOException {

		mylogger.error("handleGenericException ex {}", ex);

		TaskManagerErrorResponse errRespObj = new TaskManagerErrorResponse();

		errRespObj.setDateTime(LocalDateTime.now());
		errRespObj.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

		errRespObj.setErrors(Arrays.asList(ex.getMessage()));
		mylogger.error("handleGenericException errRespObj {}", errRespObj);

		return new ResponseEntity<>(errRespObj, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
