package com.vijay;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.entity.Column;
import com.vijay.entity.Task;
import com.vijay.service.KanbanService;

@RestController
@RequestMapping("/api/columns")
public class KanbanController {
    private final KanbanService kanbanService;

    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
        kanbanService.initializeColumns();
    }

    @GetMapping
    public List<Column> getColumns() {
        return kanbanService.getColumns();
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        boolean taskUpdated = kanbanService.updateTask(taskId, updatedTask);
        if (taskUpdated) {
            return ResponseEntity.ok("Task updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        boolean taskDeleted = kanbanService.deleteTask(taskId);
        if (taskDeleted) {
            return ResponseEntity.ok("Task deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Task> searchTask(@PathVariable Long taskId) {
        Task task = kanbanService.searchTask(taskId);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/task/{columnName}")
    public ResponseEntity<String> insertTask(@PathVariable String columnName, @RequestBody Task newTask) {
        boolean taskInserted = kanbanService.insertTask(columnName, newTask);
        if (taskInserted) {
            return ResponseEntity.ok("Task inserted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
