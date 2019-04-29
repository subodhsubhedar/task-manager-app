package com.myapp.taskmanager.controller;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.taskmanager.dto.TaskDTO;
import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.exception.TaskManagerServiceException;
import com.myapp.taskmanager.service.TaskManagerService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TaskManagerController {

	@Autowired
	private TaskManagerService taskMngrService;

	@GetMapping(value = "/tasks")
	public Set<Task> listAllBooks() throws TaskManagerServiceException {
		return taskMngrService.findAllTasks();
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
	public Task updateTask(@Valid @RequestBody TaskDTO task) throws TaskManagerServiceException {
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
		persistentTask.setTask(task.getTask());
		persistentTask.setStartDate(task.getStartDate());
		persistentTask.setEndDate(task.getEndDate());
		persistentTask.setPriority(task.getPriority());

		if (task.getParentTask() != null) {
			ParentTask persistentParentTask = taskMngrService.getParentTaskById(task.getParentTask().getParentId());
			persistentTask.setParentTask(persistentParentTask);
		}
		return persistentTask;

	}

}
