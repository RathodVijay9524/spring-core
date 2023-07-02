package com.vijay.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.http.ResponseEntity;

import com.vijay.KanbanController;

import com.vijay.entity.Task;
import com.vijay.service.KanbanService;

public class KanbanControllerTest {

	@Mock
	private KanbanService kanbanService;

	@InjectMocks
	private KanbanController kanbanController;



    @Test
    public void testInsertTask_Success() {
        // Create a mock KanbanService
        KanbanService kanbanService = Mockito.mock(KanbanService.class);
        
        // Create an instance of KanbanController
        KanbanController kanbanController = new KanbanController(kanbanService);
        
        // Prepare test data
        String columnName = "Backlog";
        Task newTask = new Task();
        newTask.setId(1L);
        newTask.setName("Task 22");
              
        
        // Configure the mock KanbanService behavior
        when(kanbanService.insertTask(columnName, newTask)).thenReturn(true);
        
        // Call the method under test
        ResponseEntity<String> response = kanbanController.insertTask(columnName, newTask);
        
        // Verify the response
        assertEquals(ResponseEntity.ok("Task inserted successfully"), response);
        
        // Verify the mock interactions
        Mockito.verify(kanbanService).insertTask(columnName, newTask);
    }
   

}
