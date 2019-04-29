package com.myapp.taskmanager.exception;

/**
 * 
 * @author Admin
 *
 */

public class TaskManagerServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskManagerServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TaskManagerServiceException(String message) {
		super(message);
	}

}
