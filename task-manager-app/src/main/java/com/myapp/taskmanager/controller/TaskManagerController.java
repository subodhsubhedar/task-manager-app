package com.myapp.taskmanager.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.taskmanager.dto.TaskDTO;
import com.myapp.taskmanager.dto.TaskDTO.UpdateTaskValidateGroup;
import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.exception.TaskManagerServiceException;
import com.myapp.taskmanager.service.TaskManagerService;

@RestController
public class TaskManagerController {

	@Autowired
	private TaskManagerService taskMngrService;

	@GetMapping(value = "/login")
	public boolean login(HttpServletRequest request, HttpServletResponse response)
			throws TaskManagerServiceException, IOException, ServletException {

		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			System.out.println("##HEADER: " + headerName + " ##VALUE: " + request.getHeader(headerName));
		}

		System.out.println("Auth requets header : " + request.getHeaders("Authorization"));

		System.out.println("request received:" + request.getHeaderNames().toString());

		boolean result = request.authenticate(response);

		System.out.println("auth result :" + result);

		return result;

	}

	@GetMapping(value = "/tasks")
	public Set<Task> listAllTasks() throws TaskManagerServiceException {
		return taskMngrService.findAllTasks();
	}

	@GetMapping(value = "/parent-tasks")
	public Set<ParentTask> listAllParentTasks() throws TaskManagerServiceException {
		return taskMngrService.findAllParenTasks();
	}

	@GetMapping(value = "/task/{taskId}")
	public Task getTask(@PathVariable @Valid @NotNull Long taskId) throws TaskManagerServiceException {

		return taskMngrService.getTaskById(taskId);
	}

	@PostMapping(value = "/task/add")
	public Task addNewTask(@Valid @RequestBody TaskDTO task) throws TaskManagerServiceException {

		Task persistentTask = mapToEntity(task);
		return taskMngrService.createTask(persistentTask);
	}

	@PutMapping(value = "/task/update")
	public Task updateTask(@Validated({ UpdateTaskValidateGroup.class }) @RequestBody TaskDTO task)
			throws TaskManagerServiceException {
		Task persistentTask = mapToEntity(task);
		return taskMngrService.updateTask(persistentTask);
	}

	@DeleteMapping(value = "/task/delete/{taskId}")
	public void deleteTask(@PathVariable @Valid @NotNull Long taskId) throws TaskManagerServiceException {
		taskMngrService.deleteTaskById(taskId);
	}

	/**
	 * 
	 * @param task
	 * @return
	 * @throws TaskManagerServiceException
	 */
	private Task mapToEntity(TaskDTO task) throws TaskManagerServiceException {
		Task persistentTask = new Task();
		persistentTask.setTaskId(task.getTaskId());
		persistentTask.setTask(task.getTask());
		persistentTask.setStartDate(task.getStartDate());
		persistentTask.setEndDate(task.getEndDate());
		persistentTask.setPriority(task.getPriority());
		persistentTask.setTaskComplete(task.getTaskComplete());

		if (task.getParentTask() != null && task.getParentTask().getParentId() !=0) {
			ParentTask persistentParentTask = taskMngrService.getParentTaskById(task.getParentTask().getParentId());
			persistentTask.setParentTask(persistentParentTask);
		}
		return persistentTask;

	}

}
