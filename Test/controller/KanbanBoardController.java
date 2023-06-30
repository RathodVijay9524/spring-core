package com.vijay.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.entity.Column;
import com.vijay.entity.Task;
import com.vijay.exception.ResourceNotFoundException;
import com.vijay.repository.ColumnRepository;
import com.vijay.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class KanbanBoardController {

	@Autowired
	private ColumnRepository columnRepository;
	@Autowired
	private TaskRepository taskRepository;

	@GetMapping
	public List<Column> getAllColumns() {
		return columnRepository.findAll();
	}

	@GetMapping("/{columnId}")
	public Column getColumn(@PathVariable Long columnId) {
		return columnRepository.findById(columnId)
				.orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + columnId));
	}

	@GetMapping("/{columnId}/cards")
	public List<Task> getCardsForColumn(@PathVariable Long columnId) {
		Column column = columnRepository.findById(columnId)
				.orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + columnId));
		return column.getTaskList();
	}

	@PostMapping("/{columnId}/cards")
	public Task createCard(@PathVariable Long columnId, @RequestBody Task card) {
		Column column = columnRepository.findById(columnId)
				.orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + columnId));
		card.setColumn(column);
		return taskRepository.save(card);
	}

	@DeleteMapping("/cards/{cardId}")
	public void deleteCard(@PathVariable Long cardId) {
		Task card = taskRepository.findById(cardId)
				.orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
		taskRepository.delete(card);
	}
	
	

}
