package com.myapp.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.taskmanager.entity.Task;

@Repository
public interface TaskManagerRepository extends JpaRepository<Task, Long>	{

	
	Optional<Task> findByTaskDesc(String taskDesc);

}
