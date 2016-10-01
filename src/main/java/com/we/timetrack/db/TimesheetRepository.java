package com.we.timetrack.db;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Timesheet;

public interface TimesheetRepository {
	
	/**
     * Returns list of all timesheet database records with matching 
     * employeeId.
     */
	public List<Timesheet> getTimesheets(Employee employee);
	
	/**
     * Returns list of all timesheet database records with matching
     * employeeId and later task than dateTask.
     */
	public List<Timesheet> getTimesheets(Employee employee, Date dateTask);
	
	/**
	 * Returns list of all timesheet database records with matching
	 * projectId.
	 */
	public List<Timesheet> getTimesheets(Project project);
	
	/**
	 * Returns list of all timesheet database records.
	 * For test only.
	 */
	public List<Timesheet> getTimesheets();
	
	/**
     * Saves a Timesheet object.
     */
	public void saveTimesheet(Timesheet timesheet);
	
	/**
     * Deletes Timesheet record 
     */
	public void deleteTimesheet(Timesheet timesheet);
	
	/**
     * Returns a database Timesheet record with matching timesheetId
     */
	public Timesheet getTimesheet(int timesheetId, boolean doLock);
	
	/**
	 * Returns a database Timesheets records with matching EmployeeId
	 * and later than (dateTask - week) ordered by days
	 */
	//public List<TimesheetDay> getTimesheetsByDays(Employee employee, Date dateTask);
	
	/**
	 * Returns a summary hours group by projects and task
	 * with mathcing employeeId
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getEmployeeSummary(Employee employee);
	
	
}
