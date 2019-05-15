package com.myapp.taskmanager.mapper;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.myapp.taskmanager.dto.ParentTaskDTO;
import com.myapp.taskmanager.dto.TaskDTO;
import com.myapp.taskmanager.entity.ParentTask;
import com.myapp.taskmanager.entity.Task;

/**
 * 
 * @author Admin
 *
 */
@Component
public class TaskManagerEntityToDtoMapperImpl implements TaskManagerEntityToDtoMapper {

	@Override
	public Task getMappedTaskEntity(TaskDTO taskDto) {

		Task target = new Task();
		BeanUtils.copyProperties(taskDto, target);

		if (taskDto.getParentTask() != null) {
			ParentTask trgt = getMappedParentTaskEntity(taskDto.getParentTask());
			target.setParentTask(trgt);
		}

		return target;
	}

	@Override
	public TaskDTO getMappedTaskDto(Task taskEntity) {

		TaskDTO target = new TaskDTO();
		BeanUtils.copyProperties(taskEntity, target);

		if (taskEntity.getParentTask() != null) {
			ParentTaskDTO trgt = getMappedParentTaskDto(taskEntity.getParentTask());
			target.setParentTask(trgt);
		}

		return target;
	}

	@Override
	public Set<TaskDTO> getMappedTaskDtoSet(Set<Task> taskEntitySet) {
		Set<TaskDTO> taskDtoSetTarget = new LinkedHashSet<>();

		for (Task source : taskEntitySet) {
			TaskDTO target = new TaskDTO();
			BeanUtils.copyProperties(source, target);

			if (source.getParentTask() != null) {
				ParentTaskDTO trgt = getMappedParentTaskDto(source.getParentTask());
				target.setParentTask(trgt);
			}

			taskDtoSetTarget.add(target);
		}

		return taskDtoSetTarget;
	}

	@Override
	public ParentTask getMappedParentTaskEntity(ParentTaskDTO parentTaskDto) {
		ParentTask target = new ParentTask();
		BeanUtils.copyProperties(parentTaskDto, target);

		return target;
	}

	@Override
	public ParentTaskDTO getMappedParentTaskDto(ParentTask parentTaskEntity) {
		ParentTaskDTO target = new ParentTaskDTO();
		BeanUtils.copyProperties(parentTaskEntity, target);

		return target;
	}

	@Override
	public Set<ParentTaskDTO> getMappedParentTaskDtoSet(Set<ParentTask> parentTaskEntitySet) {
		Set<ParentTaskDTO> parentTaskDtoSetTarget = new LinkedHashSet<>();

		for (ParentTask source : parentTaskEntitySet) {
			ParentTaskDTO target = new ParentTaskDTO();
			BeanUtils.copyProperties(source, target);
			parentTaskDtoSetTarget.add(target);
		}

		return parentTaskDtoSetTarget;
	}

}
