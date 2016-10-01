package com.we.timetrack.service;

import java.util.Date;
import java.util.List;

import com.we.timetrack.model.Timesheet;

public class TimesheetDay {

	private Date date;
	private float hours;
	
	private List<Timesheet> timesheets;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	public float getHours() {
		return hours;
	}

	public void setHours(float hours) {
		this.hours = hours;
	}
	
}
