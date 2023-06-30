package com.vijay.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tbl_task")
public class Task {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taskId;
	private String taskName;
	private String discription;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "column_id", referencedColumnName = "ColumnId")
    @JsonIgnoreProperties("taskList")
	private Column column;
	
	

	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Task(Long taskId, String taskName, String discription, Column column) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.discription = discription;
		this.column = column;
	}
	

}
