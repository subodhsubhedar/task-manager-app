package com.myapp.taskmanager.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;
import com.myapp.taskmanager.exception.TaskManagerServiceException;
import com.myapp.taskmanager.repository.ParentTaskManagerRepository;
import com.myapp.taskmanager.repository.TaskManagerRepository;

/**
 * ss
 * 
 * @author Admin
 *
 */
@Service
public class TaskManagerServiceImpl implements TaskManagerService {

	@Autowired
	private TaskManagerRepository repository;

	@Autowired
	private ParentTaskManagerRepository parentTaskRepository;

	@Override
	@Transactional
	public Set<Task> findAllTasks() throws TaskManagerServiceException {
		try {

			return new LinkedHashSet<>(repository.findAll());

		} catch (Exception e) {
			throw new TaskManagerServiceException(
					"Exception Occured while retrieving all Tasks." + " -- " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Task createTask(Task task) throws TaskManagerServiceException {
		try {
			return repository.save(task);

		} catch (Exception e) {
			throw new TaskManagerServiceException(
					("Exception occured while creating Task :" + task + " -- " + e.getMessage()), e);
		}
	}

	@Override
	@Transactional
	public Task updateTask(Task task) throws TaskManagerServiceException {

		try {

			Task entity = getTaskById(task.getTaskId());

			entity.setTask(task.getTask());
			entity.setStartDate(task.getStartDate());
			entity.setEndDate(task.getEndDate());
			entity.setPriority(task.getPriority());
			entity.setParentTask(task.getParentTask());

			return repository.save(entity);
		} catch (Exception e) {
			throw new TaskManagerServiceException(
					("Exception occured while updating Task :" + task.getTaskId() + " -- " + e.getMessage()), e);
		}

	}

	@Override
	@Transactional
	public Task getTask(String taskDesc) throws TaskManagerServiceException {

		Task task = null;
		try {

			Optional<Task> optional = repository.findByTaskDesc(taskDesc);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				throw new TaskManagerServiceException("Task Not found with desc :" + taskDesc);
			}
			return task;
		} catch (Exception e) {
			throw new TaskManagerServiceException(
					("Exception occured while retrieving task with desc" + taskDesc + " -- " + e.getMessage()), e);
		}
	}

	@Override
	@Transactional
	public Task getTaskById(Long taskId) throws TaskManagerServiceException {

		Task task = null;
		try {

			Optional<Task> optional = repository.findById(taskId);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				throw new TaskManagerServiceException("Task Not found with id :" + taskId);
			}
			return task;
		} catch (Exception e) {
			throw new TaskManagerServiceException(
					("Exception occured while retrieving Task with id :" + taskId + " -- " + e.getMessage()), e);
		}
	}

	@Override
	@Transactional
	public void deleteTaskById(Long taskId) throws TaskManagerServiceException {
		try {
			Task task = null;

			Optional<Task> optional = repository.findById(taskId);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				throw new TaskManagerServiceException("Task Not found with id :" + taskId);
			}

			repository.delete(task);

		} catch (Exception e) {
			throw new TaskManagerServiceException(
					("Exception occured while deleting task with id :" + taskId + " -- " + e.getMessage()), e);
		}
	}

	@Override
	public ParentTask getParentTaskById(Long taskId) throws TaskManagerServiceException {
		ParentTask task = null;
		try {

			Optional<ParentTask> optional = parentTaskRepository.findById(taskId);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				throw new TaskManagerServiceException("Parent Task Not found with id :" + taskId);
			}
			return task;
		} catch (Exception e) {
			throw new TaskManagerServiceException(
					("Exception occured while retrieving Parent Task with id :" + taskId + " -- " + e.getMessage()), e);
		}
	}

}
