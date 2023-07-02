package com.vijay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Add any custom query methods if needed
}
