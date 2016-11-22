package com.we.timetrack.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.we.timetrack.model.Timesheet;

public interface TimesheetRepository {
	
	/**
     * Returns list of all timesheet database records with matching 
     * employeeId.
     */
	public List<Timesheet> getTimesheets(UUID employeeId);
	
	/**
     * Returns list of all timesheet database records with matching
     * employeeId and later task than dateTask.
     */
	public List<Timesheet> getTimesheets(UUID employeeId, LocalDate dateTask);
	
	/**
     * Returns list of all timesheet database records with matching
     * employeeId and later task than beginDate and early then endDate.
     */
	public List<Timesheet> getTimesheets(UUID employeeId, LocalDate beginDate, LocalDate endDate);	
	
	/**
	 * Returns list of all timesheet database records with matching
	 * projectId.
	 */
	public List<Timesheet> getTimesheets(int projectId);
	
	/**
	 * Returns list of all timesheet database records with matching
	 * projectId and later task than beginDate and early then endDate.
	 */
	public List<Timesheet> getTimesheets(int projectId, LocalDate beginaDate, LocalDate endDate);
	
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
     * Saves a list of timesheet's objects.
     */
	public void saveTimesheets(List<Timesheet> timesheets);
	
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
	//public List<TimesheetDay> getTimesheetsByDays(UUID employeeId, Date dateTask);
	
	/**
	 * Returns a summary hours group by projects and task
	 * with mathcing employeeId
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getEmployeeSummary(UUID employeeId);
	
	
}
