package com.we.timetrack.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.CalendarRepository;
import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectStatus;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.model.TimesheetForm;

/**
 * @author fakadey
 *
 */
@Service
public class TimesheetManager {

	private static Logger logger = LoggerFactory.getLogger(TimesheetManager.class);

	@Autowired
	private TimesheetRepository timesheetRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private CalendarRepository calendarRepository;

	/**
	 * Returns a database Timesheets records with matching EmployeeId and later
	 * than (dateTask - week) ordered by days
	 * 
	 * @param week
	 *            - number of week, where 0 - current week, 1 - week before, -1
	 *            - week later
	 * @param model
	 *            - model where info will be save
	 */
	public List<TimesheetDay>  getTimesheetsByDays(int week) {

		// Current user
		Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return getTimesheetsByDays(employee.getEmployeeId(), week);

	}
	
	@Transactional(readOnly = true)
	public List<TimesheetDay> getTimesheetsByDays(UUID employeeId, int week) {
		LocalDate dateEarly = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).minusWeeks(week);		
		
		return getTimesheetsByDays(employeeId, new DateRange(dateEarly, dateEarly.plusDays(6)));
	}
	
	@Transactional(readOnly = true)
	public List<TimesheetDay> getTimesheetsByDays(UUID employeeId, DateRange period) {

		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employeeId, period.getBegin(),
				period.getEnd());
		
		logger.debug("Loaded timesheets count: " + timesheets.size() + " for employee " + employeeId
				+ ", dateEarly: " + period.getBegin());

		return TimesheetDay.getTimesheetsByDays(timesheets, period.getBegin(),
				period.getEnd(), calendarRepository.getDays(period.getBegin(), period.getEnd()));
	}

	/**
	 * Save new timesheet from current user
	 * 
	 * @param form
	 *            - form with params for new timesheet
	 * @param redirectAttributes
	 *            - attributes for save form params in redirect
	 */
	public void saveTimesheet(TimesheetForm form) {
		if (form != null) {

			// Current user
			Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			List<Timesheet> timesheets = new ArrayList<Timesheet>();

			String[] dates = form.getDates().split(", ");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			// Save new timesheet
			for (String dateStr : dates) {
				LocalDate date = LocalDate.parse(dateStr, dateFormat);
				Timesheet timesheet = new Timesheet();
				timesheet.setEmployeeId(employee.getEmployeeId());
				timesheet.setComment(form.getComment());
				timesheet.setCountTime(form.getCountTime());
				timesheet.setDateTask(date);

				Project project = new Project();
				project.setProjectId(Integer.parseInt(form.getProjectId()));
				timesheet.setProject(project);
				Task task = new Task();
				task.setTaskId(Integer.parseInt(form.getTaskId()));
				timesheet.setTask(task);
				timesheets.add(timesheet);
			}

			timesheetRepository.saveTimesheets(timesheets);
		}
	}

	/**
	 * Modify countTime in timesheet with matching timesheetId
	 * 
	 * @param timesheetId
	 * @param countTime
	 */
	@Transactional
	public void modifyTimesheet(int timesheetId, float countTime) {

		Timesheet timesheet = timesheetRepository.getTimesheet(timesheetId, false);
		timesheet.setCountTime(countTime);
		timesheetRepository.saveTimesheet(timesheet);
	}
	
	/**
	 * Modify comment in timesheet with matching timesheetId
	 * 
	 * @param timesheetId
	 * @param countTime
	 */
	@Transactional
	public void modifyTimesheet(int timesheetId, String comment) {

		Timesheet timesheet = timesheetRepository.getTimesheet(timesheetId, false);
		timesheet.setComment(comment);
		timesheetRepository.saveTimesheet(timesheet);
	}
	
	/**
	 * Delete choosen timesheet
	 * 
	 * @param timesheetId
	 *            - timesheet's id to delete
	 * @param model
	 *            - not use
	 */
	public void deleteTimesheet(int timesheetId) {

		Timesheet timesheet = new Timesheet();
		timesheet.setId(timesheetId);
		timesheetRepository.deleteTimesheet(timesheet);
	}

	/**
	 * Get list of all active projects with initialized tasks.
	 * @return
	 */
	@Transactional
	public List<Project> getProjects() {
		List<Project> projects = projectRepository.getProjects(ProjectStatus.Active);
		for (Project prj : projects) {
			Hibernate.initialize(prj.getTasks());
		}
		return projects;
	}
	
	/**
	 * Get list of all active tasks free of projects.
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Task> getTasks() {
		// Current user
		Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.debug("Get tasks for employee {}, department {}", employee.getShortName(), employee.getDepartment());
		return taskRepository.getFreeTasks(TaskStatus.Active, employee.getDepartment());
	}
}
