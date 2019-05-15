package com.myapp.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.taskmanager.entity.ParentTask;
/**
 * 
 * @author Admin
 *
 */
@Repository
public interface ParentTaskManagerRepository extends JpaRepository<ParentTask, Long> {

}
