package com.vijay.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vijay.entity.Column;
import com.vijay.entity.Task;
import com.vijay.repository.ColumnRepository;
import com.vijay.repository.TaskRepository;

@Service
public class KanbanServiceImpl implements KanbanService {

    private ColumnRepository columnRepository;
    private TaskRepository taskRepository;

    public KanbanServiceImpl(ColumnRepository columnRepository, TaskRepository taskRepository) {
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Column> getColumns() {
        return columnRepository.findAll();
    }

    @Override
    public boolean updateTask(Long taskId, Task updatedTask) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            // Update the task fields
            task.setName(updatedTask.getName());
            task.setIssueType(updatedTask.getIssueType());
            task.setPriority(updatedTask.getPriority());
            task.setAssignees(updatedTask.getAssignees());
            // Save the updated task
            taskRepository.save(task);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            taskRepository.delete(task);
            return true;
        }
        return false;
    }

    @Override
    public Task searchTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        return taskOptional.orElse(null);
    }

    @Override
    public boolean insertTask(String columnName, Task newTask) {
        Optional<Column> columnOptional = columnRepository.findByTitleIgnoreCase(columnName);
        if (columnOptional.isPresent()) {
            Column column = columnOptional.get();
            newTask.setId(null); // Let the database generate the ID
            column.getTasks().add(newTask);
            columnRepository.save(column);
            return true;
        }
        return false;
    }

    @Override
    public void initializeColumns() {
        if (columnRepository.count() == 0) {
            Task task1 = new Task(null, "Task 1", "Bug", "Highest", "John Doe", "Sort Summary 1", "Description 1");
            Task task2 = new Task(null, "Task 2", "Task", "Medium", "Jane Smith", "Sort Summary 2", "Description 2");
            Task task3 = new Task(null, "Task 3", "Story", "Low", "James Brown", "Sort Summary 3", "Description 3");
            Task task4 = new Task(null, "Task 4", "Bug", "High", "Emily Johnson", "Sort Summary 4", "Description 4");
            Task task5 = new Task(null, "Task 5", "Task", "Medium", "Michael Davis", "Sort Summary 5", "Description 5");
            Task task6 = new Task(null, "Task 6", "Story", "Low", "Olivia Wilson", "Sort Summary 6", "Description 6");
            Task task7 = new Task(null, "Task 7", "Bug", "Medium", "William Taylor", "Sort Summary 7", "Description 7");
            Task task8 = new Task(null, "Task 8", "Task", "High", "Sophia Anderson", "Sort Summary 8", "Description 8");

            Column backlogColumn = new Column();
            backlogColumn.setTitle("Backlog");
            backlogColumn.setTasks(new ArrayList<>(Arrays.asList(task1, task2, task3)));

            Column inProgressColumn = new Column();
            inProgressColumn.setTitle("In Progress");
            inProgressColumn.setTasks(new ArrayList<>(Arrays.asList(task4, task5)));

            Column doneColumn = new Column();
            doneColumn.setTitle("Done");
            doneColumn.setTasks(new ArrayList<>(Arrays.asList(task6, task7, task8)));

            columnRepository.saveAll(Arrays.asList(backlogColumn, inProgressColumn, doneColumn));
        }
    }
    
   }


