package com.vijay.test;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.KanbanController;

import com.vijay.entity.Task;
import com.vijay.service.KanbanService;

@WebMvcTest(KanbanController.class)
public class BoardTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private KanbanService kanbanService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testUpdateTask_Success() throws Exception {
		// Prepare test data
		Long taskId = 1L;
		Task updatedTask = new Task();
		updatedTask.setId(taskId);
		updatedTask.setName("Updated Task");

		// Serialize the updated Task object to JSON
		String jsonRequest = objectMapper.writeValueAsString(updatedTask);

		// Configure the behavior of the kanbanService when updating a task
		Mockito.when(kanbanService.updateTask(Mockito.anyLong(), Mockito.any(Task.class))).thenReturn(true);

		// Perform the PUT request to update a task
		mockMvc.perform(MockMvcRequestBuilders.put("/api/columns/task/{taskId}", taskId)
				.contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Task updated successfully"));
	}

	@Test
	public void testDeleteTask_Success() throws Exception {
		// Prepare test data
		Long taskId = 1L;

		// Configure the behavior of the kanbanService when deleting a task
		Mockito.when(kanbanService.deleteTask(taskId)).thenReturn(true);

		// Perform the DELETE request to delete a task
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/columns/task/{taskId}", taskId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Task deleted successfully"));
	}

	@Test
	public void testSearchTask_Success() throws Exception {
		// Prepare test data
		Long taskId = 1L;
		Task task = new Task();
		task.setId(taskId);
		task.setName("Task 1");

		// Configure the behavior of the kanbanService when searching for a task
		Mockito.when(kanbanService.searchTask(taskId)).thenReturn(task);

		// Perform the GET request to search for a task
		mockMvc.perform(MockMvcRequestBuilders.get("/api/columns/task/{taskId}", taskId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(taskId))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Task 1"));
	}

	


}
