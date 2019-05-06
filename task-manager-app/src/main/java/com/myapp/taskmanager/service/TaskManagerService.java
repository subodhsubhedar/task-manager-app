package com.myapp.taskmanager.service;

import java.util.Set;

import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.exception.TaskManagerServiceException;

/**
 * 
 * @author Admin
 *
 */
public interface TaskManagerService {

	Set<Task> findAllTasks() throws TaskManagerServiceException;
	
	Set<ParentTask> findAllParenTasks() throws TaskManagerServiceException;

	Task createTask(Task task) throws TaskManagerServiceException;

	Task updateTask(Task task) throws TaskManagerServiceException;

	Task getTask(String taskDesc) throws TaskManagerServiceException;

	Task getTaskById(Long taskId) throws TaskManagerServiceException;
	
	ParentTask getParentTaskById(Long taskId) throws TaskManagerServiceException;

	void deleteTaskById(Long taskId) throws TaskManagerServiceException;

}
