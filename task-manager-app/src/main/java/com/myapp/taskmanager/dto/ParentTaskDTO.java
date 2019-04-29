package com.myapp.taskmanager.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParentTaskDTO {

	private long parentId;

	@NotEmpty(message = "{parentTask.parentTask.invalid}")
	private String parentTaskDesc;

	@JsonIgnore
	private Set<TaskDTO> subTasks;

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getParentTaskDesc() {
		return parentTaskDesc;
	}

	public void setParentTaskDesc(String parentTaskDesc) {
		this.parentTaskDesc = parentTaskDesc;
	}

	public Set<TaskDTO> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(Set<TaskDTO> subTasks) {
		this.subTasks = subTasks;
	}

	public void addSubTasks(TaskDTO subTask) {
		this.subTasks.add(subTask);
	}
}
