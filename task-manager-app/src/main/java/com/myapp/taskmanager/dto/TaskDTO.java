package com.myapp.taskmanager.dto;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TaskDTO {

	public interface AddTaskValidateGroup {
	}

	public interface UpdateTaskValidateGroup {
	}

	private long taskId;

	@NotEmpty(message = "{task.task.invalid}", groups = { UpdateTaskValidateGroup.class })
	private String task;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "{task.startDate.invalid}", groups = { UpdateTaskValidateGroup.class })
	@FutureOrPresent(message = "{task.startDate.past}")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "{task.endDate.invalid}", groups = { UpdateTaskValidateGroup.class })
	@FutureOrPresent(message = "{task.endDate.past}")
	private LocalDate endDate;

	@NotNull(message = "{task.priority.invalid}", groups = { UpdateTaskValidateGroup.class })
	@Min(message = "{task.priority.negativeOrZero}", value = 1, groups = { UpdateTaskValidateGroup.class })
	private int priority;

	private ParentTaskDTO prntTask;

	@Nullable
	private Boolean taskComplete;

	public TaskDTO() {

	}

	public TaskDTO(String task, LocalDate startDate, LocalDate endDate, int priority, ParentTaskDTO parentTask) {
		super();
		this.task = task;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.prntTask = parentTask;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ParentTaskDTO getParentTask() {
		return prntTask;
	}

	public void setParentTask(ParentTaskDTO parentTask) {
		this.prntTask = parentTask;
	}

	public Boolean getTaskComplete() {
		return taskComplete;
	}

	public void setTaskComplete(Boolean taskComplete) {
		this.taskComplete = taskComplete;
	}

}
