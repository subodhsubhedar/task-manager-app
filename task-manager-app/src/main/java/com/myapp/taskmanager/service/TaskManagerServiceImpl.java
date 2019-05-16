package com.myapp.taskmanager.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(TaskManagerServiceImpl.class);

	@Autowired
	private TaskManagerRepository repository;

	@Autowired
	private ParentTaskManagerRepository parentTaskRepository;

	@Override
	@Transactional
	public Set<Task> findAllTasks() throws TaskManagerServiceException {
		logger.debug("Calling repository for findAllTasks");
		try {

			return new LinkedHashSet<>(repository.findAll());

		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					"Exception Occured while retrieving all Tasks." + " -- " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Task createTask(Task task) throws TaskManagerServiceException {
		logger.debug("Calling repository for createTask Task {}", task);
		try {
			return repository.save(task);

		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					("Exception occured while creating Task :" + task + " -- " + e.getMessage()), e);
		}
	}

	@Override
	@Transactional
	public Task updateTask(Task task) throws TaskManagerServiceException {
		logger.debug("Calling repository for updateTask Task {}", task);
		try {

			Task entity = getTaskById(task.getTaskId());

			entity.setTask(task.getTask());
			entity.setStartDate(task.getStartDate());
			entity.setEndDate(task.getEndDate());
			entity.setPriority(task.getPriority());
			
			ParentTask parentTask =null;
			if (task.getParentTask() != null) {
				long parentTaskId = task.getParentTask().getParentId();

				if (parentTaskId != 0) {
					//check if parent task is registered as a parent in parent task table.
					parentTask = getParentTaskById(parentTaskId);
					
					if(parentTask==null) {
						parentTask =  task.getParentTask();
						
						//Add the Parent task entry in Parent task table
						parentTask = parentTaskRepository.saveAndFlush(parentTask);
					}
				}
			}

			entity.setParentTask(parentTask);
			entity.setTaskComplete(task.getTaskComplete());

			return repository.save(entity);
		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					("Exception occured while updating Task :" + task.getTaskId() + " -- " + e.getMessage()), e);
		}

	}

	@Override
	@Transactional
	public Task getTask(String taskDesc) throws TaskManagerServiceException {
		logger.debug("Calling repository for getTask taskDesc {}", taskDesc);
		Task task = null;
		try {

			Optional<Task> optional = repository.findByTask(taskDesc);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				logger.debug("Task Not found {}", taskDesc);
				throw new TaskManagerServiceException("Task Not found with desc :" + taskDesc);
			}
			return task;
		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					("Exception occured while retrieving task with desc" + taskDesc + " -- " + e.getMessage()), e);
		}
	}

	@Override
	@Transactional
	public Task getTaskById(Long taskId) throws TaskManagerServiceException {
		logger.debug("Calling repository for getTaskById taskId {}", taskId);
		Task task = null;
		try {

			Optional<Task> optional = repository.findById(taskId);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				logger.debug("getTaskById task not found");
				throw new TaskManagerServiceException("Task Not found with id :" + taskId);
			}
			return task;
		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					("Exception occured while retrieving Task with id :" + taskId + " -- " + e.getMessage()), e);
		}
	}

	@Override
	@Transactional
	public void deleteTaskById(Long taskId) throws TaskManagerServiceException {
		logger.debug("Calling repository for deleteTaskById taskId {}", taskId);
		try {
			Task task = null;

			Optional<Task> optional = repository.findById(taskId);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				logger.debug("getTaskById task not found");
				throw new TaskManagerServiceException("Task Not found with id :" + taskId);
			}

			repository.delete(task);

		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					("Exception occured while deleting task with id :" + taskId + " -- " + e.getMessage()), e);
		}
	}

	@Override
	public ParentTask getParentTaskById(Long taskId) throws TaskManagerServiceException {
		logger.debug("Calling repository for getParentTaskById taskId {}", taskId);
		ParentTask task = null;
		try {

			Optional<ParentTask> optional = parentTaskRepository.findById(taskId);

			if (optional.isPresent()) {
				task = optional.get();
			} else {
				logger.debug("getParentTaskById task not found");
			}
			return task;
		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					("Exception occured while retrieving Parent Task with id :" + taskId + " -- " + e.getMessage()), e);
		}
	} 

	@Override
	@Transactional
	public Set<ParentTask> findAllParenTasks() throws TaskManagerServiceException {
		logger.debug("Calling repository for findAllParenTasks");
		try {

			return new LinkedHashSet<>(parentTaskRepository.findAll());

		} catch (Exception e) {
			logger.error("TaskManagerServiceException e {}", e);
			throw new TaskManagerServiceException(
					"Exception Occured while retrieving all Tasks." + " -- " + e.getMessage(), e);
		}
	}

}
