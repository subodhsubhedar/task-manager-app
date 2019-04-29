package com.myapp.taskmanager.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

public class TaskManagerErrorResponse {

	private LocalDateTime dateTime;

	private HttpStatus httpStatus;

	private List<String> errors;

	private Exception detailedException;

	public TaskManagerErrorResponse() {
		// Auto-generated constructor stub
	}

	public Exception getDetailedException() {
		return detailedException;
	}

	public void setDetailedException(Exception detailedException) {
		this.detailedException = detailedException;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
