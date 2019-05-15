package com.myapp.taskmanager.mapper;

import java.util.Set;

import com.myapp.taskmanager.dto.ParentTaskDTO;
import com.myapp.taskmanager.dto.TaskDTO;
import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;

/**
 * 
 * @author Admin
 *
 */
public interface TaskManagerEntityToDtoMapper {

	Task getMappedTaskEntity(TaskDTO taskDto);

	TaskDTO getMappedTaskDto(Task taskEntity);

	Set<TaskDTO> getMappedTaskDtoSet(Set<Task> taskEntitySet);

	Set<ParentTaskDTO> getMappedParentTaskDtoSet(Set<ParentTask> parentTaskEntitySet);

	ParentTask getMappedParentTaskEntity(ParentTaskDTO parentTaskDto);

	ParentTaskDTO getMappedParentTaskDto(ParentTask parentTaskEntity);
}
