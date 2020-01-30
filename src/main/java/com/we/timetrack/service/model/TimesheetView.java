package com.we.timetrack.service.model;

import com.we.timetrack.model.Timesheet;

public class TimesheetView {
	
	private int id;
	private String project;
	private int projectId;
	private String task;
	private int taskId;
	private float countTime;
	private String comment;
	
	public TimesheetView(Timesheet timesheet) {
		this.id = timesheet.getId();
		this.project = timesheet.getProject().getName();
		this.task = timesheet.getTask().getName();
		this.countTime = timesheet.getCountTime();
		this.comment = timesheet.getComment();
		this.projectId = timesheet.getProject().getProjectId();
		this.taskId = timesheet.getTask().getTaskId();
	}
	
	public int getId() {
		return id;
	}
	public String getProject() {
		return project;
	}
	public String getTask() {
		return task;
	}
	public float getCountTime() {
		return countTime;
	}
	public String getComment() {
		return comment;
	}

	public int getProjectId() {
		return projectId;
	}

	public int getTaskId() {
		return taskId;
	}
	
	

}
