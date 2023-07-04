package com.vijay.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String issueType;
    private String priority;
    private String assignees;
    private String sortSummary;
    private String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getAssignees() {
		return assignees;
	}
	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}
	public String getSortSummary() {
		return sortSummary;
	}
	public void setSortSummary(String sortSummary) {
		this.sortSummary = sortSummary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Task(Long id, String name, String issueType, String priority, String assignees, String sortSummary,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.issueType = issueType;
		this.priority = priority;
		this.assignees = assignees;
		this.sortSummary = sortSummary;
		this.description = description;
	}
	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
