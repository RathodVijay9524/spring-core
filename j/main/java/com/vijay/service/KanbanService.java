package com.vijay.service;

import java.util.List;

import com.vijay.entity.Column;
import com.vijay.entity.Task;

public interface KanbanService {
    List<Column> getColumns();
    boolean updateTask(Long taskId, Task updatedTask);
    boolean deleteTask(Long taskId);
    Task searchTask(Long taskId);
    boolean insertTask(String columnName, Task newTask);
    void initializeColumns();
}
