package com.vijay.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_column")
public class Column {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ColumnId;
	private String processName;
	
	//@OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany(mappedBy = "column", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Task> taskList=new ArrayList<>();
	
	

	public Column() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getColumnId() {
		return ColumnId;
	}

	public void setColumnId(Long columnId) {
		ColumnId = columnId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public Column(Long columnId, String processName, List<Task> taskList) {
		super();
		ColumnId = columnId;
		this.processName = processName;
		this.taskList = taskList;
	}
	
	
	

}
