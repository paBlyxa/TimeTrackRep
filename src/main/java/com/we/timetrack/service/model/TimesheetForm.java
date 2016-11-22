package com.we.timetrack.service.model;

public class TimesheetForm {

	private String projectId;
	private String taskId;
	private String dates;
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

	public String getDates() {
		return dates;
	}
	
	public void setDates(String dates) {
		this.dates = dates;
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
