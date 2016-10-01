package com.we.timetrack.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TimesheetForm {

	private String projectId;
	private String taskId;
	private Date dateTask;
	private float countTime;
	private String comment;

	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getDateTask() {
		return dateTask;
	}
	
	public void setDateTask(Date dateTask) {
		this.dateTask = dateTask;
	}
	
	public float getCountTime() {
		return countTime;
	}
	public void setCountTime(float countTime) {
		this.countTime = countTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
