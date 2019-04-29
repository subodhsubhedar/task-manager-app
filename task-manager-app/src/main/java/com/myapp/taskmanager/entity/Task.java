package com.myapp.taskmanager.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "task")
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3373094650936392401L;

	@Id
	@GeneratedValue
	@Column(name = "Task_ID", nullable = false)
	private long taskId;

	@NotEmpty(message = "{task.task.invalid}")
	@Column(name = "Task")
	private String taskDesc;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "{task.startDate.invalid}")
	@FutureOrPresent(message = "{task.startDate.past}")
	@Column(name = "Start_Date")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "{task.endDate.invalid}")
	@FutureOrPresent(message = "{task.endDate.past}")
	@Column(name = "End_Date")
	private LocalDate endDate;

	@NotNull(message = "{task.priority.invalid}")
	@Min(message = "{task.priority.negativeOrZero}", value = 1)
	@Column(name = "Priority")
	private int priority;

	@Nullable
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private ParentTask prntTask;

	public Task(String task, LocalDate startDate, LocalDate endDate, int priority, ParentTask parentTask) {
		super();
		this.taskDesc = task;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.prntTask = parentTask;
	}

	public Task() {
	
	}
	
	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTask() {
		return taskDesc;
	}

	public void setTask(String task) {
		this.taskDesc = task;
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

	public ParentTask getParentTask() {
		return prntTask;
	}

	public void setParentTask(ParentTask parentTask) {
		this.prntTask = parentTask;
	}
}
