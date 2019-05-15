package com.myapp.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.taskmanager.entity.Task;
/**
 * 
 * @author Admin
 *
 */
@Repository
public interface TaskManagerRepository extends JpaRepository<Task, Long>	{

	
	Optional<Task> findByTask(String taskDesc);

}
