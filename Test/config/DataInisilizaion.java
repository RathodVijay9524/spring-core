package com.vijay.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.vijay.entity.Column;
import com.vijay.entity.Task;
import com.vijay.repository.ColumnRepository;
import com.vijay.repository.TaskRepository;

@Component
public class DataInisilizaion implements CommandLineRunner {
	
	private ColumnRepository columnRepository;
	private TaskRepository taskRepository;
	

	@Autowired
	public DataInisilizaion(ColumnRepository columnRepository, TaskRepository taskRepository) {
		super();
		this.columnRepository = columnRepository;
		this.taskRepository = taskRepository;
	}


	@Override
	public void run(String... args) throws Exception {
		
		List<Task> lists=new ArrayList<>();
		
		Column column1=new Column();
		column1.setProcessName("TO DO");
		column1.setTaskList(lists);
		Column column2=new Column();
		column2.setProcessName("OnGoing");
		Column column3=new Column();
		column3.setProcessName("Done");
		columnRepository.saveAll(List.of(column1,column3,column2));
		
		
		
		
		Task task1=new Task();
		task1.setTaskName("Task 1");
		task1.setDiscription("Create Navbar Usinng HTML And CSS");
		task1.setColumn(column1);
		Task task2=new Task();
		task2.setTaskName("Task 2");
		task2.setDiscription("Create Login Usinng HTML And CSS");
		task2.setColumn(column1);
		Task task3=new Task();
		task3.setTaskName("Task 3");
		task3.setDiscription("Create Sinup Usinng HTML And CSS");
		task3.setColumn(column1);
		
		lists.add(task1);
		lists.add(task2);
		lists.add(task3);
		
		Task task4=new Task();
		task4.setTaskName("Task 1");
		task4.setDiscription("Create Navbar Usinng HTML And CSS");
		task4.setColumn(column2);
		Task task5=new Task();
		task5.setTaskName("Task 2");
		task5.setDiscription("Create Table Usinng HTML And CSS");
		task5.setColumn(column2);
		Task task6=new Task();
		task6.setTaskName("Task 3");
		task6.setDiscription("Create Logo Usinng HTML And CSS");
		task6.setColumn(column2);
		
		Task task7=new Task();
		task7.setTaskName("Task 1");
		task7.setDiscription("Create Box Usinng HTML And CSS");
		task7.setColumn(column3);
		Task task8=new Task();
		task8.setTaskName("Task 2");
		task8.setDiscription("Create Heding Usinng HTML And CSS");
		task8.setColumn(column3);
		
		
		
		
		
		
		
		taskRepository.saveAll(List.of(task1,task2,task3,task4,task5,task6,task7,task8));
		
		
		
		
	}

}
