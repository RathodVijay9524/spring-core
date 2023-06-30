package com.vijay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
